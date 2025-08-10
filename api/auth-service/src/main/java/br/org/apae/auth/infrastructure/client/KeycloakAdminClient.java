package br.org.apae.auth.infrastructure.client;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;

import br.org.apae.auth.api.dto.RoleRepresentationDTO;
import br.org.apae.auth.api.dto.UserRepresentationDTO;
import br.org.apae.auth.infrastructure.util.RestClientExecutor;

@Component
public class KeycloakAdminClient {
  private RestClient restClient;
  private final String keycloakAdminUrl;
  private final String urlLoginToken;
  private final String clientId;
  private final String clientSecret;

  public KeycloakAdminClient(
      RestClient restClient,
      @Value("${keycloak_admin_url}") String keycloakAdminUrl,
      @Value("${client_id}") String clientId,
      @Value("${client_secret}") String clientSecret,
      @Value("${url_login_token}") String urlLoginToken) {
    this.restClient = restClient;
    this.keycloakAdminUrl = keycloakAdminUrl;
    this.urlLoginToken = urlLoginToken;
    this.clientId = clientId;
    this.clientSecret = clientSecret;
  }

  public boolean userExistsByUsername(String username, String token) {
    String url = String.format("%s/users?username=%s", keycloakAdminUrl, username);

    UserRepresentationDTO[] users = RestClientExecutor.execute(() ->
      restClient.get()
        .uri(url)
        .headers(h -> h.addAll(getHeadersWithToken(token)))
        .retrieve()
        .body(UserRepresentationDTO[].class),
        "Buscando usuário por username"
      );

    return users != null && users.length > 0;
  }

  public boolean userExistsByEmail(String email, String token) {
    String url = String.format("%s/users?email=%s", keycloakAdminUrl, email);
    UserRepresentationDTO[] users = RestClientExecutor.execute(() ->
      restClient.get()
        .uri(url)
        .headers(h -> h.addAll(getHeadersWithToken(token)))
        .retrieve()
        .body(UserRepresentationDTO[].class),
        "Buscando usuário por email"
      );
    return users != null && users.length > 0;
  }

  public String createUser(UserRepresentationDTO user, String password, String token) {
    String createUserUrl = keycloakAdminUrl + "/users";

    RestClientExecutor.execute(() ->
      restClient.post()
        .uri(createUserUrl)
        .headers(h -> h.addAll(getHeadersWithToken(token)))
        .body(user)
        .retrieve()
        .toBodilessEntity(),
        "Criando usuário no Keycloak"
    );

    String url = keycloakAdminUrl + "/users?username=" + user.username();
    UserRepresentationDTO[] foundUser = RestClientExecutor.execute(() ->
      restClient.get()
        .uri(url)
        .headers(h -> h.addAll(getHeadersWithToken(token)))
        .retrieve()
        .body(UserRepresentationDTO[].class),
        "Buscar usuário por username"
      );

      String userId = foundUser[0].id();

      Map<String, Object> credentials = Map.of(
          "type", "password",
          "value", password,
          "temporary", false
      );

      RestClientExecutor.execute(() ->
        restClient.put()
          .uri(keycloakAdminUrl + "/users/" + userId + "/reset-password")
          .headers(h -> h.addAll(getHeadersWithToken(token)))
          .body(credentials)
          .retrieve()
          .toBodilessEntity(),
          "Definindo senha do usuário"
      );
      
    return userId;
  }

  public String getAccessToken(String username, String password) {
    MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
    formData.add("grant_type", "password");
    formData.add("username", username);
    formData.add("password", password);
    formData.add("client_id", clientId);
    formData.add("client_secret", clientSecret);

    return RestClientExecutor.execute(() ->
      restClient.post()
        .uri(urlLoginToken)
        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
        .body(formData)
        .retrieve()
        .body(String.class),
        "Realizando login"
      );
  }

  public void assignRealmRole(String userId, String roleName, String token) {
    String roleUrl = keycloakAdminUrl + "/roles/" + roleName;

    RoleRepresentationDTO role = RestClientExecutor.execute(() ->
      restClient.get()
        .uri(roleUrl)
        .headers(h -> h.addAll(getHeadersWithToken(token)))
        .retrieve()
        .body(RoleRepresentationDTO.class),
        "Buscando role no Keycloak"
      );

    List<RoleRepresentationDTO> roles = List.of(role);
    String assignUrl = keycloakAdminUrl + "/users/" + userId + "/role-mappings/realm";

    RestClientExecutor.execute(() ->
      restClient.post()
        .uri(assignUrl)
        .headers(h -> h.addAll(getHeadersWithToken(token)))
        .body(roles)
        .retrieve()
        .toBodilessEntity(),
        "Atribuindo role ao usuário"
      );
  }

  private HttpHeaders getHeadersWithToken(String token) {
    HttpHeaders headers = new HttpHeaders();
    headers.setBearerAuth(token);
    headers.setContentType(MediaType.APPLICATION_JSON);
    return headers;
  }
}
