package ee.ttu.idk0071.ajukraanid.database.internal;
//
import lombok.Getter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Answers {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Getter private Long id;
    @Getter private Long questionId;
    @Getter private Long playerId;
    @Getter private String text;

    private Answers() {
    }

    public Answers(Long questionId, Long playerId, String text) {
        this.questionId = questionId;
        this.playerId = playerId;
        this.text = text;
    }
}
