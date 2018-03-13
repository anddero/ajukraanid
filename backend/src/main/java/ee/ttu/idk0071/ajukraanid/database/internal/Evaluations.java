package ee.ttu.idk0071.ajukraanid.database.internal;

import lombok.Getter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Evaluations { // TODO All id-s to Long
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Getter private Integer id;
    @Getter private Integer questionId;
    @Getter private Integer giverId;
    @Getter private Integer targetId;

    private Evaluations() {
    }

    public Evaluations(Integer questionId, Integer giverId, Integer targetId) {
        this.questionId = questionId;
        this.giverId = giverId;
        this.targetId = targetId;
    }
}
