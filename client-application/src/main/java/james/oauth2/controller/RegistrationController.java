package james.oauth2.controller;

import james.oauth2.User;
import james.oauth2.service.UserService;
import james.oauth2.model.UserModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@Slf4j
public class RegistrationController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public Mono<Void> registerUser(@RequestBody UserModel userModel) {
        return userService.registerUser(userModel);
    }
}
