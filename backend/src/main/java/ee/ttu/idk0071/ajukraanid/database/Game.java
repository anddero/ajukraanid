package ee.ttu.idk0071.ajukraanid.database;

//
import ee.ttu.idk0071.ajukraanid.controller.GameRunner;
import ee.ttu.idk0071.ajukraanid.database.internal.Games;
import ee.ttu.idk0071.ajukraanid.database.sync.Entry;
import ee.ttu.idk0071.ajukraanid.random.Random;
import ee.ttu.idk0071.ajukraanid.util.StringUtilities;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public final class Game extends Entry {
    private static final int TOKEN_RAW_BYTE_COUNT = 64;
    private static final Random RANDOM = new Random();
    @RequiredArgsConstructor
    public enum State {
        LOBBY("Lobby"),
        ANSWERING("Answering"),
        GRADING("Grading"),
        RESULTS("Results"),
        INACTIVE("Inactive"),
        ERROR("Error");
        private final String text;
        private static State fromString(String state) {
            for (State s : State.values()) {
                if (s.text.equalsIgnoreCase(state)) {
                    return s;
                }
            }
            return ERROR;
        }
        @Override
        public String toString() {
            return text;
        }
    }

    // inaccessible
    private final Database database;
    private Games game;

    // accessible
    @Getter private final int gameCode;
    @Getter private final Date timestamp;
    @Getter private final String token;
    private State gameState = State.LOBBY;
    @Getter private int questionNumber = 0;

    // referenced by
    @Getter private ArrayList<Player> players = new ArrayList<>();
    @Getter private List<Question> questions = new ArrayList<>();

    // runtime
    @Setter private GameRunner runner = null;

    /**
     * From an existing database entry.
     */
    Game(Database database, Games game) {
        this.database = database;
        database.getGames().add(this);
        this.game = game;
        this.gameCode = game.getCode();
        this.timestamp = game.getTimestamp();
        this.token = game.getToken();
        this.gameState = State.fromString(game.getState());
        this.questionNumber = game.getQuestionNumber();
    }

    /**
     * Completely new.
     */
    public Game(Database database, int code) {
        this.database = database;
        database.getGames().add(this);
        timestamp = new Date();
        token = RANDOM.nextBase64UrlSafeString(TOKEN_RAW_BYTE_COUNT);
        game = new Games(code, timestamp, token); // create a transient entity
        this.gameCode = code;
        game.setState(gameState.text);
        game.setQuestionNumber(questionNumber);
        game = database.getGamesRepository().save(game); // replace with persistent entity (ends up detached?)
    }

    Games getGame() {
        return game;
    }

    public synchronized State getGameState() {
        return gameState;
    }

    public void setGameState(State gameState) {
        synchronized (this) {
            this.gameState = gameState;
        }
        game.setState(gameState.text);
        game = getDatabase().getGamesRepository().save(game);
    }

    public void setQuestionNumber(int questionNumber) {
        this.questionNumber = questionNumber;
        game.setQuestionNumber(questionNumber);
        game = getDatabase().getGamesRepository().save(game);
    }

    @Override
    protected void appendTo(StringBuilder stringBuilder, int indentSize) {
        StringUtilities.addIndent(indentSize, stringBuilder);
        stringBuilder.append(gameCode).append(" ").append(gameState.text).append(" questionNumber: ")
                .append(questionNumber).append(" token: ").append(token).append("\n");

        StringUtilities.addIndent(indentSize + 1, stringBuilder);
        stringBuilder.append("Players (").append(players.size()).append(")").append("\n");
        players.forEach(player -> player.appendTo(stringBuilder, indentSize + 2));

        StringUtilities.addIndent(indentSize + 1, stringBuilder);
        stringBuilder.append("Questions (").append(questions.size()).append(")").append("\n");
        questions.forEach(question -> question.appendTo(stringBuilder, indentSize + 2));
    }

    public Optional<GameRunner> getRunner() {
        return Optional.of(runner);
    }

    @Override
    protected Database getDatabase() {
        return database;
    }
}
