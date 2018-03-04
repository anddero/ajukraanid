package ee.ttu.idk0071.ajukraanid.database;

import ee.ttu.idk0071.ajukraanid.database.internal.GamesRepository;
import ee.ttu.idk0071.ajukraanid.database.internal.PlayersRepository;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class Database {
    // inaccessible internal repositories
    private final GamesRepository gamesRepository;
    private final PlayersRepository playersRepository;

    // accessible members
    @Getter private final List<Game> games = new ArrayList<>(); // all the game sessions

    @Autowired
    private Database(GamesRepository gamesRepository, PlayersRepository playersRepository) {
        this.gamesRepository = gamesRepository;
        this.playersRepository = playersRepository;
        loadGames();
        System.out.println("Finished loading database entries...");
        games.forEach(game -> {
            System.out.println("\t" + game.getGameCode() + "\t" + game.getGameState());
            game.getPlayers()
                    .forEach(player -> System.out.println("\t\t" + player.getName() + "\t" + player.getQuestionNumber()));
        });
    }

    private void loadGames() {
        gamesRepository.findAll()
                .forEach(game -> games.add(new Game(this, game)));
        playersRepository.findAll()
                .forEach(player -> {
                    games.forEach(game -> {
                        if (player.getGame_id().equals(game.getGame().getGame_id())) {
                            game.getPlayers().add(new Player(game, player));
                        }
                    });
                });
    }

    GamesRepository getGamesRepository() {
        return gamesRepository;
    }

    PlayersRepository getPlayersRepository() {
        return playersRepository;
    }
}
