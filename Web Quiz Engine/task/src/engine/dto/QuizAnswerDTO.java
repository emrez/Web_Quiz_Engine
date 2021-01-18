package engine.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class QuizAnswerDTO {
    private Long id;
    private List<Integer> answer;
}
