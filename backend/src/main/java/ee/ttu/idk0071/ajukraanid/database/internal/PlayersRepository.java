package ee.ttu.idk0071.ajukraanid.database.internal;
//
import org.springframework.data.repository.CrudRepository;

public interface PlayersRepository extends CrudRepository<Players, Long> {
    Iterable<Players> findByGameId(Long gameId);
}
