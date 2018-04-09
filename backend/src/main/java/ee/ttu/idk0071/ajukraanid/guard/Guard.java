package ee.ttu.idk0071.ajukraanid.guard;
//
import ee.ttu.idk0071.ajukraanid.config.GameConfig;
import ee.ttu.idk0071.ajukraanid.database.Game;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Guard {
    private final GameConfig gameConfig;

    @Autowired
    public Guard(GameConfig gameConfig) {
        this.gameConfig = gameConfig;
    }

    public void checkJoinGame(Game game) {
        if (game.getGameState() != Game.State.LOBBY) {
            throw new GuardException("Cannot join the game at this point");
        }
        if (game.getPlayers().size() >= gameConfig.getMaximumPlayers()) {
            throw new GuardException("The game is full: " + gameConfig.getMaximumPlayers() + " players");
        }
    }

    public void checkStartGame(Game game) {
        if (game.getGameState() != Game.State.LOBBY) {
            throw new GuardException("Cannot start the game at this point");
        }
        if (game.getPlayers().size() < gameConfig.getMinimumPlayers()) {
            throw new GuardException("Not enough players to start the game, need at least " + gameConfig
                    .getMinimumPlayers());
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
