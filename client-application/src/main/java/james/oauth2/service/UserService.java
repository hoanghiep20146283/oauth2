package james.oauth2.service;

import james.oauth2.User;
import james.oauth2.UserRepository;
import james.oauth2.model.UserModel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public Mono<User> findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Transactional
    public Mono<Void> registerUser(UserModel userModel) {
        User user = new User();
        user.setEmail(userModel.getEmail());
        user.setFirstName(userModel.getFirstName());
        user.setLastName(userModel.getLastName());
        user.setRole("USER");
        user.setPassword(userModel.getPassword());

        return userRepository.save(user).then();
    }
}
