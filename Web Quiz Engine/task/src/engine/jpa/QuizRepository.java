package engine.jpa;

import engine.domain.Quiz;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface QuizRepository extends
        PagingAndSortingRepository<Quiz, Long>,
        JpaSpecificationExecutor<Quiz> {
}
