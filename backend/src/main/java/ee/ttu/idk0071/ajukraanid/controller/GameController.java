package ee.ttu.idk0071.ajukraanid.controller;

import ee.ttu.idk0071.ajukraanid.game.Game;
import ee.ttu.idk0071.ajukraanid.player.Player;

import java.util.ArrayList;
import java.util.Optional;

public class GameController {

    private ArrayList<Game> activeGames = new ArrayList<>();

    public boolean addGame(int gameCode) {
        System.out.println(activeGames);
        Optional<Game> game = activeGames.stream()
                .filter(c -> c.getGameCode() == gameCode)
                .findFirst();
        if (game.isPresent()) {
            System.out.println("A game with that game code already exists.");
            return false;
        }
        Game tempGame = new Game();
        tempGame.setGameCode(gameCode);
        activeGames.add(tempGame);
        return true;
    }

    public void addPlayerToGame(int gameCode, String playerName) {
        Optional<Game> game = activeGames.stream()
                .filter(c -> c.getGameCode() == gameCode)
                .findFirst();
        Player player = new Player();
        player.setName(playerName);
        game.ifPresent(game1 -> game1.addPlayerToGame(player));
    }

    public void startGame(int gameCode) {
        Optional<Game> game = activeGames.stream()
                .filter(c -> c.getGameCode() == gameCode)
                .findFirst();
        //  game.ifPresent(game1 -> game1.startGame());
    }
}
