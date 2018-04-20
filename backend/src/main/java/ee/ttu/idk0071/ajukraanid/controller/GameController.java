package ee.ttu.idk0071.ajukraanid.controller;
//
import ee.ttu.idk0071.ajukraanid.config.GameConfig;
import ee.ttu.idk0071.ajukraanid.database.*;
import ee.ttu.idk0071.ajukraanid.guard.Guard;
import ee.ttu.idk0071.ajukraanid.guard.GuardException;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static ee.ttu.idk0071.ajukraanid.message.Message.createFetchStateResponse;
import static ee.ttu.idk0071.ajukraanid.message.Message.createSuccessResponse;

@Component
class GameController {
    private final Logger log = LoggerFactory.getLogger(this.getClass());
    private final ExecutorService executor = Executors.newCachedThreadPool();
    private final Random random = new Random();
    private final Guard guard;
    private final Database database;
    private final ResourceLoader resourceLoader;
    private final GameConfig gameConfig;

    @Autowired
    private GameController(Database database, Guard guard, ResourceLoader resourceLoader, GameConfig gameConfig) {
        this.database = database;
        this.guard = guard;
        this.resourceLoader = resourceLoader;
        this.gameConfig = gameConfig;
        loadPlainQuestions(); // TODO Questions created once only not on every launch.
    }

    /**
     * @return fetches the game state. It will always be possible to generate a unique game unless there are over
     * 9999 current games.
     */
    String createGame() {
        archiveExpiredGames();
        List<Integer> usedCodes = database.getGames().stream()
                .filter(game -> game.getGameState() != Game.State.INACTIVE)
                .map(Game::getGameCode)
                .collect(Collectors.toList());
        int[] availableCodes = IntStream.range(1_000, 10_000)
                .filter(code -> !usedCodes.contains(code))
                .toArray();
        if (availableCodes.length == 0) {
            throw new GuardException("Too many active games, could not create a new instance");
        }
        int randomIndex = random.nextInt(availableCodes.length);
        int gameCode = availableCodes[randomIndex];
        Game game = new Game(database, gameCode);
        selectRandomQuestions().forEach(question -> new Question(game, question.getText()));
        return new JSONObject()
                .put("Action", "CreateGame")
                .put("Code", gameCode).toString();
    }

    String joinGame(int gameCode, String playerName) {
        Optional<Game> optionalGame = findActiveGame(gameCode);

        if (!optionalGame.isPresent()) {
            throw new GuardException("Did not find such game with game code: " + gameCode);
        }

        guard.checkJoinGame(optionalGame.get());

        if (findPlayerIgnoreCase(optionalGame.get(), playerName).isPresent()) {
            throw new GuardException("Such username is already taken.");
        }

        new Player(optionalGame.get(), playerName);
        return createSuccessResponse("You have successfully joined the game");
    }

    /**
     * @param gameCode the unique code to each game
     * @return the game state, if it is in lobby then return the list of player names.
     */
    String startGame(int gameCode) {
        Optional<Game> optionalGame = findActiveGame(gameCode);

        if (!optionalGame.isPresent()) {
            throw new GuardException("Was not able to start the game because such game was not found.");
        }

        guard.checkStartGame(optionalGame.get());

        executor.submit(new GameRunner(optionalGame.get()));
        return createSuccessResponse("Game successfully started");
    }

    String fetchState(int gameCode) {
        Optional<Game> optionalGame = findActiveGame(gameCode);

        if (!optionalGame.isPresent()) {
            throw new GuardException("No game found with game code: " + gameCode);
        }

        Game game = optionalGame.get();
        Game.State gameState = optionalGame.get().getGameState();

        Object data;
        switch (gameState) {
            case LOBBY:
                JSONArray players = new JSONArray();
                game.getPlayers().forEach(player -> players.put(player.toString()));
                data = players;
                break;
            case ANSWERING:
                data = getCurrentQuestion(game).getText();
                break;
            case GRADING:
                JSONArray answers = new JSONArray();
                getCurrentQuestion(game).getAnswers().forEach(answer -> answers.put(new JSONObject()
                        .put("Name", answer.getPlayer().getName())
                        .put("Answer", answer.getText())));
                data = answers;
                break;
            case RESULTS:
                JSONArray points = new JSONArray();
                game.getPlayers().forEach(player -> points.put(new JSONObject()
                        .put("Name", player.getName())
                        .put("Points", getPoints(game, player))));
                data = points;
                break;
            case INACTIVE:
                data = null;
                break;
            case ERROR:
                data = "The game with ID " + gameCode + " is in Error state";
                break;
            default:
                data = "The game with ID " + gameCode + " is in an unrecognized state";
                break;
        }
        return createFetchStateResponse(gameState.toString(), data);
    }

    String submitAnswer(int gameCode, String playerName, String answer) {
        // TODO Need Question Number
        Optional<Game> optionalGame = findActiveGame(gameCode);
        if (!optionalGame.isPresent()) {
            throw new GuardException("Did not find such game with game code: " + gameCode);
        }

        guard.checkSubmitAnswer(optionalGame.get());

        Optional<Player> optionalPlayer = findPlayer(optionalGame.get(), playerName);
        if (!optionalPlayer.isPresent()) {
            throw new GuardException("Wrong username was given");
        }

        Question currentQuestion = getCurrentQuestion(optionalGame.get());

        boolean hasAnswered = currentQuestion.getAnswers().stream()
                .anyMatch(ans -> ans.getPlayer().getName().equals(playerName));
        if (hasAnswered) {
            throw new GuardException("You already answered the question");
        }
        new Answer(currentQuestion, optionalPlayer.get(), answer);
        return createSuccessResponse("Your answer was submitted.");
    }

