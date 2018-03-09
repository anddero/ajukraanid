package ee.ttu.idk0071.ajukraanid.database;

import ee.ttu.idk0071.ajukraanid.database.internal.Players;
import ee.ttu.idk0071.ajukraanid.database.sync.Entry;
import ee.ttu.idk0071.ajukraanid.database.sync.Table;
import lombok.Getter;
import lombok.Setter;

public class Player extends Entry {
    // inaccessible
    private final Game game;
    private Players player;
    // accessible

    @Getter private final String name;
    @Getter @Setter private int questionNumber = 0;

    // methods

    /**
     * From existing database entry.
     */
    Player(Game game, Players player) {
        this.game = game;
        game.getPlayers().add(this);
        this.player = player;
        this.name = player.getPlayer_name();
        this.questionNumber = player.getQuestion_number();
    }

    /**
     * New instance.
     */
    public Player(Game game, String name) {
        this.game = game;
        game.getPlayers().add(this);
        this.name = name;
        player = new Players(name);
        player.setGame_id(game.getGame().getGame_id());
        player.setQuestion_number(questionNumber);
        player = game.getDatabase().getPlayersRepository().save(player); // TODO Thread?
    }

    /**
     * Temporarily allow the creation of unrelated objects.
     * @deprecated TODO Remove this constructor.
     */
    public Player(String name) {
        this.game = null;
        this.name = name;
        player = new Players(name);
        player.setQuestion_number(questionNumber);
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    protected Database getDatabase() {
        return game.getDatabase();
    }
}
