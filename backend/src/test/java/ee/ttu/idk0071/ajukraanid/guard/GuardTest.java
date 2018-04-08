package ee.ttu.idk0071.ajukraanid.guard;

import ee.ttu.idk0071.ajukraanid.database.Game;
import ee.ttu.idk0071.ajukraanid.database.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class GuardTest {
    private final Guard guard = new Guard();
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
        /*game = Mockito.mock(Game.class);*/
    }

    @Test
    void testCheckJoinGameWhileLobby() {
        /*Mockito.when(game.getGameState()).thenReturn(Game.State.LOBBY);
        guard.checkJoinGame(game);*/
    }

    @Test
    void testCheckJoinGameWhileOther() {
        /*for (Game.State state : Game.State.values()) {
            if (state == Game.State.LOBBY) continue;
            Mockito.when(game.getGameState()).thenReturn(state);
            assertThrows(GuardException.class, () -> guard.checkJoinGame(game));
        }*/
    }

    @Test
    void testCheckJoinGameWhileFull() {
        /*Mockito.when(game.getGameState()).thenReturn(Game.State.LOBBY);

        Mockito.when(game.getPlayers()).thenReturn(getPlayers(30));
        assertThrows(GuardException.class, () -> guard.checkJoinGame(game));

        Mockito.when(game.getPlayers()).thenReturn(getPlayers(20));
        assertThrows(GuardException.class, () -> guard.checkJoinGame(game));*/
    }

    @Test
    void testCheckJoinGameSucceed() {
        /*Mockito.when(game.getGameState()).thenReturn(Game.State.LOBBY);

        Mockito.when(game.getPlayers()).thenReturn(getPlayers(5));
        guard.checkJoinGame(game);

        Mockito.when(game.getPlayers()).thenReturn(getPlayers(2));
        guard.checkJoinGame(game);*/
    }
}