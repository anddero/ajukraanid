package ee.ttu.idk0071.ajukraanid.database.internal;
//
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity
public class Games {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Getter private Long id;
    @Getter private Integer code;
    @Temporal(TemporalType.TIMESTAMP)
    @Getter private Date timestamp;
    @Getter @Setter private String state;
    @Getter @Setter private Integer questionNumber;

    private Games() {
    }

    public Games(Integer code, Date timestamp) {
        this.code = code;
        this.timestamp = timestamp;
    }
}
