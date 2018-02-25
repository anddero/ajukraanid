package ee.ttu.idk0071.ajukraanid.game;


import ee.ttu.idk0071.ajukraanid.player.Player;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor
public class Answer {
    @Getter @Setter private int points = 0;
    @Getter @Setter private Player player;
}
