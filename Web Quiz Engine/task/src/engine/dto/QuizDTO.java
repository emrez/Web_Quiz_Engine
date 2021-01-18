package engine.dto;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class QuizDTO {
    private int id;
    private String title;
    private String text;

    private List<String> options;

}
