package ee.ttu.idk0071.ajukraanid.database;


import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor
public class Answer {
    @Getter @Setter private int points = 0;
    @Getter @Setter private Player player;
}
