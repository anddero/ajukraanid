package ee.ttu.idk0071.ajukraanid.database;

import lombok.Getter;
import lombok.Setter;

public class Evaluation {

    // inaccessible
    // accessible
    @Getter
    private final Player giver; // foreign key
    @Getter
    private final Player target;
    @Getter @Setter
    private Question question;

    public Evaluation(Player giver, Player target, Question question) {
        this.giver = giver;
        this.target = target;
        this.question = question;
        question.getEvaluations().add(this);
        target.setPoints(target.getPoints() + 100); // temp stuff
    }
}
