package engine.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity(name = "quizzes")
@Table
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@ToString
public class Quiz {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;

    String text;
    String title;

    @JsonIgnore
    @ElementCollection
    List<Integer> answer;

    public Quiz() {

    }

    @ElementCollection
    List<String> options;

    @CreatedDate
    @JsonIgnore
    private LocalDateTime createdOn;

    @LastModifiedDate
    @JsonIgnore
    private LocalDateTime updatedOn;

}
