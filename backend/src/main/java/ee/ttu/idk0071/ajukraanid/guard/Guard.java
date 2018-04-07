package ee.ttu.idk0071.ajukraanid.guard;

import ee.ttu.idk0071.ajukraanid.database.Database;
import ee.ttu.idk0071.ajukraanid.database.Game;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class Guard {
    private static final int MINIMUM_PLAYERS = 2;
    private static final int MAXIMUM_PLAYERS = 8;

    private final Database database;

    public boolean canJoinGame(Game game) {
        return game.getGameState() == Game.State.LOBBY
                && game.getPlayers().size() < MAXIMUM_PLAYERS;
    }

    public boolean canStartGame(Game game) {
        return game.getGameState() == Game.State.LOBBY
                && game.getPlayers().size() >= MINIMUM_PLAYERS;
    }

    public boolean canSubmitAnswer(Game game) {
        return game.getGameState() == Game.State.ANSWERING;
    }

    public boolean canGivePoints(Game game) {
        return game.getGameState() == Game.State.GRADING;
    }

    public boolean canRemovePlayer(Game game) {
        return game.getGameState() == Game.State.LOBBY;
    }
}
