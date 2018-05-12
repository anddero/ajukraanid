package ee.ttu.idk0071.ajukraanid.database.internal;

import org.springframework.data.repository.CrudRepository;

import javax.transaction.Transactional;

public interface PlainQuestionsRepository extends CrudRepository<PlainQuestions, Long> {
    @Transactional
    Long deleteById(Long id);
}
