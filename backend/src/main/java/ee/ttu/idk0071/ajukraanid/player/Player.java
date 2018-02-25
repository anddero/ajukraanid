package ee.ttu.idk0071.ajukraanid.player;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor
public class Player { // TODO Move to database module
    @Getter private final String name;
    @Getter @Setter private int questionNumber = 0;

    @Override
    public String toString() {
        return name;
    }

}
