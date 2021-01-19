package engine.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.Email;
import java.time.LocalDateTime;

@Entity(name = "users")
@Table
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@ToString
public class User {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;

    @Email
    public String email;
    public String password;

    @CreatedDate
    @JsonIgnore
    private LocalDateTime createdOn;

    @LastModifiedDate
    @JsonIgnore
    private LocalDateTime updatedOn;
}
