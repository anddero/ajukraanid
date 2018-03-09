package ee.ttu.idk0071.ajukraanid.controller;

import ee.ttu.idk0071.ajukraanid.database.Database;
import ee.ttu.idk0071.ajukraanid.database.Game;
import ee.ttu.idk0071.ajukraanid.database.Player;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
class GameController {
    private final Database database;

    @Autowired
    private GameController(Database database) {
        this.database = database;
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


    String fetchState(int gameCode) throws JSONException {
        Optional<Game> game = findActiveGame(gameCode);
        if (game.isPresent()) {
            ArrayList<String> s = new ArrayList<>();
            for (Player player : game.get().getPlayers()) {
                s.add(new JSONObject()
                        .put("name", player.getName()).toString());
            }

            return new JSONObject()
                    .put("Action", "FetchState")
                    .put("State", game.get().getGameState())
                    .put("Data", s.toString()).toString();
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
    String startGame(int gameCode) {
        Optional<Game> game = findActiveGame(gameCode);
        if (game.isPresent()) {
            game.get().setGameState("Question");
            return fetchState(gameCode);
        }
        return fetchErrorState(gameCode, "Error", "Was not able to start the game because such game was not found.");
    }

    /**
     * @return fetches the game state. It will always be possible to generate a unique game unless there are over
     * 9999 current games. Will fix it in ETA 5 weeks
     * @throws JSONException cos i build json here and it generates JSONException.
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
        return new JSONObject()
                .put("Action", "NewGame")
                .put("Code", randomNum).toString();
    }

    /**
     * @param game       gamecode unique to each game.
     * @param playerName player that submits the asnwer
     * @return IT IS NOT IMPLEMENTED. ETA 1.5 WEEKS.
     */
    String submitAnswer(int game, String playerName) {
        return null;
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
     */
    private String getData(Game game) {
        if (Objects.equals(game.getGameState(), "Lobby")) {
            return game.getPlayers().toString();
        } else return "{“Number”:”2”,“Question”:“Que pikk question?”}";
    }

//    /**
//     * @param game       Game object that WILL never be NULL.
//     * @param playerName string player name.
//     * @return if such a player exists or not.
//     */
//    private boolean doesSuchPlayerExist(Game game, String playerName) {
//        return game.getPlayers().stream()
//                .anyMatch(t -> t.getName().equals(playerName));
//    }

    public String GivePoints(int gameCode,int questionNumber, String giver, String target) {
        Optional<Game> game = findActiveGame(gameCode);

        if (!suchPlayerExistsInGame(gameCode, giver) && !suchPlayerExistsInGame(gameCode, target)) {
            return fetchErrorState(gameCode, "Error", "Wrong username was given");
        } if (game.isPresent() && questionNumber != game.get().getQuestionNumber()) {
            return fetchErrorState(gameCode, "Error", "Question number does not match he current games' state");
        }
        if (game.isPresent()) {
            Game gameObj = game.get();
            if (!(gameObj.getQuestionEvaluation().get(game.get().getQuestionNumber())).containsKey(giver)) {
                HashMap<String, Integer> targetHashmap = new HashMap<>();
                targetHashmap.put(target, 100);
                game.get().getQuestionEvaluation().get(game.get().getQuestionNumber()).put(giver, targetHashmap);
                return fetchErrorState(gameCode, "Success", "Your points were given to " + target);
            } else fetchErrorState(gameCode, "Error", "Game with such ");
        }
        return fetchErrorState(gameCode, "Error", "Can not give points, beacause you allready gave points");
    }



    public String getTotalPlayerPointStatistics(int gameCode) {
        JSONObject json = new JSONObject();
        HashMap<String, Integer> playerAndPointsThatHeHas = new HashMap<>();

        Optional<Game> game = findActiveGame(gameCode);
        if (game.isPresent()) {
            for (int i = 0; i < game.get().getQuestionEvaluation().size(); i++) {
                HashMap<String, HashMap<String, Integer>> temp = game.get().getQuestionEvaluation().get(i);
                for (HashMap<String, Integer> o : temp.values()) {
                    for (String name : o.keySet()) {
                        if (playerAndPointsThatHeHas.containsKey(name)) {
                            playerAndPointsThatHeHas.replace(name, playerAndPointsThatHeHas.get(name) + 100);
                        } else playerAndPointsThatHeHas.put(name, 100);
                    }
                }
            }
            for (Player player : game.get().getPlayers()) {
                String name = player.getName();
                if (!playerAndPointsThatHeHas.containsKey(name)) {
                    playerAndPointsThatHeHas.put(name, 0);
                }
            }
        }
        return fetchErrorState(gameCode, "Points", playerAndPointsThatHeHas.toString());
    }


}
