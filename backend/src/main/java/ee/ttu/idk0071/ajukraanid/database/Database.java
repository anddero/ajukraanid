package ee.ttu.idk0071.ajukraanid.database;

import ee.ttu.idk0071.ajukraanid.database.internal.*;
import ee.ttu.idk0071.ajukraanid.database.sync.Entry;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public final class Database extends Entry {
    // inaccessible internal repositories
    private final GamesRepository gamesRepository;
    private final PlayersRepository playersRepository;
    private final PlainQuestionsRepository plainQuestionsRepository;
    private final QuestionsRepository questionsRepository;
    private final AnswersRepository answersRepository;
    private final EvaluationsRepository evaluationsRepository;

    // accessible members
    @Getter private final ArrayList<Game> games = new ArrayList<>(); // all the game sessions
    @Getter private final ArrayList<PlainQuestion> plainQuestions = new ArrayList<>(); // all questions

    @Autowired
    private Database(GamesRepository gamesRepository, PlayersRepository playersRepository,
                     PlainQuestionsRepository plainQuestionsRepository, QuestionsRepository questionsRepository,
                     AnswersRepository answersRepository, EvaluationsRepository evaluationsRepository) {
        this.gamesRepository = gamesRepository;
        this.playersRepository = playersRepository;
        this.plainQuestionsRepository = plainQuestionsRepository;
        this.questionsRepository = questionsRepository;
        this.answersRepository = answersRepository;
        this.evaluationsRepository = evaluationsRepository;
        loadDatabaseEntries();
        System.out.println("Finished loading database entries...");
        printDatabaseEntries();
    }

    private void loadDatabaseEntries() {
        loadGames();
        loadPlainQuestions();
    }

    private void loadGames() {
        gamesRepository.findAll()
                .forEach(game -> new Game(this, game));
        games.forEach(this::loadPlayers);
        games.forEach(this::loadQuestions);
    }

    private void loadPlayers(Game game) {
        playersRepository.findByGameId(game.getGame().getId())
                .forEach(player -> new Player(game, player));
    }

    private void loadQuestions(Game game) {
        questionsRepository.findByGameId(game.getGame().getId())
                .forEach(question -> new Question(game, question));
        game.getQuestions().forEach(this::loadAnswers);
        game.getQuestions().forEach(this::loadEvaluations);
    }

    private void loadAnswers(Question question) {
        answersRepository.findByQuestionId(question.getQuestion().getId())
                .forEach(answer -> new Answer(question, answer));
    }

    private void loadEvaluations(Question question) {
        evaluationsRepository.findByQuestionId(question.getQuestion().getId())
                .forEach(evaluation -> new Evaluation(question, evaluation));
    }

    private void loadPlainQuestions() {
        plainQuestionsRepository.findAll()
                .forEach(question -> new PlainQuestion(this, question));
    }

    private void printDatabaseEntries() {
        System.out.println(games.size() + " games");
        games.forEach(game -> {
            System.out.println("\t" + game.getGameCode() + " " + game.getGameState() + " (" + game.getPlayers().size
                    () + " players)");
            game.getPlayers()
                    .forEach(player -> System.out.println("\t\t" + player.getName()));
        });
        System.out.println(plainQuestions.size() + " questions");
        plainQuestions.forEach(question -> System.out.println("\t" + question.getText()));
    }

    GamesRepository getGamesRepository() {
        return gamesRepository;
    }

    PlayersRepository getPlayersRepository() {
        return playersRepository;
    }

    PlainQuestionsRepository getPlainQuestionsRepository() {
        return plainQuestionsRepository;
    }

    QuestionsRepository getQuestionsRepository() {
        return questionsRepository;
    }

    AnswersRepository getAnswersRepository() {
        return answersRepository;
    }

    EvaluationsRepository getEvaluationsRepository() {
        return evaluationsRepository;
    }

    @Override
    protected Database getDatabase() {
        return this;
    }
}
