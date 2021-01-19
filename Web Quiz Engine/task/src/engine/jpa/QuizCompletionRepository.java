package engine.jpa;

import engine.domain.QuizCompletion;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface QuizCompletionRepository extends
        PagingAndSortingRepository<QuizCompletion, Long>,
        JpaSpecificationExecutor<QuizCompletion> {
    Page<QuizCompletion> findAllByEmail(String email, Pageable pageable);
}
