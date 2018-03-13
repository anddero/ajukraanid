package ee.ttu.idk0071.ajukraanid.database;

import ee.ttu.idk0071.ajukraanid.database.internal.PlainQuestions;
import ee.ttu.idk0071.ajukraanid.database.sync.Entry;
import lombok.Getter;

import java.util.ArrayList;

public class PlainQuestion extends Entry {
    // inaccessible
    private final Database database;
    private PlainQuestions question;
    // accessible
    @Getter private final String text;

    /**
     * From an existing database entry.
     */
    PlainQuestion(Database database, PlainQuestions question) {
        this.database = database;
        database.getPlainQuestions().add(this);
        this.question = question;
        this.text = question.getText();
    }

    /**
     * Completely new.
     */
    public PlainQuestion(Database database, String text) {
        this.database = database;
        database.getPlainQuestions().add(this);
        this.text = text;
        question = new PlainQuestions(text);
        question = database.getPlainQuestionsRepository().save(question);
    }

    @Override
    protected Database getDatabase() {
        return database;
    }
}
