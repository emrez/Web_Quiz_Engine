package engine.domain;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity(name = "completions")
@Table
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@ToString
public class QuizCompletion {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JsonIgnore
    private Long completionId;

    @JsonIgnore
    private String email;

    @CreatedDate
    private LocalDateTime completedAt;

    private Long id;
}
