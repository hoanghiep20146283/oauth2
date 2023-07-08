package james.oauth2.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import james.oauth2.entity.ClientRegistrationEntity;
import james.oauth2.repository.ClientRegistrationReactiveRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientId;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizedClientRepository;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2RefreshToken;
import org.springframework.security.oauth2.core.oidc.OidcScopes;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ClientRegistrationService implements OAuth2AuthorizedClientRepository {

  private final ClientRegistrationReactiveRepository clientRegistrationReactiveRepository;

  public ClientRegistration findByRegistrationId(String clientRegistrationId) {
    return ClientRegistration.withRegistrationId(clientRegistrationId)
        .registrationId(clientRegistrationId)
        .redirectUri("http://127.0.0.1:8089/login/oauth2/code/{registrationId}")
        .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_JWT)
        .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
        .scope(OidcScopes.OPENID)
        .build();
  }

  @Override
  public <T extends OAuth2AuthorizedClient> T loadAuthorizedClient(String clientRegistrationId,
      Authentication principal, HttpServletRequest request) {
    return (T) clientRegistrationReactiveRepository.findById(
            getOAuth2AuthorizedClientId(clientRegistrationId, principal.getName()))
        .map(this::toOAuth2AuthorizedClient).block();
  }

  @Override
  public void saveAuthorizedClient(OAuth2AuthorizedClient authorizedClient,
      Authentication principal, HttpServletRequest request,
      jakarta.servlet.http.HttpServletResponse response) {
    ClientRegistrationEntity clientRegistrationEntity = ClientRegistrationEntity.builder()
        .clientName("client-application")
        .id(getOAuth2AuthorizedClientId(
            authorizedClient.getClientRegistration().getRegistrationId(),
            authorizedClient.getPrincipalName()))
        .clientRegistrationId(authorizedClient.getClientRegistration().getRegistrationId())
        .principalName(principal.getName())
        .build();
    clientRegistrationReactiveRepository.save(clientRegistrationEntity);
  }

  @Override
  public void removeAuthorizedClient(String clientRegistrationId, Authentication principal,
      HttpServletRequest request,
      HttpServletResponse response) {
    clientRegistrationReactiveRepository.deleteById(
        getOAuth2AuthorizedClientId(clientRegistrationId, principal.getName()));
  }

  private OAuth2AuthorizedClientId getOAuth2AuthorizedClientId(String clientRegistrationId,
      String principalName) {
    return new OAuth2AuthorizedClientId(clientRegistrationId, principalName);
  }


  private OAuth2AuthorizedClient toOAuth2AuthorizedClient(
      ClientRegistrationEntity entity) {
    ClientRegistration clientRegistration = findByRegistrationId(entity.getClientRegistrationId());
    OAuth2AccessToken accessToken = new OAuth2AccessToken(OAuth2AccessToken.TokenType.BEARER,
        entity.getAccessToken(), null, null);
    OAuth2RefreshToken refreshToken = new OAuth2RefreshToken(entity.getRefreshToken(), null);
    return new OAuth2AuthorizedClient(clientRegistration, entity.getPrincipalName(), accessToken,
        refreshToken);
  }
}
