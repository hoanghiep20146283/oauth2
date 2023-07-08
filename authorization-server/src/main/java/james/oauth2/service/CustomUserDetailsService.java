package james.oauth2.service;

import james.oauth2.User;
import james.oauth2.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
@Transactional
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Mono<User> user = userRepository.findByEmail(email);
        User authenticatedUser = user.switchIfEmpty(Mono.error(new UsernameNotFoundException("No User Found"))).block();

        return new org.springframework.security.core.userdetails.User(
                authenticatedUser.getEmail(),
                authenticatedUser.getPassword(),
                authenticatedUser.isEnabled(),
                true,
                true,
                true,
                getAuthorities(List.of(authenticatedUser.getRole()))
        );
    }

    private Collection<? extends GrantedAuthority> getAuthorities(List<String> roles) {
        List<GrantedAuthority> authorities = new ArrayList<>();
        for (String role : roles) {
            authorities.add(new SimpleGrantedAuthority(role));
        }
        return authorities;
    }
}
