package engine.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class AnswerFeedbackDTO {
    private String feedback;
    private boolean success;
}