    String givePoints(int gameCode, String giverName, String targetName) {
        // TODO Need Question Number
        Optional<Game> optionalGame = findActiveGame(gameCode);

        if (!optionalGame.isPresent()) {
            throw new GuardException("Game with such id was not found");
        }

        Game game = optionalGame.get();

        guard.checkGivePoints(game);

        Optional<Player> optionalGiver = findPlayer(game, giverName);
        Optional<Player> optionalTarget = findPlayer(game, targetName);

        if (!optionalGiver.isPresent() || !optionalTarget.isPresent()) { // TODO Different exceptions for giver/target
            throw new GuardException("Wrong username was given");
        }

        Player giver = optionalGiver.get();
        Player target = optionalTarget.get();

        if (giver == target) {
            throw new GuardException("You cannot give points to yourself");
        }

        Question currentQuestion = getCurrentQuestion(game);
        ArrayList<Evaluation> evaluations = currentQuestion.getEvaluations();

        boolean hasAlreadyGiven = evaluations.stream()
                .map(Evaluation::getGiver)
                .anyMatch(player -> player == giver);

        if (hasAlreadyGiven) {
            throw new GuardException("Can not give points, because you already gave points");
        }

        new Evaluation(currentQuestion, giver, target);
        return createSuccessResponse("Your points were given to " + targetName);
    }

    String removePlayer(int gameCode, String playerName) {
        Optional<Game> optionalGame = findActiveGame(gameCode);

        if (!optionalGame.isPresent()) {
            throw new GuardException("Did not find such game with game code: " + gameCode);
        }

        guard.checkRemovePlayer(optionalGame.get());

        Optional<Player> optionalPlayer = findPlayer(optionalGame.get(), playerName);

        if (!optionalPlayer.isPresent()) {
            throw new GuardException("Invalid player name: " + playerName);
        }

        optionalGame.get().getPlayers().remove(optionalPlayer.get());
        optionalPlayer.get().setValid(false);
        return createSuccessResponse("Player successfully removed: " + playerName);
    }

    String getPlayers(int gameCode) {
        Optional<Game> optionalGame = findActiveGame(gameCode);

        if (!optionalGame.isPresent()) {
            throw new GuardException("Did not find such game with game code: " + gameCode);
        }

        JSONArray players = new JSONArray();
        optionalGame.get().getPlayers().forEach(player -> players.put(player.toString()));
        return new JSONObject()
                .put("Action", "GetPlayers")
                .put("Players", players)
                .toString();
    }

    // private methods

    private Optional<Player> findPlayerIgnoreCase(Game game, String name) {
        return game.getPlayers().stream()
                .filter(player -> player.getName().equalsIgnoreCase(name))
                .findAny();
    }

    private Optional<Player> findPlayer(Game game, String name) {
        return game.getPlayers().stream()
                .filter(player -> player.getName().equals(name))
                .findAny();
    }

    private Optional<Game> findActiveGame(int gameCode) {
        return database.getGames().stream()
                .filter(game -> game.getGameState() != Game.State.INACTIVE)
                .filter(game -> game.getGameCode() == gameCode)
                .findAny();
    }

    private Question getCurrentQuestion(Game game) {
        return game.getQuestions().get(game.getQuestionNumber());
    }

    private int getPoints(Game game, Player player) {
        return (int) getCurrentQuestion(game).getEvaluations().stream()
                .map(Evaluation::getTarget)
                .filter(target -> player == target)
                .count() * 100;
    }

    private void archiveExpiredGames() {
        database.getGames().forEach(game -> {
            switch (game.getGameState()) {
                case INACTIVE:
                    break;
                case ERROR:
                    log.error("Archive task: Game with ID {} in Error state", game.getGameCode());
                default:
                    if (new Date().getTime() - game.getTimestamp().getTime() > 1000 * 60 * gameConfig.getGameTimeoutMinutes()) {
                        game.setGameState(Game.State.INACTIVE);
                        log.info("Archive task: Game with ID {} has expired and has been archived", game.getGameCode());
                    }
            }
        });
    }

    private List<PlainQuestion> selectRandomQuestions() {
        int questionAmount = database.getPlainQuestions().size();

        if (questionAmount < gameConfig.getQuestionsPerGame()) {
            throw new GuardException("Not enough questions in database to choose from, found " + questionAmount +
                    ", required " + gameConfig.getQuestionsPerGame());
        }

        List<Integer> allIndices = IntStream.range(0, database.getPlainQuestions().size())
                .boxed()
                .collect(Collectors.toList());

        List<Integer> selectedIndices = new ArrayList<>();

        for (int i = 0; i != gameConfig.getQuestionsPerGame(); ++i) {
            int randomIndex = random.nextInt(allIndices.size());
            selectedIndices.add(allIndices.get(randomIndex));
            allIndices.remove(randomIndex);
        }

        return selectedIndices.stream()
                .map(i -> database.getPlainQuestions().get(i))
                .collect(Collectors.toList());
    }

    private void loadPlainQuestions() {
        try (InputStream inStr = resourceLoader.getResource("classpath:" + gameConfig.getQuestionsFile()).getInputStream()) {
            try (InputStreamReader reader = new InputStreamReader(inStr)) {
                try (BufferedReader bufferedReader = new BufferedReader(reader)) {
                    bufferedReader.lines().forEach(line -> {
                        new PlainQuestion(database, line);
                        log.info("Created new plain question: " + line);
                    });
                }
            }
        } catch (IOException e) {
            log.error("Failed loading plain questions from file '" + gameConfig.getQuestionsFile() + "'", e);
        }
    }
}
