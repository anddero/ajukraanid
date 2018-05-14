package ee.ttu.idk0071.ajukraanid.database.internal;
//
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
    @Getter private Long id;
    @Getter private Long gameId;
    @Getter private String name;
    @Getter private String token;
    @Getter @Setter private boolean valid;

    private Players() {
    }

    public Players(Long gameId, String name, String token, boolean valid) {
        this.gameId = gameId;
        this.name = name;
        this.token = token;
        this.valid = valid;
    }
}
