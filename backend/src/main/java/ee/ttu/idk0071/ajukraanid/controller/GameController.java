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
    private final Random random = new Random();
    private final Database database;
    private final Guard guard;

    @Autowired
    private GameController(Database database) {
        this.database = database;
        guard = new Guard(database);
    }

    /**
     * @return fetches the game state. It will always be possible to generate a unique game unless there are over
     * 9999 current games. TODO Will fix it in ETA 5 weeks
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
                .put("Action", "NewGame") // TODO CreateGame instead of NewGame
                .put("Code", gameCode).toString();
    }

    String joinGame(int gameCode, String playerName) {
        Optional<Game> game = findActiveGame(gameCode);

        if (!game.isPresent()) {
            return createErrorResponse("Did not find such game with game code: " + gameCode); // TODO Exception
        }

        if (findPlayer(game.get(), playerName).isPresent()) { // TODO Ignore case check
            return createErrorResponse("Such username is already taken.");
        }

        new Player(game.get(), playerName);
        return fetchState(gameCode);
    }

    /**
     * @param gameCode the unique code to each game
     * @return the game state, if it is in lobby then return the list of player names.
     */
    String startGame(int gameCode) {
        Optional<Game> game = findActiveGame(gameCode);

        if (!game.isPresent()) {
            return createErrorResponse("Was not able to start the game because such game was not found.");
        }

        executor.submit(new GameRunner(game.get()));
        return fetchState(gameCode);
    }

    String fetchState(int gameCode) {
        Optional<Game> game = findActiveGame(gameCode);

        if (!game.isPresent()) {
            return createErrorResponse("No game found with game code: " + gameCode);
        }

        JSONArray players = new JSONArray();
        game.get().getPlayers().forEach(player -> players.put(player.toString()));
        return new JSONObject()
                .put("Action", "FetchState")
                .put("State", game.get().getGameState()) // TODO Enum
                .put("Data", players).toString();
    }

    String submitAnswer(int gameCode, String playerName, String answer) {
        // TODO Need Question Number
        Optional<Game> game = findActiveGame(gameCode);
        if (!game.isPresent()) {
            return createErrorResponse("Did not find such game with game code: " + gameCode);
        }
        Optional<Player> player = findPlayer(game.get(), playerName);
        if (!player.isPresent()) {
            return createErrorResponse("Wrong username was given");
        }

        Question currentQuestion = getCurrentQuestion(game.get());

        boolean hasAnswered = currentQuestion.getAnswers().stream()
                .anyMatch(ans -> ans.getPlayer().getName().equals(playerName));
        if (hasAnswered) {
            return createErrorResponse("You already answered the question");
        }
        new Answer(currentQuestion, player.get(), answer);
        return createSuccessResponse("Your answer was submitted.");
    }

    String givePoints(int gameCode, String giverName, String targetName) {
        // TODO Need Question Number
        Optional<Game> optGame = findActiveGame(gameCode);

        if (!optGame.isPresent()) {
            return createErrorResponse("Game with such id was not found");
        }

        Game game = optGame.get();

        Optional<Player> optGiver = findPlayer(game, giverName);
        Optional<Player> optTarget = findPlayer(game, targetName);

        if (!optGiver.isPresent() || !optTarget.isPresent()) {
            return createErrorResponse("Wrong username was given");
        }

        Player giver = optGiver.get();
        Player target = optTarget.get();

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

    String getPoints(int gameCode) {
        JSONArray jsonArray = new JSONArray();
        Optional<Game> game = findActiveGame(gameCode);

        if (!game.isPresent()) {
            return createErrorResponse("Game with id " + gameCode + " does not exist");
        }

        for (Player player : game.get().getPlayers()) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("name", player.getName());
            jsonObject.put("points", player.getPoints());
            jsonArray.put(jsonObject);
        }

        return createFetchStateResponse("Points", jsonArray);
    }

    String getAnswers(int gameCode) {
        // TODO Need Question Number
        Optional<Game> game = findActiveGame(gameCode);

        if (!game.isPresent()) {
            return createErrorResponse("Did not find such game with game code: " + gameCode);
        }

        JSONArray questions = new JSONArray();

        for (Answer answer : getCurrentQuestion(game.get()).getAnswers()) {
            JSONObject data = new JSONObject();
            data.put("name", answer.getPlayer().getName());
            data.put("answer", answer.getText());
            questions.put(data);
        }

        return createFetchStateResponse("GetAnswers", questions);
    }

    String removePlayer(int gameCode, String playerName) {
        Optional<Game> optGame = findActiveGame(gameCode);

        if (!optGame.isPresent()) {
            return createErrorResponse("Did not find such game with game code: " + gameCode);
        }

        Optional<Player> optPlayer = findPlayer(optGame.get(), playerName);

        if (!optPlayer.isPresent()) {
            return createErrorResponse("Invalid player name: " + playerName);
        }

        optGame.get().getPlayers().remove(optPlayer.get());
        optPlayer.get().setValid(false);
        return createSuccessResponse("Player successfully removed: " + playerName);
    }

    String getQuestion(int gameCode) {
        // TODO Need Q number?
        Optional<Game> optGame = findActiveGame(gameCode);

        if (!optGame.isPresent()) {
            return createErrorResponse("Invalid game id: " + gameCode);
        }

        return createFetchStateResponse("GetQuestion", getCurrentQuestion(optGame.get()).getText());
    }

    // private methods

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
}
