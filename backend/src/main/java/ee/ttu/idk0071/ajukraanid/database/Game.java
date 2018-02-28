package ee.ttu.idk0071.ajukraanid.database;


import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;

@RequiredArgsConstructor
public class Game {
    // inaccessible
    private int id; // private key
    // accessible
    @Getter private final int gameCode;
    @Getter @Setter private String gameState = "Lobby";
    // referenced by
    @Getter @Setter private ArrayList<Player> players = new ArrayList<>();
    @Getter private final ArrayList<Question> questions = new ArrayList<>();
}


