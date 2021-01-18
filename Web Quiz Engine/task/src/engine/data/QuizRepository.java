package engine.data;

import engine.domain.Quiz;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

public interface QuizRepository extends CrudRepository<Quiz, Long>,
        JpaSpecificationExecutor<Quiz> {
}
