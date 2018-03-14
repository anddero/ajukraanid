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
    @Getter private Long id;
    @Getter private Integer code;
    @Getter @Setter private String state;
    @Getter @Setter private Integer questionNumber;

    private Games() {
    }

    public Games(Integer code) {
        this.code = code;
    }
}
