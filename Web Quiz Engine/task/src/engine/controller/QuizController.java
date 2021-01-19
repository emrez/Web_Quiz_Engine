package engine.controller;

import engine.domain.QuizCompletion;
import engine.dto.AnswerFeedbackDTO;
import engine.dto.QuizAnswerDTO;
import engine.dto.QuizCreationDTO;
import engine.dto.QuizDTO;
import engine.services.QuizService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

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
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ID_NOT_FOUND_MESSAGE);
        }
    }

    @GetMapping(value = "/api/quizzes",
            produces = "application/json")
    public ResponseEntity<Page<QuizDTO>> getAllQuizzes(
            @RequestParam("page") int page
    ) {
        Page<QuizDTO> quizPage = quizService.getAllQuizzesOnPage(page, 10);
        return ResponseEntity.ok(quizPage);

    }

    @PostMapping(value = "/api/quizzes", consumes = "application/json", produces = "application/json")
    public ResponseEntity<QuizDTO> postNewQuiz(
            Authentication principal,
            @RequestBody @Validated QuizCreationDTO quizCreation,
            BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Required field is empty");
        }
        QuizDTO quiz = quizService.save(quizCreation, principal.getName());
        return ResponseEntity.ok(quiz);
    }

    @PostMapping(value = "/api/quizzes/{id}/solve",
            consumes = "application/json",
            produces = "application/json")
    public ResponseEntity<AnswerFeedbackDTO> postQuizAnswer(
            @PathVariable() Long id,
            @RequestBody QuizAnswerDTO quizAnswer,
            Authentication principal) {

        try {
            quizAnswer.setId(id);
            AnswerFeedbackDTO feedback = quizService.checkQuizAnswer(quizAnswer, principal.getName());
            return ResponseEntity.ok(feedback);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Given id does not exist");
        }
    }

    @DeleteMapping(value = "/api/quizzes/{id}")
    public ResponseEntity<Object> deleteQuiz(
            @PathVariable Long id,
            Authentication principal
    ) {
        try {
            quizService.delete(id, principal.getName());
        } catch (IllegalAccessException e) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "This author does not have permission to delete this quiz.");
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Given id does not exist");
        }
        return ResponseEntity.noContent().build();
    }

    @GetMapping(value = "/api/quizzes/completed", produces = "application/json")
    public ResponseEntity<Page<QuizCompletion>> getCompletedQuizzes(
            @RequestParam("page") int page,
            Authentication principal
    ) {
        return ResponseEntity
                .ok(quizService
                        .getCompletedQuizzesOnPage(
                                page,
                                10,
                                principal.getName()));

    }


}
