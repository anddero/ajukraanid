package ee.ttu.idk0071.ajukraanid.database;

import ee.ttu.idk0071.ajukraanid.database.internal.Players;
import ee.ttu.idk0071.ajukraanid.database.sync.Entry;
import ee.ttu.idk0071.ajukraanid.database.sync.Table;
import lombok.Getter;
import lombok.Setter;

public class Player implements Entry {
    // inaccessible
    @Setter private Table table;
    private final Players player;
    // accessible
    @Getter private final String name;
    @Getter @Setter private int questionNumber = 0;

    // methods

    /**
     * From existing database entry.
     */
    Player(Players player) {
        this.player = player;
        this.name = player.getPlayer_name();
        this.questionNumber = player.getQuestion_number();
    }

    /**
     * New instance.
     */
    public Player(String name) {
        this.name = name;
        player = new Players(name);
        player.setQuestion_number(questionNumber);
    }

    @Override
    public String toString() {
        return name;
    }
}
