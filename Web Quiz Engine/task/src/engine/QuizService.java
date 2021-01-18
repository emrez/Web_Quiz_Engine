package engine;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;

import java.util.ArrayList;
import java.util.List;

@Service
public class QuizService {

    QuizRepository quizRepository;

    @Autowired
    public QuizService(QuizRepository quizRepository) {
        this.quizRepository = quizRepository;
    }

    public Quiz findById(long id) {
        return quizRepository.findById(id).orElseThrow(IllegalArgumentException::new);
    }

    public Quiz save(Quiz quiz) {
        return null;
    }


    public Iterable<Quiz> findAll() {
//        List<ModelMap> quizzes = new ArrayList<>();
//        quizRepository.findAll().forEach(quiz -> {
//            quizzes.add(convertToModel(quiz));
//        });

        return quizRepository.findAll();
    }

}
