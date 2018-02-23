package ee.ttu.idk0071.ajukraanid.game;


import ee.ttu.idk0071.ajukraanid.player.Player;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Objects;

@Data
public class Game {

    @Setter @Getter int gameCode;

    @Setter @Getter ArrayList<Player> players = new ArrayList<>();

    @Setter @Getter ArrayList<Question> questions = new ArrayList<>();

    @Getter @Setter String gameState = "Lobby";

    public Game(int gameCode) {
        this.gameCode = gameCode;

    }

    public String getData() {
        if (Objects.equals(gameState, "Lobby")) {
            return players.toString();
        } else return "{“Number”:”2”,“Question”:“Que pikk question?”}";
    }

    public boolean doesSuchPlayerExist(String playerName) {
        return players.stream()
                .anyMatch(t -> t.getName().equals(playerName));
    }

    public boolean addPlayerToGame(String playerName) {
        if (doesSuchPlayerExist(playerName)) {
            return false;
        }
        Player player = new Player();
        player.setName(playerName);
        players.add(player);
        return true;
    }

}
