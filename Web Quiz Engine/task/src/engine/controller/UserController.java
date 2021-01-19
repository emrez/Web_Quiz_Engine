package engine.controller;

import engine.dto.UserRegistrationDTO;
import engine.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.server.ResponseStatusException;

@Controller
public class UserController {
    UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping(value = "/api/register", consumes = "application/json", produces = "application/json")
    public ResponseEntity<Object> registerUserAccount(
            @RequestBody @Validated UserRegistrationDTO userDto,
            BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Required conditions are not met");
        }
        try {
            userService.registerNewUserAccount(userDto);
        } catch (IllegalArgumentException uaeEx) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "An user with this email already exists");
        }

        return ResponseEntity.ok().build();
    }
}
