package ee.ttu.idk0071.ajukraanid.guard;

import ee.ttu.idk0071.ajukraanid.database.Game;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class Guard {
    private static final int MINIMUM_PLAYERS = 2;
    private static final int MAXIMUM_PLAYERS = 8;

    public void checkJoinGame(Game game) {
        if (game.getGameState() != Game.State.LOBBY) {
            throw new GuardException("Cannot join the game at this point");
        }
        if (game.getPlayers().size() >= MAXIMUM_PLAYERS) {
            throw new GuardException("The game is full: " + MAXIMUM_PLAYERS + " players");
        }
    }

    public void checkStartGame(Game game) {
        if (game.getGameState() != Game.State.LOBBY) {
            throw new GuardException("Cannot start the game at this point");
        }
        if (game.getPlayers().size() < MINIMUM_PLAYERS) {
            throw new GuardException("Not enough players to start the game, need at least " + MINIMUM_PLAYERS);
        }
    }

    public void checkSubmitAnswer(Game game) {
        if (game.getGameState() != Game.State.ANSWERING) {
            throw new GuardException("Cannot submit an answer at this point");
        }
    }

    public void checkGivePoints(Game game) {
        if (game.getGameState() != Game.State.GRADING) {
            throw new GuardException("Cannot give points at the current moment");
        }
    }

    public void checkRemovePlayer(Game game) {
        if (game.getGameState() != Game.State.LOBBY) {
            throw new GuardException("Cannot remove players at this point");
        }
    }
}
