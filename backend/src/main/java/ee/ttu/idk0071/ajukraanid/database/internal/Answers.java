package ee.ttu.idk0071.ajukraanid.database.internal;

import lombok.Getter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Answers {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Getter private Integer id;
    @Getter private Integer questionId;
    @Getter private Integer playerId;
    @Getter private String text;

    private Answers() {
    }

    public Answers(Integer questionId, Integer playerId, String text) {
        this.questionId = questionId;
        this.playerId = playerId;
        this.text = text;
    }
}
