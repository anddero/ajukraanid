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
        games.forEach(System.out::println); // TODO Remove this.
    }

    private void loadGames() {
        gamesRepository.findAll()
                .forEach(game -> games.add(new Game(game)));
    }
}
