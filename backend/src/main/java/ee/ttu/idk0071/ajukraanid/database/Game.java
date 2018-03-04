package ee.ttu.idk0071.ajukraanid.database;


import ee.ttu.idk0071.ajukraanid.database.internal.Games;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

public class Game {
    // inaccessible
    private final Database database;
    private final Games game;

    // accessible
    @Getter private final int gameCode;
    @Getter @Setter private String gameState = "Lobby"; // TODO Override setter to update database.

    // referenced by
    @Getter private ArrayList<Player> players = new ArrayList<>();
    @Getter private final ArrayList<Question> questions = new ArrayList<>();

    /**
     * From an existing database entry.
     */
    Game(Database database, Games game) {
        this.database = database;
        this.game = game;
        this.gameCode = Integer.valueOf(game.getGame_code());
        this.gameState = game.getGame_state();
    }

    /**
     * Completely new.
     */
    public Game(Database database, int code) {
        this.database = database;
        this.gameCode = code;
        game = new Games(Integer.toString(code));
        game.setGame_state(gameState);
        // TODO Update database.
    }

    /**
     * Temporarily enable construction of unrelated objects.
     * @deprecated TODO Remove this constructor.
     */
    public Game(int code) {
        this.database = null;
        this.game = null;
        this.gameCode = code;
    }

    Games getGame() {
        return game;
    }
}
