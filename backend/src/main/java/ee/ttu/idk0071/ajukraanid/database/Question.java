package ee.ttu.idk0071.ajukraanid.database;

import ee.ttu.idk0071.ajukraanid.database.internal.Questions;
import ee.ttu.idk0071.ajukraanid.database.sync.Entry;
import ee.ttu.idk0071.ajukraanid.database.sync.Table;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;

public class Question extends Entry {
    // inaccessible
    private final Database database;
    private Questions question;
    // accessible
    @Getter private final String text;
    // referenced by
    @Getter private final ArrayList<Answer> answers = new ArrayList<>();
    @Getter private final ArrayList<Evaluation> evaluations = new ArrayList<>();

    /**
     * From an existing database entry.
     */
    Question(Database database, Questions question) {
        this.database = database;
        database.getQuestions().add(this);
        this.question = question;
        this.text = question.getText();
    }

    /**
     * Completely new.
     */
    public Question(Database database, String text) {
        this.database = database;
        database.getQuestions().add(this);
        this.text = text;
        question = new Questions(text);
        question = database.getQuestionsRepository().save(question);
    }

    @Override
    protected Database getDatabase() {
        throw new UnsupportedOperationException("Partially implemented class definition");
    }
}
