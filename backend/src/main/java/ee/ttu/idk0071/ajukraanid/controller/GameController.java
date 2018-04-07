package ee.ttu.idk0071.ajukraanid.controller;

import ee.ttu.idk0071.ajukraanid.database.*;
import ee.ttu.idk0071.ajukraanid.guard.Guard;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static ee.ttu.idk0071.ajukraanid.message.Message.createErrorResponse;
import static ee.ttu.idk0071.ajukraanid.message.Message.createFetchStateResponse;
import static ee.ttu.idk0071.ajukraanid.message.Message.createSuccessResponse;

@Component
class GameController {
    private final ExecutorService executor = Executors.newCachedThreadPool();
    private final Guard guard = new Guard();
    private final Random random = new Random();
    private final Database database;

    @Autowired
    private GameController(Database database) {
        this.database = database;
    }

    /**
     * @return fetches the game state. It will always be possible to generate a unique game unless there are over
     * 9999 current games. TODO Will fix it in ETA 5 weeks (check db for old games, add timestamps)
     */
    String createGame() {
        List<Integer> usedCodes = database.getGames().stream()
                .map(Game::getGameCode)
                .collect(Collectors.toList());
        int[] availableCodes = IntStream.range(1_000, 10_000)
                .filter(code -> !usedCodes.contains(code))
                .toArray();
        if (availableCodes.length == 0) {
            return createErrorResponse("Too many active games, could not create a new instance");
        }
        int randomIndex = random.nextInt(availableCodes.length);
        int gameCode = availableCodes[randomIndex];
        Game game = new Game(database, gameCode);
        // TODO Temporary below
        new Question(game, "If a horse and a duck would have a child, what would you name it?");
        new Question(game, "Name something Donal Trump would say to Vladimr Putin?");
        new Question(game, "On a scale from squirrel to whale, how liberal is Russia?");
        new Question(game, "What did Johns mom tell him after he passed out drunk on the sofa?");
        // TODO Temporary above
        return new JSONObject()
                .put("Action", "CreateGame")
                .put("Code", gameCode).toString();
    }

    String joinGame(int gameCode, String playerName) {
        Optional<Game> optionalGame = findActiveGame(gameCode);

        if (!optionalGame.isPresent()) {
            return createErrorResponse("Did not find such game with game code: " + gameCode); // TODO Exception
        }

        if (!guard.canJoinGame(optionalGame.get())) {
            return createErrorResponse("The game is not accepting any more players");
        }

        if (findPlayerIgnoreCase(optionalGame.get(), playerName).isPresent()) {
            return createErrorResponse("Such username is already taken.");
        }

        new Player(optionalGame.get(), playerName);
        return fetchState(gameCode);
    }

    /**
     * @param gameCode the unique code to each game
     * @return the game state, if it is in lobby then return the list of player names.
     */
    String startGame(int gameCode) {
        Optional<Game> optionalGame = findActiveGame(gameCode);

        if (!optionalGame.isPresent()) {
            return createErrorResponse("Was not able to start the game because such game was not found.");
        }

        if (!guard.canStartGame(optionalGame.get())) {
            return createErrorResponse("The game cannot be started at this point");
        }

        executor.submit(new GameRunner(optionalGame.get()));
        return fetchState(gameCode);
    }

