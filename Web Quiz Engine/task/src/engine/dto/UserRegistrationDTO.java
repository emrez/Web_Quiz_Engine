package engine.dto;


import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
public class UserRegistrationDTO {

    @NotNull
    @NotEmpty
    @Email(regexp = ".+@.+\\\\..+")
    private String email;

    @Size(min = 5)
    @NotNull
    @NotEmpty
    private String password;

}
