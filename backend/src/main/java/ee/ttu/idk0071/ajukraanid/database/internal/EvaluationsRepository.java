package ee.ttu.idk0071.ajukraanid.database.internal;
//
import org.springframework.data.repository.CrudRepository;

public interface EvaluationsRepository extends CrudRepository<Evaluations, Long> {
    Iterable<Evaluations> findByQuestionId(Long questionId);
}
