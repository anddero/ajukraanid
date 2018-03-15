package ee.ttu.idk0071.ajukraanid.database;


import ee.ttu.idk0071.ajukraanid.database.internal.Games;
import ee.ttu.idk0071.ajukraanid.database.sync.Entry;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

public final class Game extends Entry {
    // inaccessible
    private final Database database;
    private Games game;

    // accessible
    @Getter private final int gameCode;
    @Getter private String gameState = "Lobby"; // TODO Override setter to update database.
    @Getter private int questionNumber = 0;

    // referenced by
    @Getter private ArrayList<Player> players = new ArrayList<>();
    @Getter private List<Question> questions = new ArrayList<>();

    /**
     * From an existing database entry.
     */
    Game(Database database, Games game) {
        this.database = database;
        database.getGames().add(this);
        this.game = game;
        this.gameCode = game.getCode();
        this.gameState = game.getState();
        this.questionNumber = game.getQuestionNumber();
    }

    /**
     * Completely new.
     */
    public Game(Database database, int code) {
        this.database = database;
        database.getGames().add(this);
        game = new Games(code); // create a transient entity
        this.gameCode = code;
        game.setState(gameState);
        game.setQuestionNumber(questionNumber);
        game = database.getGamesRepository().save(game); // replace with persistent entity (ends up detached?)
    }

    /**
     * Allow creation of unrelated objects temporarily.
     * @deprecated TODO Remove this constructor.
     */
    public Game(int code) {
        this.database = null;
        this.gameCode = code;
        game = new Games(code);
        game.setState(gameState);
        game.setQuestionNumber(questionNumber);
    }

    Games getGame() {
        return game;
    }

    public void setGameState(String gameState) {
        this.gameState = gameState;
        game.setState(gameState);
        game = getDatabase().getGamesRepository().save(game);
    }

    public void setQuestionNumber(int questionNumber) {
        this.questionNumber = questionNumber;
        game.setQuestionNumber(questionNumber);
        game = getDatabase().getGamesRepository().save(game);
    }

    @Override
    protected Database getDatabase() {
        return database;
    }
}
