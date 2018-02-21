package ee.ttu.idk0071.ajukraanid.player;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

@Data
public class Player {

    @Setter @Getter int questionNumber = 0;
    @Setter @Getter String name;

    @Override
    public String toString() {
        return name;
    }

}
