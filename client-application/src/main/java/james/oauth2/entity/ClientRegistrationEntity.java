package james.oauth2.entity;

import javax.persistence.Id;
import lombok.Builder;
import lombok.Data;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientId;

@Data
@Builder
public class ClientRegistrationEntity {

  @Id
  private OAuth2AuthorizedClientId id;
  private String clientRegistrationId;
  private String clientName;
  private String accessToken;
  private String refreshToken;
  private String principalName;
}