package ee.ttu.idk0071.ajukraanid.controller;

import ee.ttu.idk0071.ajukraanid.database.Database;
import ee.ttu.idk0071.ajukraanid.database.Game;
import ee.ttu.idk0071.ajukraanid.database.Player;
import ee.ttu.idk0071.ajukraanid.database.internal.Players;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
class GameController {
    private final Database database; // TODO Use this instead of activeGames.

    private ArrayList<Game> activeGames = new ArrayList<>(); // TODO Use database and remove this list.

    @Autowired
    private GameController(Database database) {
        this.database = database;
    }

    private Optional<Game> findActiveGame(int gameCode) {
        return activeGames.stream()
                .filter(c -> c.getGameCode() == gameCode)
                .findFirst();
    }

    String fetchErrorState(int gameCode, String errorData) throws JSONException {
        return new JSONObject()
                .put("Action", "FetchState")
                .put("State", "Error")
                .put("Data", errorData).toString();
    }

    String fetchState(int gameCode) throws JSONException {
        Optional<Game> game = findActiveGame(gameCode);
        if (game.isPresent()) {
            return new JSONObject()
                    .put("Action", "FetchState")
                    .put("State", game.get().getGameState())
                    .put("Data", getData(game.get())).toString();
        }
        return fetchErrorState(gameCode, "No game found with game code: " + gameCode);
    }

    String findGameWithGameCodeAndAddPlayerToThatGame(int gameCode, String playerName) throws JSONException {
        Optional<Game> game = findActiveGame(gameCode);
        if (game.isPresent() && !doesSuchPlayerExist(game.get(), playerName)) {
            game.get().getPlayers().add(new Player(playerName));
            return fetchState(gameCode);
        } else if (game.isPresent() && doesSuchPlayerExist(game.get(), playerName)) {
            return fetchErrorState(gameCode, "Such username is already taken.");
        }
        return fetchErrorState(gameCode, "Did not find such game with game code: " + gameCode);
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
        return fetchErrorState(gameCode, "Was not able to start the game because such game was not found.");
    }

    /**
     * @return fetches the game state. It will always be possible to generate a unique game unless there are over
     * 9999 current games. Will fix it in ETA 5 weeks
     * @throws JSONException cos i build json here and it generates JSONException.
     */
    String createNewGame() throws JSONException {
        List<Integer> usedCodes = activeGames.stream()
                .map(Game::getGameCode)
                .collect(Collectors.toList());
        Random rand = new Random();
        int randomNum = rand.nextInt(8999) + 1000;

        while (usedCodes.contains(randomNum)) {
            randomNum = rand.nextInt(8999) + 1000;
        }
        Game newGame = new Game(randomNum);
        activeGames.add(newGame);
        return new JSONObject()
                .put("Action", "NewGame")
                .put("Code", randomNum).toString();
    }

    /**
     * @param game gamecode unique to each game.
     * @param playerName player that submits the asnwer
     * @return IT IS NOT IMPLEMENTED. ETA 1.5 WEEKS.
     */
    String submitAnswer(int game, String playerName) {
        return null;
    }

    /**
     * @param game Game code unique to each game.
     * @param playerName  player that gives score.
     * @param target player that the first player gives score to.
     * @return it should return a error if someone already gave score or just fetch the game state.
     */
    String giveScore(int game, String playerName, String target) {
        return null;
    }

    /**
     * @param game Game object that is operated with, it will never be NULL.
     * @return return a list of players OR the current question.
     */
    private String getData(Game game) {
        if (Objects.equals(game.getGameState(), "Lobby")) {
            return game.getPlayers().toString();
        } else return "{“Number”:”2”,“Question”:“Que pikk question?”}";
    }

    /**
     * @param game Game object that WILL never be NULL.
     * @param playerName string player name.
     * @return if such a player exists or not.
     */
    private boolean doesSuchPlayerExist(Game game, String playerName) {
        return game.getPlayers().stream()
                .anyMatch(t -> t.getName().equals(playerName));
    }


}
