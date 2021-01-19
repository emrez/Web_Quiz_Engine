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

    //    @ModelAttribute("user")
    @PostMapping(value = "/api/register", consumes = "application/json", produces = "application/json")
    public ResponseEntity<Object> registerUserAccount(
            @RequestBody @Validated UserRegistrationDTO userDto,
            BindingResult bindingResult) {
//            Model userDto) {
        System.out.println("Registering: " + userDto);

        if (bindingResult.hasErrors()) {
//            System.out.println(userDto);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Required conditions are not met");
//            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, bindingResult.getFieldError().getDefaultMessage());
        }
        try {
            userService.registerNewUserAccount(userDto);
        } catch (IllegalArgumentException uaeEx) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "An user with this email already exists");
        }

        return ResponseEntity.ok().build();
    }
}
