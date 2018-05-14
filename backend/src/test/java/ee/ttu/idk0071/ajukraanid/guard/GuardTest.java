package ee.ttu.idk0071.ajukraanid.guard;

import ee.ttu.idk0071.ajukraanid.config.GameConfig;
import ee.ttu.idk0071.ajukraanid.database.Game;
import ee.ttu.idk0071.ajukraanid.database.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertThrows;

class GuardTest {
    private final Guard guard = new Guard(Mockito.mock(GameConfig.class));
    private Game game;

    private ArrayList<Player> getPlayers(int amt) {
        ArrayList<Player> list = new ArrayList<>();
        for (int i = 0; i != amt; ++i) {
            list.add(Mockito.mock(Player.class));
        }
        return list;
    }

    @BeforeEach
    void startUp() {
        game = Mockito.mock(Game.class);
    }

    @Test
    void testCheckJoinGameWhileLobby() {
        Mockito.when(game.getGameState()).thenReturn(Game.State.LOBBY);
        assertThrows(GuardException.class, () -> guard.checkJoinGame(game));
    }

    @Test
    void testCheckJoinGameWhileOther() {
        for (Game.State state : Game.State.values()) {
            if (state == Game.State.LOBBY) continue;
            Mockito.when(game.getGameState()).thenReturn(state);
            assertThrows(GuardException.class, () -> guard.checkJoinGame(game));
        }
    }

    @Test
    void testCheckJoinGameWhileFull() {
        Mockito.when(game.getGameState()).thenReturn(Game.State.LOBBY);

        Mockito.when(game.getPlayers()).thenReturn(getPlayers(30));
        assertThrows(GuardException.class, () -> guard.checkJoinGame(game));

        Mockito.when(game.getPlayers()).thenReturn(getPlayers(20));
        assertThrows(GuardException.class, () -> guard.checkJoinGame(game));
    }

    @Test
    void testCheckJoinGameSucceed() {
        GameConfig config = Mockito.mock(GameConfig.class);
        Mockito.when(config.getMaximumPlayers()).thenReturn(10);
        game = Mockito.mock(Game.class);

        Mockito.when(game.getGameState()).thenReturn(Game.State.LOBBY);

        Mockito.when(game.getPlayers()).thenReturn(getPlayers(11));
        assertThrows(GuardException.class, () -> guard.checkJoinGame(game));

        Mockito.when(game.getPlayers()).thenReturn(getPlayers(9));
        assertThrows(GuardException.class, () -> guard.checkJoinGame(game));
    }
}