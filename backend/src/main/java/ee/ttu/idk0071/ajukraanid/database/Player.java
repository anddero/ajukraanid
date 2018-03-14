package ee.ttu.idk0071.ajukraanid.database;

import ee.ttu.idk0071.ajukraanid.database.internal.Players;
import ee.ttu.idk0071.ajukraanid.database.sync.Entry;
import lombok.Getter;
import lombok.Setter;

public final class Player extends Entry {
    // inaccessible
    private final Game game;
    private Players player;
    // accessible
    @Getter private final String name;

    @Getter @Setter private int points = 0;     // TODO IS TEMPORARY

    // methods

    /**
     * From existing database entry.
     */
    Player(Game game, Players player) {
        this.game = game;
        game.getPlayers().add(this);
        this.player = player;
        this.name = player.getName();
    }

    /**
     * New instance.
     */
    public Player(Game game, String name) {
        this.game = game;
        game.getPlayers().add(this);
        player = new Players(game.getGame().getId(), name);
        this.name = name;
        player = game.getDatabase().getPlayersRepository().save(player);
    }

    /**
     * Temporarily allow the creation of unrelated objects.
     * @deprecated TODO Remove this constructor.
     */
    public Player(String name) {
        this.game = null;
        player = new Players((long) -1, name);
        this.name = name;
    }

    Players getPlayer() {
        return player;
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
