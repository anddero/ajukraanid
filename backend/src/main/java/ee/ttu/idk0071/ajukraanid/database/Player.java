package ee.ttu.idk0071.ajukraanid.database;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor
public class Player { // TODO Move to database module
    private int id;
    @Getter private final String name;
    @Getter @Setter private int questionNumber = 0;

    @Override
    public String toString() {
        return name;
    }

}
