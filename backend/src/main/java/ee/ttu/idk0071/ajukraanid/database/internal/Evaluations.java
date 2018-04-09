package ee.ttu.idk0071.ajukraanid.database.internal;
//
import lombok.Getter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Evaluations {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Getter private Long id;
    @Getter private Long questionId;
    @Getter private Long giverId;
    @Getter private Long targetId;

    private Evaluations() {
    }

    public Evaluations(Long questionId, Long giverId, Long targetId) {
        this.questionId = questionId;
        this.giverId = giverId;
        this.targetId = targetId;
    }
}
