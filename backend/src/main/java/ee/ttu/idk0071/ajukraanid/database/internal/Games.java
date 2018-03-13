package ee.ttu.idk0071.ajukraanid.database.internal;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Games {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Getter private Integer game_id;
    @Getter private Integer game_code;
    @Getter @Setter private String game_state;
    @Getter @Setter private Integer question_number;

    private Games() {
    }

    public Games(Integer code) {
        game_code = code;
    }
}
