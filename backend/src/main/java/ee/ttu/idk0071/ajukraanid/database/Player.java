package ee.ttu.idk0071.ajukraanid.database;

import ee.ttu.idk0071.ajukraanid.database.internal.Players;
import ee.ttu.idk0071.ajukraanid.database.sync.Entry;
import ee.ttu.idk0071.ajukraanid.util.StringUtilities;
import lombok.Getter;
import lombok.Setter;

public final class Player extends Entry {
    // inaccessible
    private final Game game;
    private Players player;
    // accessible
    @Getter private final String name;
    @Getter private boolean valid = true;

    // methods

    /**
     * From existing database entry.
     */
    Player(Game game, Players player) {
        this.game = game;
        game.getPlayers().add(this);
        this.player = player;
        this.name = player.getName();
        this.valid = player.isValid();
    }

    /**
     * New instance.
     */
    public Player(Game game, String name) {
        this.game = game;
        game.getPlayers().add(this);
        player = new Players(game.getGame().getId(), name, valid);
        this.name = name;
        player = getDatabase().getPlayersRepository().save(player);
    }

    Players getPlayer() {
        return player;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
        player.setValid(valid);
        player = getDatabase().getPlayersRepository().save(player);
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    protected void appendTo(StringBuilder stringBuilder, int indentSize) {
        StringUtilities.addIndent(indentSize, stringBuilder);
        stringBuilder.append(name).append("\n");
    }

    @Override
    protected Database getDatabase() {
        return game.getDatabase();
    }
}
