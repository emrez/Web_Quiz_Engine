package engine.controllers;

import engine.dto.AnswerFeedbackDTO;
import engine.dto.QuizAnswerDTO;
import engine.dto.QuizCreationDTO;
import engine.dto.QuizDTO;
import engine.services.QuizService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Controller
public class QuizController {

    private final QuizService quizService;
    private static final String ID_NOT_FOUND_MESSAGE = "The quiz with the provided id does not exist.";

    @Autowired
    public QuizController(QuizService quizService) {
        this.quizService = quizService;
    }


    @GetMapping(value = "/api/quizzes/{id}",
            produces = "application/json")
    public ResponseEntity<QuizDTO> getApiQuiz(
            @PathVariable long id) {

        try {
            QuizDTO quiz = quizService.findById(id);
            return ResponseEntity.ok(quiz);

        } catch (IllegalArgumentException e) {
            System.out.println("GET api/quizzes/{id} - id does not exist");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ID_NOT_FOUND_MESSAGE);
        }
    }

    @GetMapping(value = "/api/quizzes",
            produces = "application/json")
    public ResponseEntity<List<QuizDTO>> getAllQuizzes() {
        List<QuizDTO> quizzes = quizService.getAllQuizzes();
        return ResponseEntity.ok(quizzes);

    }

    @PostMapping(value = "/api/quizzes", consumes = "application/json", produces = "application/json")
    public ResponseEntity<QuizDTO> postNewQuiz(
            @RequestBody @Validated QuizCreationDTO quizCreation,
            BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Required field is empty");
        }
        QuizDTO quiz = quizService.save(quizCreation);
        return ResponseEntity.ok(quiz);
    }

    @PostMapping(value = "/api/quizzes/{id}/solve",
            consumes = "application/json",
            produces = "application/json")
    public ResponseEntity<AnswerFeedbackDTO> postQuizAnswer(
            @PathVariable() Long id,
            @RequestBody QuizAnswerDTO quizAnswer) {

        try {
            quizAnswer.setId(id);
            AnswerFeedbackDTO feedback = quizService.checkQuizAnswer(quizAnswer);
            return ResponseEntity.ok(feedback);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Given id does not exist");
        }
    }
}
