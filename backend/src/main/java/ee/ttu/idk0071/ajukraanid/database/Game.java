package ee.ttu.idk0071.ajukraanid.database;


import ee.ttu.idk0071.ajukraanid.database.internal.Games;
import ee.ttu.idk0071.ajukraanid.database.sync.Entry;
import ee.ttu.idk0071.ajukraanid.database.sync.Table;
import lombok.Getter;
import lombok.Setter;

public class Game implements Entry {
    // inaccessible
    @Setter private Table table;
    private final Games game;

    // accessible
    @Getter private final int gameCode;
    @Getter @Setter private String gameState = "Lobby"; // TODO Override setter to update database.

    // referenced by
    @Getter private Table<Player> players = new Table<>();
    @Getter private final Table<Question> questions = new Table<>();

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
        // TODO Update database.
    }

    Games getGame() {
        return game;
    }
}
