package engine.services;

import engine.jpa.QuizRepository;
import engine.domain.Quiz;
import engine.dto.AnswerFeedbackDTO;
import engine.dto.QuizAnswerDTO;
import engine.dto.QuizCreationDTO;
import engine.dto.QuizDTO;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class QuizService {

    private final QuizRepository quizRepository;

    private final ModelMapper modelMapper;

    @Autowired
    public QuizService(QuizRepository quizRepository, ModelMapper modelMapper) {
        this.quizRepository = quizRepository;
        this.modelMapper = modelMapper;
    }

    public QuizDTO findById(long id) {
        return convertToQuizDTO(quizRepository
                .findById(id)
                .orElseThrow(IllegalArgumentException::new));
    }

    public QuizDTO save(QuizCreationDTO quiz) {
        return convertToQuizDTO(
                quizRepository.save(
                        convertFromQuizCreationDTO(
                                quiz)));

    }


    public List<QuizDTO> getAllQuizzes() {
        return ((List<Quiz>) quizRepository
                .findAll())
                .stream()
                .map(this::convertToQuizDTO)
                .collect(Collectors.toList());

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

    public AnswerFeedbackDTO checkQuizAnswer(QuizAnswerDTO answer) {
        Quiz quiz = quizRepository.findById(answer.getId()).orElseThrow(IllegalArgumentException::new);
        boolean success = true;
        List<Integer> answers = answer.getAnswer();
        if (answers.size() == quiz.getAnswer().size()) {
            for (Integer ans : answers) {
                int index = quiz.getAnswer().indexOf(ans);
                if (index == -1) {
                    success = false;
                    break;
                }
            }
        } else {
            success = false;
        }
        AnswerFeedbackDTO feedback = new AnswerFeedbackDTO();
        feedback.setFeedback(success ? "Congratulations, you're right!"
                : "Wrong answer! Please, try again.");
        feedback.setSuccess(success);
        return feedback;
    }
}
