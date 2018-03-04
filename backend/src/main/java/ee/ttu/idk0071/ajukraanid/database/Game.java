package ee.ttu.idk0071.ajukraanid.database;


import ee.ttu.idk0071.ajukraanid.database.internal.Games;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

public class Game {
    // inaccessible
    private final Games game;

    // accessible
    @Getter private final int gameCode;
    @Getter @Setter private String gameState = "Lobby";

    // referenced by
    @Getter private ArrayList<Player> players = new ArrayList<>();
    @Getter private final ArrayList<Question> questions = new ArrayList<>();

    /**
     * From an existing database entry.
     */
    Game(Games game) {
        this.game = game;
        this.gameCode = Integer.valueOf(game.getGame_code());
        this.gameState = game.getGame_state();
    }

    /**
     * Completely new.
     */
    public Game(int code) {
        this.gameCode = code;
        game = new Games(Integer.toString(code));
        game.setGame_state(gameState);
    }

    @Override
    public String toString() {
        return "Game: " + gameCode + ", " + gameState;
    }
}
