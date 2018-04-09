package ee.ttu.idk0071.ajukraanid.database;
//
import ee.ttu.idk0071.ajukraanid.database.internal.PlainQuestions;
import ee.ttu.idk0071.ajukraanid.database.sync.Entry;
import ee.ttu.idk0071.ajukraanid.util.StringUtilities;
import lombok.Getter;

import java.util.ArrayList;

public final class PlainQuestion extends Entry {
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
        question = new PlainQuestions(text);
        this.text = text;
        question = database.getPlainQuestionsRepository().save(question);
    }

    @Override
    public void appendTo(StringBuilder stringBuilder, int indentSize) {
        StringUtilities.addIndent(indentSize, stringBuilder);
        stringBuilder.append(text).append("\n");
    }

    @Override
    protected Database getDatabase() {
        return database;
    }
}
