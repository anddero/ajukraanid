package ee.ttu.idk0071.ajukraanid.database;

import ee.ttu.idk0071.ajukraanid.database.internal.Questions;
import ee.ttu.idk0071.ajukraanid.database.sync.Entry;
import lombok.Getter;

import java.util.ArrayList;

public final class Question extends Entry {
    // inaccessible
    private final Game game;
    private Questions question;
    // accessible
    @Getter private final String text;
    // referenced by
    @Getter private final ArrayList<Answer> answers = new ArrayList<>();
    @Getter private final ArrayList<Evaluation> evaluations = new ArrayList<>();

    /**
     * From an existing database entry.
     */
    Question(Game game, Questions question) {
        this.game = game;
        game.getQuestions().add(this);
        this.question = question;
        this.text = question.getText();
    }

    /**
     * Completely new.
     */
    public Question(Game game, String text) {
        this.game = game;
        game.getQuestions().add(this);
        question = new Questions(text);
        this.text = text;
        question = getDatabase().getQuestionsRepository().save(question);
    }

    Game getGame() {
        return game;
    }

    Questions getQuestion() {
        return question;
    }

    @Override
    public String toString() {
        return text;
    }

    @Override
    protected Database getDatabase() {
        return game.getDatabase();
    }
}
