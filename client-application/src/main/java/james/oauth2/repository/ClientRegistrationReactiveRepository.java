package james.oauth2.repository;

import james.oauth2.entity.ClientRegistrationEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientId;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface ClientRegistrationReactiveRepository extends
    ReactiveCrudRepository<ClientRegistrationEntity, OAuth2AuthorizedClientId> {

  Mono<ClientRegistrationEntity> findById(OAuth2AuthorizedClientId id);
}
