package ee.ttu.idk0071.ajukraanid.database;

import ee.ttu.idk0071.ajukraanid.database.internal.*;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class DatabaseTest {
    private Database database = Mockito.mock(Database.class);

    @Test
    void getGamesRepository() {
        Mockito.when(database.getGamesRepository()).thenReturn(Mockito.mock(GamesRepository.class));
    }

    @Test
    void getPlayersRepository() {
        Mockito.when(database.getPlayersRepository()).thenReturn(Mockito.mock(PlayersRepository.class));
    }

    @Test
    void getPlainQuestionsRepository() {
        Mockito.when(database.getPlainQuestionsRepository()).thenReturn(Mockito.mock(PlainQuestionsRepository.class));
    }

    @Test
    void getQuestionsRepository() {
        Mockito.when(database.getQuestionsRepository()).thenReturn(Mockito.mock(QuestionsRepository.class));
    }

    @Test
    void getAnswersRepository() {
        Mockito.when(database.getAnswersRepository()).thenReturn(Mockito.mock(AnswersRepository.class));
    }

    @Test
    void getEvaluationsRepository() {
        Mockito.when(database.getEvaluationsRepository()).thenReturn(Mockito.mock(EvaluationsRepository.class));
    }

    @Test
    void getDatabase() {
        Mockito.when(database.getDatabase()).thenReturn(database);
    }

    @Test
    void getGames() {
        Mockito.when(database.getGames()).thenReturn(new ArrayList<>());
    }

    @Test
    void getPlainQuestions() {
        Mockito.when(database.getPlainQuestions()).thenReturn(new ArrayList<>());
    }
}