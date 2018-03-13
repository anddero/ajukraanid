package ee.ttu.idk0071.ajukraanid.database;

import ee.ttu.idk0071.ajukraanid.database.internal.GamesRepository;
import ee.ttu.idk0071.ajukraanid.database.internal.PlayersRepository;
import ee.ttu.idk0071.ajukraanid.database.internal.QuestionsRepository;
import ee.ttu.idk0071.ajukraanid.database.sync.Entry;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class Database extends Entry {
    // inaccessible internal repositories
    private final GamesRepository gamesRepository;
    private final PlayersRepository playersRepository;
    private final QuestionsRepository questionsRepository;

    // accessible members
    @Getter private final ArrayList<Game> games = new ArrayList<>(); // all the game sessions
    @Getter private final ArrayList<Question> questions = new ArrayList<>(); // all questions

    @Autowired
    private Database(GamesRepository gamesRepository, PlayersRepository playersRepository, QuestionsRepository questionsRepository) {
        this.gamesRepository = gamesRepository;
        this.playersRepository = playersRepository;
        this.questionsRepository = questionsRepository;
        loadDatabaseEntries();
        System.out.println("Finished loading database entries...");
        System.out.println(games.size() + " games");
        games.forEach(game -> {
            System.out.println("\t" + game.getGameCode() + " " + game.getGameState() + " (" + game.getPlayers().size
                    () + " players)");
            game.getPlayers()
                    .forEach(player -> System.out.println("\t\t" + player.getName() + "\t" + player.getQuestionNumber()));
        });
        System.out.println(questions.size() + " questions");
        questions.forEach(question -> System.out.println("\t" + question.getText()));
    }

    private void loadDatabaseEntries() {
        gamesRepository.findAll()
                .forEach(game -> new Game(this, game));
        playersRepository.findAll()
                .forEach(player -> {
                    games.forEach(game -> {
                        if (player.getGame_id().equals(game.getGame().getGame_id())) {
                            new Player(game, player);
                        }
                    });
                });
        questionsRepository.findAll()
                .forEach(question -> new Question(this, question));
    }

    GamesRepository getGamesRepository() {
        return gamesRepository;
    }

    PlayersRepository getPlayersRepository() {
        return playersRepository;
    }

    QuestionsRepository getQuestionsRepository() {
        return questionsRepository;
    }

    @Override
    protected Database getDatabase() {
        return this;
    }
}
