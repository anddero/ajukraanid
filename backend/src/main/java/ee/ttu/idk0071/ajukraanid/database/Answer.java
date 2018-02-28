package ee.ttu.idk0071.ajukraanid.database;


import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor
public class Answer {
    // inaccessible
    private final int id; // private key
    // accessible
    @Getter private final Player player; // foreign key
    @Getter private final String text;
    @Getter @Setter private int points = 0;
}
