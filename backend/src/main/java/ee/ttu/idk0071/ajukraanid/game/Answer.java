package ee.ttu.idk0071.ajukraanid.game;


import ee.ttu.idk0071.ajukraanid.player.Player;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class Answer {


    @Getter @Setter int points;
    @Getter @Setter Player player;
}
