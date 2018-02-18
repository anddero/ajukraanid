package ee.ttu.idk0071.ajukraanid.game;


import ee.ttu.idk0071.ajukraanid.player.Player;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;

@Data
public class Game {

    @Setter @Getter int GameCode;
    @Setter @Getter ArrayList<Player> players;
    @Setter @Getter ArrayList<Question> questions;


    public void addPlayerToGame(Player player) {
        players.add(player);
    }

}
