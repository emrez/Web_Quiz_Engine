package engine.dto;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Getter
@Setter
@ToString
public class QuizCreationDTO {

    @NotEmpty
    public String title;

    @NotEmpty
    public String text;

    @NotNull
    @Size(min = 2)
    public List<String> options;

    public List<Integer> answer;

}
