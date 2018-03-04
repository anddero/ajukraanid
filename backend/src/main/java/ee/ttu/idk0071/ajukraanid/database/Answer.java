package ee.ttu.idk0071.ajukraanid.database;


import ee.ttu.idk0071.ajukraanid.database.sync.Entry;
import lombok.Getter;
import lombok.Setter;

public class Answer extends Entry {
    // inaccessible
    private final int id; // private key
    // accessible
    @Getter private final Player player; // foreign key
    @Getter private final String text;
    @Getter @Setter private int points = 0;

    public Answer(int id, Player player, String text) {
        this.id = id;
        this.player = player;
        this.text = text;
    }

    @Override
    protected Database getDatabase() {
        throw new UnsupportedOperationException("Partially implemented class definition");
    }
}
