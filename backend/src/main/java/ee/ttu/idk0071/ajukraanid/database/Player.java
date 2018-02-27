package ee.ttu.idk0071.ajukraanid.database;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor
public class Player {
    // accessible
    @Getter
    private final String name;
    // inaccessible
    private int id;
    @Getter
    @Setter
    private int questionNumber = 0;
    @Override
    public String toString() {
        return name;
    }

}
