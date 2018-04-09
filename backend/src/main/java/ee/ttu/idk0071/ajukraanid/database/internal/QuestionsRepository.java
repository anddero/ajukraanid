package ee.ttu.idk0071.ajukraanid.database.internal;
//
import org.springframework.data.repository.CrudRepository;

public interface QuestionsRepository extends CrudRepository<Questions, Long> {
    Iterable<Questions> findByGameId(Long gameId);
}
