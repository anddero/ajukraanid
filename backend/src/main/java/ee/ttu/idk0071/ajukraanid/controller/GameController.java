package ee.ttu.idk0071.ajukraanid.controller;

import ee.ttu.idk0071.ajukraanid.database.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

@Component
class GameController {
    private final Database database;
    ExecutorService executor = Executors.newFixedThreadPool(10);

    @Autowired
    private GameController(Database database) {
        this.database = database;
    }

    private Optional<Player> findPlayer(Game game, String name) {
        return game.getPlayers().stream()
                .filter(player -> player.getName().equals(name))
                .findFirst();
    }

    private Optional<Game> findActiveGame(int gameCode) {
        return database.getGames().stream()
                .filter(c -> c.getGameCode() == gameCode)
                .findFirst();
    }

    String fetchErrorState(int gameCode, String state, String errorData) throws JSONException {
        return new JSONObject()
                .put("Action", "FetchState")
                .put("State", state)
                .put("Data", errorData).toString();
    }

    String fetchErrorState(int gameCode, String state, Object json) throws JSONException {
        return new JSONObject()
                .put("Action", "FetchState")
                .put("State", state)
                .put("Data", json).toString();
    }

    String fetchState(int gameCode) throws JSONException {
        Optional<Game> game = findActiveGame(gameCode);
        if (game.isPresent()) {
            JSONArray players = new JSONArray();
            game.get().getPlayers().forEach(player -> players.put(player.toString()));
            return new JSONObject()
                    .put("Action", "FetchState")
                    .put("State", game.get().getGameState())
                    .put("Data", players).toString();
        }
        return fetchErrorState(gameCode, "Error", "No game found with game code: " + gameCode);
    }

    String findGameWithGameCodeAndAddPlayerToThatGame(int gameCode, String playerName) throws JSONException {
        Optional<Game> game = findActiveGame(gameCode);
        if (game.isPresent() && !suchPlayerExistsInGame(gameCode, playerName)) {
            Player player = new Player(game.get(), playerName);
            return fetchState(gameCode);
        } else if (game.isPresent() && suchPlayerExistsInGame(gameCode, playerName)) {
            return fetchErrorState(gameCode, "Error", "Such username is already taken.");
        }
        return fetchErrorState(gameCode, "Error", "Did not find such game with game code: " + gameCode);
    }


    private boolean suchPlayerExistsInGame(int gameCode, String playerName) {
        Optional<Game> game = findActiveGame(gameCode);
        return game.map(game1 -> game1.getPlayers().stream()
                .anyMatch(player -> player.getName().equals(playerName))).orElse(false);
    }

    /**
     * @param gameCode the unique code to each game
     * @return the game state, if it is in lobby then return the list of player names.
     */
    String startGame(int gameCode) throws InterruptedException {
        Optional<Game> game = findActiveGame(gameCode);
        if (game.isPresent()) {
            executor.submit(new GameRunner(game.get()));
            return fetchState(gameCode);
        }
        return fetchErrorState(gameCode, "Error", "Was not able to start the game because such game was not found.");
    }

    /**
     * @return fetches the game state. It will always be possible to generate a unique game unless there are over
     * 9999 current games. Will fix it in ETA 5 weeks
     */
    String createNewGame() throws JSONException {
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



    /**
     * @param game       Game code unique to each game.
     * @param playerName player that gives score.
     * @param target     player that the first player gives score to.
     * @return it should return a error if someone already gave score or just fetch the game state.
     */

    /**
     * @param game Game object that is operated with, it will never be NULL.
     * @return return a list of players OR the current question.


//    /**
//     * @param game       Game object that WILL never be NULL.
//     * @param playerName string player name.
//     * @return if such a player exists or not.
//     */
//    private boolean doesSuchPlayerExist(Game game, String playerName) {
//        return game.getPlayers().stream()
//                .anyMatch(t -> t.getName().equals(playerName));
//    }



    String GivePoints(int gameCode, String giver, String target) {
        Optional<Game> game = findActiveGame(gameCode);
        if (!suchPlayerExistsInGame(gameCode, giver) && !suchPlayerExistsInGame(gameCode, target)) {
            return fetchErrorState(gameCode, "Error", "Wrong username was given");
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
                return fetchErrorState(gameCode, "Success", "Your points were given to " + target);
            } else return fetchErrorState(gameCode, "Error", "Can not give points, because you already gave points");

        }
        return fetchErrorState(gameCode, "Error", "Game with such id was not found");
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

        return fetchErrorState(gameCode, "Points", jsonArray);
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

            return fetchErrorState(gameCode, "GetAnswers", questions);
        } return fetchErrorState(gameCode, "Error", "Did not find such game with game code: " + gameCode);
    }



    String submitAnswer(int gameCode, String answerer, String answer) {
        Optional<Game> game = findActiveGame(gameCode);
        if (!suchPlayerExistsInGame(gameCode, answerer)) {
            return fetchErrorState(gameCode, "Error", "Wrong username was given"); // TODO Move conditions checks to a Guardian class
        }
//        if (game.isPresent() && questionNumber != game.get().getQuestionNumber()) {
//            return fetchErrorState(gameCode, "Error", "Question number does not match he current games' state");
//        }
        if (game.isPresent()) {
           boolean hasAnswered = game.get().getQuestions().get(game.get().getQuestionNumber()).getAnswers().stream()
                   .anyMatch(answer1 -> answer1.getPlayer().getName().equals(answerer));
           if (!hasAnswered) {
               Optional<Player> player = findPlayer(game.get(), answerer);
               new Answer(game.get().getQuestions().get(game.get().getQuestionNumber()), player.get(), answer);
               return fetchErrorState(gameCode, "Success", "Your answer was submitted.");
           } else return fetchErrorState(gameCode, "Error", "You already answered the question");
        }
        return fetchErrorState(gameCode, "Error", "Did not find such game with game code: " + gameCode);
    }

    String removePlayerFromGame(int gameCode, String playername) {
        Optional<Game> gameOptional = findActiveGame(gameCode);
        if (gameOptional.isPresent()) {
            Optional<Player> playerOptional = findPlayer(gameOptional.get(), playername);
            playerOptional.ifPresent(player -> gameOptional.get().getPlayers().remove(player));
        } return fetchErrorState(gameCode, "Error", "Did not find such game with game code: " + gameCode);
    }

    String getStatus() {
        return "Question";
    }

    String getQuestion(int code) {
        Optional<Game> gameOptional = findActiveGame(code);
        if (gameOptional.isPresent()) {
            return fetchErrorState(code, "GetQuestion", gameOptional.get().getQuestions().get(gameOptional.get().getQuestionNumber()).getText());
        }
        return fetchErrorState(code, "Error", "Unknown error");
    }
}
