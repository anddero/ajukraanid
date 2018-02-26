package ee.ttu.idk0071.ajukraanid.database;


import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Objects;

@RequiredArgsConstructor
public class Game {
    // inaccessible
    private int id;
    // accessible
    @Getter private final int gameCode;
    @Getter @Setter private String gameState = "Lobby";
    // referenced by
    @Getter private final ArrayList<Player> players = new ArrayList<>();
    @Getter private final ArrayList<Question> questions = new ArrayList<>();

    public String getData() { // TODO Move to logic module
        if (Objects.equals(gameState, "Lobby")) {
            return players.toString();
        } else return "{“Number”:”2”,“Question”:“Que pikk question?”}";
    }

    public boolean doesSuchPlayerExist(String playerName) { // TODO Move to logic module
        return players.stream()
                .anyMatch(t -> t.getName().equals(playerName));
    }

    public boolean addPlayerToGame(String playerName) { // TODO Move to logic module
        if (doesSuchPlayerExist(playerName)) {
            return false;
        }
        Player player = new Player(playerName);
        players.add(player);
        return true;
    }
}
