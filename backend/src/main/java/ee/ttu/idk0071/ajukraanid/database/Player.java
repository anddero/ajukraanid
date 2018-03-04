package ee.ttu.idk0071.ajukraanid.database;

import ee.ttu.idk0071.ajukraanid.database.internal.Players;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

public class Player {
    // inaccessible
    private final Game game;
    private final Players player;
    // accessible
    @Getter private final String name;
    @Getter @Setter private int questionNumber = 0;

    // methods

    /**
     * From existing database entry.
     */
    Player(Game game, Players player) {
        this.game = game;
        this.player = player;
        this.name = player.getPlayer_name();
        this.questionNumber = player.getQuestion_number();
    }

    /**
     * New instance.
     */
    public Player(Game game, String name) { // TODO Maybe can remove game from constructor.
        this.game = game;
        this.name = name;
        player = new Players(name);
        player.setQuestion_number(questionNumber);
        player.setGame_id(game.getGame().getGame_id());
    }

    /**
     * Temporarily enable construction of unrelated objects.
     * @deprecated TODO Remove this constructor.
     */
    public Player(String name) {
        this.game = null;
        this.player = null;
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
