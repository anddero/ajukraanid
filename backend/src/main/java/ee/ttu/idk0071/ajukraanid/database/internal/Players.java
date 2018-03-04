package ee.ttu.idk0071.ajukraanid.database.internal;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Players {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Getter private Integer player_id;
    @Getter private String player_name;
    @Getter @Setter private Integer game_id;
    @Getter @Setter private Integer question_number;

    private Players() {
    }

    public Players(String name) {
        player_name = name;
    }
}
