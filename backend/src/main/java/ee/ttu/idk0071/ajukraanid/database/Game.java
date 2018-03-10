package ee.ttu.idk0071.ajukraanid.database;


import ee.ttu.idk0071.ajukraanid.database.internal.Games;
import ee.ttu.idk0071.ajukraanid.database.sync.Entry;
import ee.ttu.idk0071.ajukraanid.database.sync.Table;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.id.IncrementGenerator;

import java.util.ArrayList;
import java.util.HashMap;

public class Game extends Entry {
    // inaccessible
    private final Database database;
    private Games game;

    // accessible
    @Setter @Getter private ArrayList<Question> questions = new ArrayList<>();
    @Setter @Getter private int questionNumber = 0;

    @Getter private final int gameCode;
    @Getter @Setter private String gameState = "Lobby"; // TODO Override setter to update database.

    // referenced by
    @Getter private ArrayList<Player> players = new ArrayList<>();
//    @Getter private final Table<Question> questions = new Table<>();

    /**
     * From an existing database entry.
     */
    Game(Database database, Games game) {
        this.database = database;
        database.getGames().add(this);
        this.game = game;
        this.gameCode = Integer.valueOf(game.getGame_code());
        this.gameState = game.getGame_state();
    }

    /**
     * Completely new.
     */
    public Game(Database database, int code) {

        this.database = database;
        database.getGames().add(this);
        this.questions.add(new Question());
        this.questions.add(new Question());
        this.gameCode = code;
        game = new Games(Integer.toString(code));
        game.setGame_state(gameState);
        game = database.getGamesRepository().save(game); // TODO Thread?
    }

    /**
     * Allow creation of unrelated objects temporarily.
     * @deprecated TODO Remove this constructor.
     */
    public Game(int code) {
        this.database = null;
        this.gameCode = code;
        game = new Games(Integer.toString(code));
        game.setGame_state(gameState);
    }

    Games getGame() {
        return game;
    }

    @Override
    protected Database getDatabase() {
        return database;
    }
}
