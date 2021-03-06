package ee.ttu.idk0071.ajukraanid.controller;

import ee.ttu.idk0071.ajukraanid.config.GameConfig;
import ee.ttu.idk0071.ajukraanid.database.*;
import ee.ttu.idk0071.ajukraanid.guard.Guard;
import ee.ttu.idk0071.ajukraanid.guard.GuardException;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
import static ee.ttu.idk0071.ajukraanid.message.Message.createSuccessResponseJson;

@Component
public class GameController {
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
                .put("Code", gameCode)
                .put("Token", game.getToken())
                .toString();
    }

    String joinGame(int gameCode, String playerName) {
        Optional<Game> optionalGame = findGame(gameCode);

        if (!optionalGame.isPresent()) {
            throw new GuardException("Did not find such game with game code: " + gameCode);
        }

        guard.checkJoinGame(optionalGame.get());

        if (findPlayerIgnoreCase(optionalGame.get(), playerName).isPresent()) {
            throw new GuardException("Such username is already taken.");
        }

        Player player = new Player(optionalGame.get(), playerName);
        return createSuccessResponseJson("You have successfully joined the game")
                .put("Token", player.getToken())
                .toString();
    }

    /**
     * @param gameCode the unique code to each game
     * @return the game state, if it is in lobby then return the list of player names.
     */
    String startGame(int gameCode, String token) {
        Optional<Game> optionalGame = findGame(gameCode);

        if (!optionalGame.isPresent()) {
            throw new GuardException("Was not able to start the game because such game was not found.");
        }

        if (!optionalGame.get().getToken().equals(token)) {
            throw new GuardException("Invalid game token");
        }

        guard.checkStartGame(optionalGame.get());

        GameRunner gameRunner = new GameRunner(gameConfig, optionalGame.get());
        optionalGame.get().setRunner(gameRunner);
        executor.submit(gameRunner);

        return createSuccessResponse("Game successfully started");
    }

    String fetchState(int gameCode, String playerOrGameToken) {
        Optional<Game> optionalGame = findGame(gameCode);

        if (!optionalGame.isPresent()) {
            throw new GuardException("No game found with game code: " + gameCode);
        }

        Game game = optionalGame.get();
        Game.State gameState = optionalGame.get().getGameState();

        if (!validToken(game, playerOrGameToken)) {
            throw new GuardException("Invalid game or player token");
        }

        Object data;
        switch (gameState) {
            case LOBBY:
                JSONArray players = new JSONArray();
                game.getPlayers().forEach(player -> players.put(player.toString()));
                data = players;
                break;
            case ANSWERING:
                data = new JSONObject()
                        .put("TimeLeft", getTimeLeft(game))
                        .put("Question", getCurrentQuestion(game).getText());
                if (hasEveryoneAnswered(game)) {
                    Optional<GameRunner> runner = game.getRunner();
                    if (runner.isPresent()) {
                        synchronized (runner.get()) {
                            runner.get().notify(); // skip to grading if everyone has answered
                        }
                    }
                }
                break;
            case GRADING:
                JSONArray answers = new JSONArray();
                getCurrentQuestion(game).getAnswers().forEach(answer -> answers.put(new JSONObject()
                        .put("Name", answer.getPlayer().getName())
                        .put("Answer", answer.getText())));
                data = new JSONObject()
                        .put("TimeLeft", getTimeLeft(game))
                        .put("Answers", answers);
                if (hasEveryoneGraded(game)) {
                    Optional<GameRunner> runner = game.getRunner();
                    if (runner.isPresent()) {
                        synchronized (runner.get()) {
                            runner.get().notify(); // skip to points if everyone has graded
                        }
                    }
                }
                break;
            case RESULTS:
                JSONArray points = new JSONArray();
                game.getPlayers().forEach(player -> points.put(new JSONObject()
                        .put("Name", player.getName())
                        .put("Points", getPoints(game, player))));
                data = new JSONObject()
                        .put("TimeLeft", getTimeLeft(game))
                        .put("Scores", points);
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

    String submitAnswer(int gameCode, String playerName, String answer, String token) {
        // TODO Need Question Number
        Optional<Game> optionalGame = findGame(gameCode);
        if (!optionalGame.isPresent()) {
            throw new GuardException("Did not find such game with game code: " + gameCode);
        }

        guard.checkSubmitAnswer(optionalGame.get());

        Optional<Player> optionalPlayer = findPlayer(optionalGame.get(), playerName);
        if (!optionalPlayer.isPresent()) {
            throw new GuardException("Wrong username was given");
        }

        if (!optionalPlayer.get().getToken().equals(token)) {
            throw new GuardException("Invalid player token");
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

    String givePoints(int gameCode, String giverName, String targetName, String token) {
        // TODO Need Question Number
        Optional<Game> optionalGame = findGame(gameCode);

        if (!optionalGame.isPresent()) {
            throw new GuardException("Game with such id was not found");
        }

        Game game = optionalGame.get();

        guard.checkGivePoints(game);

        Optional<Player> optionalGiver = findPlayer(game, giverName);
        Optional<Player> optionalTarget = findPlayer(game, targetName);

        if (!optionalGiver.isPresent()) {
            throw new GuardException("Invalid evaluation author username given");
        }

        if (!optionalTarget.isPresent()) {
            throw new GuardException("Invalid evaluation target username given");
        }

        Player giver = optionalGiver.get();
        Player target = optionalTarget.get();

        if (!giver.getToken().equals(token)) {
            throw new GuardException("Invalid player token");
        }

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

    String removePlayer(int gameCode, String playerName, String token) {
        Optional<Game> optionalGame = findGame(gameCode);

        if (!optionalGame.isPresent()) {
            throw new GuardException("Did not find such game with game code: " + gameCode);
        }

        if (!optionalGame.get().getToken().equals(token)) {
            throw new GuardException("Invalid game token");
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

    String getPlayers(int gameCode, String gameOrPlayerToken) {
        Optional<Game> optionalGame = findGame(gameCode);

        if (!optionalGame.isPresent()) {
            throw new GuardException("Did not find such game with game code: " + gameCode);
        }

        if (!validToken(optionalGame.get(), gameOrPlayerToken)) {
            throw new GuardException("Invalid game or player token");
        }

        guard.checkGetPlayers(optionalGame.get());

        JSONArray players = new JSONArray();
        optionalGame.get().getPlayers().forEach(player -> players.put(player.toString()));
        return new JSONObject()
                .put("Action", "GetPlayers")
                .put("Players", players)
                .toString();
    }

    String getPlainQuestions() {
        JSONArray questions = new JSONArray();
        database.getPlainQuestions().forEach(question -> questions.put(new JSONObject()
                    .put("Id", question.getQuestion().getId())
                    .put("Text", question.getText())));
        return new JSONObject()
                .put("Action", "GetQuestions")
                .put("Questions", questions)
                .toString();
    }

    String addPlainQuestion(String text) {
        if (addUniqueQuestion(text)) {
            return createSuccessResponse("Question successfully added");
        }
        throw new GuardException("Cannot add the same question multiple times");
    }

    String deletePlainQuestion(long uniqueId) {
        if (deleteQuestion(uniqueId)) {
            return createSuccessResponse("Question successfully deleted");
        }
        throw new GuardException("Invalid question ID provided, a question with the ID " + uniqueId + " does not " +
                "exist");
    }

    String updatePlainQuestion(long uniqueId, String text) {
        Optional<PlainQuestion> question = getPlainQuesion(uniqueId);
        if (!question.isPresent()) {
            throw new GuardException("Question with id " + uniqueId + " does not exist");
        }
        if (!canAddUniqueQuestion(text)) {
            throw new GuardException("Cannot change to the given text because a question like this already exists");
        }
        question.get().setText(text);
        return createSuccessResponse("Successfully updated question");
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

    /**
     * If there is an active game with the given code, it will be returned.
     * If there is not an active game with the given code, but inactive games exist, any of them will be returned.
     * Otherwise an empty optional is returned.
     * @param gameCode The code of the game.
     * @return Optional Game object with the exact same code given as argument to the function.
     */
    private Optional<Game> findGame(int gameCode) {
        Optional<Game> optionalGame = database.getGames().stream()
                .filter(game -> game.getGameState() != Game.State.INACTIVE)
                .filter(game -> game.getGameCode() == gameCode)
                .findAny();

        if (optionalGame.isPresent()) {
            return optionalGame;
        }

        return database.getGames().stream()
                .filter(game -> game.getGameCode() == gameCode)
                .findAny();
    }

    private Question getCurrentQuestion(Game game) {
        return game.getQuestions().get(game.getQuestionNumber());
    }

    private int getPoints(Game game, Player player) {
        return (int) game.getQuestions().stream()
                .flatMap(q -> q.getEvaluations().stream())
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
                    bufferedReader.lines().forEach(this::addUniqueQuestion);
                }
            }
        } catch (IOException e) {
            log.error("Failed loading plain questions from file '" + gameConfig.getQuestionsFile() + "'", e);
        }
    }

    private int getTimeLeft(Game game) {
        Optional<GameRunner> runner = game.getRunner();

        if (!runner.isPresent()) {
            log.error("GameRunner for active Game instance is not present, game id: {}", game.getGameCode());
            return 0;
        }

        return runner.get().getTimeLeftSeconds();
    }

    private boolean addUniqueQuestion(String text) {
        if (!canAddUniqueQuestion(text)) {
            log.debug("Could not add an already existing question: " + text);
            return false;
        }
        new PlainQuestion(database, text);
        log.info("Created new plain question: " + text);
        return true;
    }

    private boolean canAddUniqueQuestion(String text) {
        Optional<PlainQuestion> question = database.getPlainQuestions().stream()
                .filter(q -> q.getText().equalsIgnoreCase(text))
                .findAny();
        return !question.isPresent();
    }

    private boolean deleteQuestion(long uniqueId) {
        Optional<PlainQuestion> question = database.getPlainQuestions().stream()
                .filter(q -> q.getQuestion().getId() == uniqueId)
                .findAny();
        question.ifPresent(q -> {
            database.getPlainQuestions().remove(q);
            database.getPlainQuestionsRepository().deleteById(uniqueId);
        });
        return question.isPresent();
    }

    private Optional<PlainQuestion> getPlainQuesion(long uniqueId) {
        return database.getPlainQuestions().stream()
                .filter(q -> q.getQuestion().getId() == uniqueId)
                .findAny();
    }

    private boolean validPlayerToken(Game game, String token) {
        for (Player player : game.getPlayers()) {
            if (player.getToken().equals(token)) {
                return true;
            }
        }
        return false;
    }

    private boolean validToken(Game game, String token) {
        return game.getToken().equals(token) || validPlayerToken(game, token);
    }

    private boolean hasEveryoneAnswered(Game game) {
        Set<Player> answerers = getCurrentQuestion(game).getAnswers().stream()
                .map(Answer::getPlayer)
                .collect(Collectors.toSet());
        Set<Player> players = new HashSet<>(game.getPlayers());
        if (answerers.size() != players.size()) {
            return false;
        }
        for (Player answerer : answerers) {
            if (!players.contains(answerer)) {
                return false;
            }
        }
        return true;
    }

    private boolean canGrade(Player player, Question question) {
        return question.getAnswers().stream()
                .anyMatch(a -> a.getPlayer() != player);
    }

    private boolean hasEveryoneGraded(Game game) {
        Question question = getCurrentQuestion(game);
        Set<Player> graders = question.getEvaluations().stream()
                .map(Evaluation::getGiver)
                .collect(Collectors.toSet());
        Set<Player> players = game.getPlayers().stream()
                .filter(player -> canGrade(player, question))
                .collect(Collectors.toSet());
        if (graders.size() != players.size()) {
            return false;
        }
        for (Player grader : graders) {
            if (!players.contains(grader)) {
                return false;
            }
        }
        return true;
    }
}
