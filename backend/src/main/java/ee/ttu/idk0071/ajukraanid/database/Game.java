package ee.ttu.idk0071.ajukraanid.database;


import ee.ttu.idk0071.ajukraanid.database.internal.Games;
import ee.ttu.idk0071.ajukraanid.database.sync.Entry;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

public final class Game extends Entry {
    // inaccessible
    private final Database database;
    private Games game;

    // accessible
    @Getter private final int gameCode;
    @Getter @Setter private String gameState = "Lobby"; // TODO Override setter to update database.
    @Getter @Setter private int questionNumber = 0;

    // referenced by
    @Getter private ArrayList<Player> players = new ArrayList<>();
    @Getter private ArrayList<Question> questions = new ArrayList<>();

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
        game = new Games(code);
        this.gameCode = code;
        game.setState(gameState);
        game.setQuestionNumber(questionNumber);
        game = database.getGamesRepository().save(game);

        this.questions.add(new Question(this, "If a horse and a duck would have a child, what would you name it?")); // TODO Remove
        this.questions.add(new Question(this, "Name something Donal Trump would say to Vladimr Putin?")); // TODO Remove
        this.questions.add(new Question(this, "On a scale from squirrel to whale, how liberal is Russia?")); // TODO Remove
        this.questions.add(new Question(this, "What did Johns mom tell him after he passed out drunk on the sofa?")); // TODO Remove
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

    @Override
    protected Database getDatabase() {
        return database;
    }
}
