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

    String fetchGameState(int gameCode, String errorData) throws JSONException {
        return new JSONObject()
                .put("Action", "FetchState")
                .put("State", "Error")
                .put("Data", errorData).toString();
    }

    String fetchGameState(int gameCode) throws JSONException {
        Optional<Game> game = findActiveGame(gameCode);
        if (game.isPresent()) {
            return new JSONObject()
                    .put("Action", "FetchState")
                    .put("State", game.get().getGameState())
                    .put("Data", game.get().getData()).toString();
        }
        return fetchGameState(gameCode, "No game found with game code: " + gameCode);
    }

    String addPlayerToGame(int gameCode, String playerName) throws JSONException {
        Optional<Game> game = findActiveGame(gameCode);
        if (game.isPresent() && !game.get().doesSuchPlayerExist(playerName)) {
            game.get().addPlayerToGame(playerName);
            return fetchGameState(gameCode);
        } else if (game.isPresent() && game.get().doesSuchPlayerExist(playerName)) {
            return fetchGameState(gameCode, "Such username is already taken.");
        }
        return fetchGameState(gameCode, "Did not find such game with game code: " + gameCode);
    }

    String startGame(int gameCode) throws JSONException {
        Optional<Game> game = findActiveGame(gameCode);
        if (game.isPresent()) {
            game.get().setGameState("Question");
            return fetchGameState(gameCode);
        }
        return fetchGameState(gameCode, "Was not able to start the game because such game was not found.");
    }

    String createNewGame() throws JSONException {
        List<Integer> usedCodes = activeGames.stream()
                .map(Game::getGameCode)
                .collect(Collectors.toList());
        Random rand = new Random();
        int randomNum = rand.nextInt((9999 - 1000) + 1) + 1000;

        while (usedCodes.contains(randomNum)) {
            randomNum = rand.nextInt((9999 - 1000) + 1) + 1000;
        }
        Game newGame = new Game();
        newGame.setGameCode(randomNum);
        activeGames.add(newGame);
        return new JSONObject()
                .put("Action", "NewGame")
                .put("Code", randomNum).toString();
    }
}
