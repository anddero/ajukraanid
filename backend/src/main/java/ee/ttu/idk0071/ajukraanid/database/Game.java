package ee.ttu.idk0071.ajukraanid.database;


import ee.ttu.idk0071.ajukraanid.database.internal.Games;
import ee.ttu.idk0071.ajukraanid.database.sync.Entry;
import ee.ttu.idk0071.ajukraanid.util.StringUtilities;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public final class Game extends Entry {
    @RequiredArgsConstructor
    public enum State {
        LOBBY("Lobby"),
        ANSWERING("question"),
        GRADING("chooseBestAnswer"),
        RESULTS("awarding"),
        INACTIVE("ended"),
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
    @Getter private State gameState = State.LOBBY;
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
        this.timestamp = game.getTimestamp();
        this.gameState = State.fromString(game.getState());
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
        timestamp = new Date();
        game.setState(gameState.text);
        game.setQuestionNumber(questionNumber);
        game = database.getGamesRepository().save(game); // replace with persistent entity (ends up detached?)
    }

    Games getGame() {
        return game;
    }

    public void setGameState(State gameState) {
        this.gameState = gameState;
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
                .append(questionNumber).append("\n");

        StringUtilities.addIndent(indentSize + 1, stringBuilder);
        stringBuilder.append("Players (").append(players.size()).append(")").append("\n");
        players.forEach(player -> player.appendTo(stringBuilder, indentSize + 2));

        StringUtilities.addIndent(indentSize + 1, stringBuilder);
        stringBuilder.append("Questions (").append(questions.size()).append(")").append("\n");
        questions.forEach(question -> question.appendTo(stringBuilder, indentSize + 2));
    }

    @Override
    protected Database getDatabase() {
        return database;
    }
}
