package ee.ttu.idk0071.ajukraanid.controller;

import ee.ttu.idk0071.ajukraanid.database.*;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import static ee.ttu.idk0071.ajukraanid.message.Message.createErrorResponse;
import static ee.ttu.idk0071.ajukraanid.message.Message.createFetchStateResponse;
import static ee.ttu.idk0071.ajukraanid.message.Message.createSuccessResponse;

@Component
class GameController {
    private final Database database;
    private final ExecutorService executor = Executors.newCachedThreadPool();

    @Autowired
    private GameController(Database database) {
        this.database = database;
    }

    /**
     * @return fetches the game state. It will always be possible to generate a unique game unless there are over
     * 9999 current games. TODO Will fix it in ETA 5 weeks
     */
    String createNewGame() {
        List<Integer> usedCodes = database.getGames().stream()
                .map(Game::getGameCode)
                .collect(Collectors.toList());
        Random rand = new Random();
        int randomNum = rand.nextInt(8999) + 1000;
        while (usedCodes.contains(randomNum)) {
            randomNum = rand.nextInt(8999) + 1000;
        }
        Game newGame = new Game(database, randomNum);
        // TODO Temporary below
        new Question(newGame, "If a horse and a duck would have a child, what would you name it?");
        new Question(newGame, "Name something Donal Trump would say to Vladimr Putin?");
        new Question(newGame, "On a scale from squirrel to whale, how liberal is Russia?");
        new Question(newGame, "What did Johns mom tell him after he passed out drunk on the sofa?");
        newGame.getQuestions().forEach(q -> System.out.println(q.toString()));
        // TODO Temporary above
        return new JSONObject()
                .put("Action", "NewGame")
                .put("Code", randomNum).toString();
    }

    String joinGame(int gameCode, String playerName) {
        Optional<Game> game = findActiveGame(gameCode);
        if (game.isPresent() && !playerExists(gameCode, playerName)) {
            Player player = new Player(game.get(), playerName);
            return fetchState(gameCode);
        } else if (game.isPresent() && playerExists(gameCode, playerName)) {
            return createErrorResponse("Such username is already taken.");
        }
        return createErrorResponse("Did not find such game with game code: " + gameCode);
    }

    /**
     * @param gameCode the unique code to each game
     * @return the game state, if it is in lobby then return the list of player names.
     */
    String startGame(int gameCode) {
        Optional<Game> game = findActiveGame(gameCode);
        if (game.isPresent()) {
            executor.submit(new GameRunner(game.get()));
            return fetchState(gameCode);
        }
        return createErrorResponse("Was not able to start the game because such game was not found.");
    }

    String fetchState(int gameCode) {
        Optional<Game> game = findActiveGame(gameCode);
        if (game.isPresent()) {
            JSONArray players = new JSONArray();
            game.get().getPlayers().forEach(player -> players.put(player.toString()));
            return new JSONObject()
                    .put("Action", "FetchState")
                    .put("State", game.get().getGameState())
                    .put("Data", players).toString();
        }
        return createErrorResponse("No game found with game code: " + gameCode);
    }

    String submitAnswer(int gameCode, String answerer, String answer) {
        Optional<Game> game = findActiveGame(gameCode);
        if (!playerExists(gameCode, answerer)) {
            return createErrorResponse("Wrong username was given"); // TODO Move conditions checks to a Guardian class
        }
//        if (game.isPresent() && questionNumber != game.get().getQuestionNumber()) {
//            return createErrorResponse("Question number does not match he current games' state");
//        }
        if (game.isPresent()) {
            boolean hasAnswered = game.get().getQuestions().get(game.get().getQuestionNumber()).getAnswers().stream()
                    .anyMatch(answer1 -> answer1.getPlayer().getName().equals(answerer));
            if (!hasAnswered) {
                Optional<Player> player = findPlayer(game.get(), answerer);
                new Answer(game.get().getQuestions().get(game.get().getQuestionNumber()), player.get(), answer);
                return createSuccessResponse("Your answer was submitted.");
            } else return createErrorResponse("You already answered the question");
        }
        return createErrorResponse("Did not find such game with game code: " + gameCode);
    }

    String givePoints(int gameCode, String giver, String target) {
        Optional<Game> game = findActiveGame(gameCode);
        if (!playerExists(gameCode, giver) && !playerExists(gameCode, target)) {
            return createErrorResponse("Wrong username was given");
        }

        if (game.isPresent()) {
            Game gameObj = game.get();
            Question question = gameObj.getQuestions().get(game.get().getQuestionNumber());

            ArrayList<Evaluation> evaluations = question.getEvaluations();

            boolean personHasEvaluated = evaluations.stream()
                    .anyMatch(evaluation -> evaluation.getGiver().getName().equals(giver));

            Player player = findPlayer(gameObj, target).get();
            Player player1 = findPlayer(gameObj, giver).get();

            if (!personHasEvaluated) {
                new Evaluation(question, player1, player);
                return createSuccessResponse("Your points were given to " + target);
            } else return createErrorResponse("Can not give points, because you already gave points");

        }
        return createErrorResponse("Game with such id was not found");
    }

    String getTotalPlayerPointStatistics(int gameCode) {
        JSONObject json = new JSONObject();
        HashMap<String, Integer> playerAndPointsThatHeHas = new HashMap<>();
        JSONArray jsonArray = new JSONArray();
        Optional<Game> game = findActiveGame(gameCode);
        if (game.isPresent()) {
            for (Player player : game.get().getPlayers()) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("name", player.getName());
                jsonObject.put("points", player.getPoints());
                jsonArray.put(jsonObject);
            }
        }

        return createFetchStateResponse("Points", jsonArray);
    }

    String getAnswers(int gameCode) {
        Optional<Game> game = findActiveGame(gameCode);
        if (game.isPresent()) {
            JSONArray questions = new JSONArray();
            Question question = game.get().getQuestions().get(game.get().getQuestionNumber());

            for (Answer a : question.getAnswers()) {
                JSONObject data = new JSONObject();
                data.put("name", a.getPlayer().getName());
                data.put("answer", a.getText());
                questions.put(data);
            }

            return createFetchStateResponse("GetAnswers", questions);
        } return createErrorResponse("Did not find such game with game code: " + gameCode);
    }

    String removePlayerFromGame(int gameCode, String playername) {
        Optional<Game> gameOptional = findActiveGame(gameCode);
        if (gameOptional.isPresent()) {
            Optional<Player> playerOptional = findPlayer(gameOptional.get(), playername);
            playerOptional.ifPresent(player -> {
                gameOptional.get().getPlayers().remove(player);
                player.setValid(false);
            });
        } return createErrorResponse("Did not find such game with game code: " + gameCode);
    }

    String getQuestion(int code) {
        Optional<Game> gameOptional = findActiveGame(code);
        if (gameOptional.isPresent()) {
            return createFetchStateResponse("GetQuestion", gameOptional.get().getQuestions().get(gameOptional.get().getQuestionNumber()).getText());
        }
        return createErrorResponse("Unknown error");
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

    private boolean playerExists(int gameCode, String playerName) {
        return findActiveGame(gameCode)
                .filter(game -> findPlayer(game, playerName).isPresent())
                .isPresent();
    }
}
