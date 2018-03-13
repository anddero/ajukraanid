package ee.ttu.idk0071.ajukraanid.database.internal;

import lombok.Getter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Questions {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Getter private Integer id;
    @Getter private String text;

    private Questions() {
    }

    public Questions(String text) {
        this.text = text;
    }
}