    String fetchState(int gameCode) {
        Optional<Game> optionalGame = findActiveGame(gameCode);

        if (!optionalGame.isPresent()) {
            return createErrorResponse("No game found with game code: " + gameCode);
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
                        .put("name", answer.getPlayer().getName())
                        .put("answer", answer.getText())));
                data = answers;
                break;
            case RESULTS:
                JSONArray points = new JSONArray();
                game.getPlayers().forEach(player -> points.put(new JSONObject()
                        .put("name", player.getName())
                        .put("points", getPoints(game, player)))); // TODO Clean Code with JSON keys
                data = points;
                break;
            case INACTIVE:
                data = "";
                break;
            case ERROR:
                data = "The game with ID " + gameCode + " is in erroneous state";
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
            return createErrorResponse("Did not find such game with game code: " + gameCode);
        }

        if (!guard.canSubmitAnswer(optionalGame.get())) {
            return createErrorResponse("Cannot submit answer at this point");
        }

        Optional<Player> optionalPlayer = findPlayer(optionalGame.get(), playerName);
        if (!optionalPlayer.isPresent()) {
            return createErrorResponse("Wrong username was given");
        }

        Question currentQuestion = getCurrentQuestion(optionalGame.get());

        boolean hasAnswered = currentQuestion.getAnswers().stream()
                .anyMatch(ans -> ans.getPlayer().getName().equals(playerName));
        if (hasAnswered) {
            return createErrorResponse("You already answered the question");
        }
        new Answer(currentQuestion, optionalPlayer.get(), answer);
        return createSuccessResponse("Your answer was submitted.");
    }

    String givePoints(int gameCode, String giverName, String targetName) {
        // TODO Need Question Number
        Optional<Game> optionalGame = findActiveGame(gameCode);

        if (!optionalGame.isPresent()) {
            return createErrorResponse("Game with such id was not found");
        }

        Game game = optionalGame.get();

        if (!guard.canGivePoints(game)) {
            return createErrorResponse("Cannot give points right now");
        }

        Optional<Player> optionalGiver = findPlayer(game, giverName);
        Optional<Player> optionalTarget = findPlayer(game, targetName);

        if (!optionalGiver.isPresent() || !optionalTarget.isPresent()) {
            return createErrorResponse("Wrong username was given");
        }

        Player giver = optionalGiver.get();
        Player target = optionalTarget.get();

        Question currentQuestion = getCurrentQuestion(game);
        ArrayList<Evaluation> evaluations = currentQuestion.getEvaluations();

        boolean hasAlreadyGiven = evaluations.stream()
                .map(Evaluation::getGiver)
                .anyMatch(player -> player == giver);

        if (hasAlreadyGiven) {
            return createErrorResponse("Can not give points, because you already gave points");
        }

        new Evaluation(currentQuestion, giver, target);
        return createSuccessResponse("Your points were given to " + targetName);
    }

    /**
     * @deprecated Do not handle this type of requests.
     * @param gameCode The unique game code.
     * @return The response JSON message.
     */
    String getPoints(int gameCode) {
        return createErrorResponse("GetPoints request is deprecated, use FetchState instead");
    }

    /**
     * @deprecated Do not handle this type of requests.
     * @param gameCode The unique game code.
     * @return The response JSON message.
     */
    String getAnswers(int gameCode) {
        return createErrorResponse("GetAnswers request is deprecated, use FetchState instead");
    }

    String removePlayer(int gameCode, String playerName) {
        Optional<Game> optionalGame = findActiveGame(gameCode);

        if (!optionalGame.isPresent()) {
            return createErrorResponse("Did not find such game with game code: " + gameCode);
        }

        if (!guard.canRemovePlayer(optionalGame.get())) {
            return createErrorResponse("Cannot remove players from the game right now");
        }

        Optional<Player> optionalPlayer = findPlayer(optionalGame.get(), playerName);

        if (!optionalPlayer.isPresent()) {
            return createErrorResponse("Invalid player name: " + playerName);
        }

        optionalGame.get().getPlayers().remove(optionalPlayer.get());
        optionalPlayer.get().setValid(false);
        return createSuccessResponse("Player successfully removed: " + playerName);
    }

    /**
     * @deprecated Do not handle this type of requests.
     * @param gameCode The unique game code.
     * @return The response JSON message.
     */
    String getQuestion(int gameCode) {
        return createErrorResponse("GetQuestion request is deprecated, use FetchState instead");
    }

    // private methods

    private Optional<Player> findPlayerIgnoreCase(Game game, String name) {
        return game.getPlayers().stream()
                .filter(player -> player.getName().equals(name)) // TODO equalsIgnoreCase
                .findAny();
    }

    private Optional<Player> findPlayer(Game game, String name) {
        return game.getPlayers().stream()
                .filter(player -> player.getName().equals(name))
                .findAny();
    }

    private Optional<Game> findActiveGame(int gameCode) {
        return database.getGames().stream()
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
}
