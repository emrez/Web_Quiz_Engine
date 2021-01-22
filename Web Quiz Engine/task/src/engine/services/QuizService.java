package engine.services;

import engine.domain.Quiz;
import engine.domain.QuizCompletion;
import engine.dto.AnswerFeedbackDTO;
import engine.dto.QuizAnswerDTO;
import engine.dto.QuizCreationDTO;
import engine.dto.QuizDTO;
import engine.jpa.QuizCompletionRepository;
import engine.jpa.QuizRepository;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QuizService {

    private final QuizRepository quizRepository;

    private final QuizCompletionRepository completionRepository;

    private final ModelMapper modelMapper;

    @Autowired
    public QuizService(QuizRepository quizRepository,
                       QuizCompletionRepository completionRepository,
                       ModelMapper modelMapper) {
        this.quizRepository = quizRepository;
        this.completionRepository = completionRepository;
        this.modelMapper = modelMapper;
    }

    public QuizDTO findById(long id) {
        return convertToQuizDTO(
                quizRepository.findById(id).orElseThrow(IllegalArgumentException::new));
    }

    public QuizDTO save(QuizCreationDTO quiz, String email) {
        Quiz q = convertFromQuizCreationDTO(quiz);
        q.setAuthorEmail(email);
        q = quizRepository.save(q);
        return convertToQuizDTO(q);

    }

    public Page<QuizDTO> getAllQuizzesOnPage(int pageNumber,
                                             int rowPerPage) {
        if (pageNumber < 0 || quizRepository.count() == 0) {
            return Page.empty();
        }
        Pageable sortedById = PageRequest.of(
                pageNumber, rowPerPage, Sort.by("id").descending());
        Page<Quiz> page = quizRepository.findAll(sortedById);
        return page.map(this::convertToQuizDTO);
    }

    public Page<QuizCompletion> getCompletedQuizzesOnPage(int pageNumber,
                                                          int rowPerPage,
                                                          String email) {
        if (pageNumber < 0 || completionRepository.count() == 0) {
            return Page.empty();
        }
        Pageable sortedByCompletedAt = PageRequest.of(
                pageNumber, rowPerPage, Sort.by("completedAt").descending());

        return completionRepository.findAllByEmail(email, sortedByCompletedAt);
    }

    private QuizDTO convertToQuizDTO(Quiz user) {
        modelMapper.getConfiguration()
                .setMatchingStrategy(MatchingStrategies.LOOSE);
        return modelMapper
                .map(user, QuizDTO.class);
    }

    private Quiz convertFromQuizCreationDTO(QuizCreationDTO quizDTO) {
        modelMapper.getConfiguration()
                .setMatchingStrategy(MatchingStrategies.LOOSE);
        return modelMapper
                .map(quizDTO, Quiz.class);
    }

    public AnswerFeedbackDTO checkQuizAnswer(QuizAnswerDTO answer, String email) {
        Quiz quiz = quizRepository.findById(answer.getId()).orElseThrow(IllegalArgumentException::new);
        List<Integer> answers = answer.getAnswer();
        boolean success = answers.size() == quiz.getAnswer().size();
        if (success) {
            for (Integer ans : answers) {
                int index = quiz.getAnswer().indexOf(ans);
                if (index == -1) {
                    success = false;
                    break;
                }
            }
        }
        if (success) {
            QuizCompletion quizCompletion = new QuizCompletion();
            quizCompletion.setEmail(email);
            quizCompletion.setId(quiz.getId());
            completionRepository.save(quizCompletion);
        }

        AnswerFeedbackDTO feedback = new AnswerFeedbackDTO();
        feedback.setFeedback(success ? "Congratulations, you're right!"
                : "Wrong answer! Please, try again.");
        feedback.setSuccess(success);
        return feedback;
    }

    public void delete(Long id, String email) throws IllegalAccessException {
        Quiz quiz = quizRepository.findById(id).orElseThrow(IllegalArgumentException::new);
        if (!quiz.getAuthorEmail().equals(email)) {
            throw new IllegalAccessException("This user doesn't have permission to delete this quiz");
        }
        quizRepository.deleteById(id);
    }


}
