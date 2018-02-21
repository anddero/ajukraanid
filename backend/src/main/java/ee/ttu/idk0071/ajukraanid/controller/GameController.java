package ee.ttu.idk0071.ajukraanid.controller;

import ee.ttu.idk0071.ajukraanid.game.Game;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

class GameController {

    private ArrayList<Game> activeGames = new ArrayList<>();

    private Optional<Game> findActiveGame(int gameCode) {
        return activeGames.stream()
                .filter(c -> c.getGameCode() == gameCode)
                .findFirst();
    }

    String fetchState(int gameCode, String errorData) throws JSONException {
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
                    .put("Data", game.get().getData()).toString();
        }
        return fetchState(gameCode, "No game found with game code: " + gameCode);
    }

    String addPlayerToGame(int gameCode, String playerName) throws JSONException {
        Optional<Game> game = findActiveGame(gameCode);
        if (game.isPresent() && !game.get().doesSuchPlayerExist(playerName)) {
            game.get().addPlayerToGame(playerName);
            return fetchState(gameCode);
        } else if (game.isPresent() && game.get().doesSuchPlayerExist(playerName)) {
            return fetchState(gameCode, "Such username is already taken.");
        }
        return fetchState(gameCode, "Did not find such game with game code: " + gameCode);
    }

    String startGame(int gameCode) throws JSONException {
        Optional<Game> game = findActiveGame(gameCode);
        if (game.isPresent()) {
            game.get().setGameState("Question");
            return fetchState(gameCode);
        }
        return fetchState(gameCode, "Was not able to start the game because such game was not found.");
    }

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
        newGame.setGameCode(randomNum);
        activeGames.add(newGame);
        return new JSONObject()
                .put("Action", "NewGame")
                .put("Code", randomNum).toString();
    }

    String submitAnswer(int game, String playerName) {

        return null;
    }

    String giveScore(int game, String playerName, String target) {
        return null;
    }
}
