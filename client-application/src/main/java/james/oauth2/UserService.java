package james.oauth2;

import james.oauth2.entity.User;
import james.oauth2.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class UserService {

  private final UserRepository userRepository;

  public Mono<User> findUserByEmail(String email) {
    return userRepository.findByEmail(email);
  }
}
