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
import br.org.apae.auth.api.dto.TokenResponseDTO;
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
      @Value("${url_login_token}") String urlLoginToken
  ) {
    this.restClient = restClient;
    this.keycloakAdminUrl = keycloakAdminUrl;
    this.urlLoginToken = urlLoginToken;
    this.clientId = clientId;
    this.clientSecret = clientSecret;
  }

  private HttpHeaders getHeadersWithToken(String token) {
    HttpHeaders headers = new HttpHeaders();
    headers.setBearerAuth(token);
    headers.setContentType(MediaType.APPLICATION_JSON);
    return headers;
  }

  private UserRepresentationDTO[] getUsersByQuery(String query, String token) {
    String url = String.format("%s/users?%s", keycloakAdminUrl, query);

    return doGet(
      url,
      token,
      UserRepresentationDTO[].class,
      "Buscando usuários por query"
    );
  }

  private <T> T doGet(String url, String token, Class<T> responseType, String action) {
    return RestClientExecutor.execute(() ->
      restClient.get()
        .uri(url)
        .headers(h -> h.addAll(getHeadersWithToken(token)))
        .retrieve()
        .body(responseType),
        action
      );
  }

  private <T> T doPost(String url, Object body, MediaType contentType, Class<T> responseType, String action) {
    return RestClientExecutor.execute(() ->
      restClient.post()
        .uri(url)
        .contentType(contentType)
        .body(body)
        .retrieve()
        .body(responseType),
        action
      );
  }

  private <T> T doPostWithToken(String url, String token, Object body, MediaType contentType, Class<T> responseType, String action) {
    return RestClientExecutor.execute(() ->
      restClient.post()
        .uri(url)
        .headers(h -> h.addAll(getHeadersWithToken(token)))
        .contentType(contentType)
        .body(body)
        .retrieve()
        .body(responseType),
        action
    );
  }

  private void doPostNoBody(String url, String token, Object body, String action) {
    RestClientExecutor.execute(() ->
      restClient.post()
      .uri(url)
      .headers(h -> h.addAll(getHeadersWithToken(token)))
      .body(body)
      .retrieve()
      .toBodilessEntity(),
      action
      );
  }
  
  private void doPutNoBody(String url, String token, Object body, String action) {
    RestClientExecutor.execute(() ->
      restClient.put()
      .uri(url)
      .headers(h -> h.addAll(getHeadersWithToken(token)))
      .body(body)
      .retrieve()
      .toBodilessEntity(),
      action
      );
  }

  private MultiValueMap<String, String> getFormData(String username, String password) {
    MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
    formData.add("grant_type", "password");
    formData.add("username", username);
    formData.add("password", password);
    formData.add("client_id", clientId);
    formData.add("client_secret", clientSecret);
    return formData;
  }
  
  public boolean userExistsByUsername(String username, String token) {
    UserRepresentationDTO[] users = getUsersByQuery(
      String.format("username=%s", username),
      token
    );

    return users != null && users.length > 0;
  }

  public boolean userExistsByEmail(String email, String token) {
    UserRepresentationDTO[] users = getUsersByQuery(
      String.format("email=%s", email),
      token
    );
    
    return users != null && users.length > 0;
  }

  public String createUser(UserRepresentationDTO user, String password, String token) {
    String createUserUrl = keycloakAdminUrl + "/users";

    doPostNoBody(createUserUrl, token, user, "Criando usuário no Keycloak");

    UserRepresentationDTO[] foundUser = getUsersByQuery(
      String.format("username=%s", user.username()),
      token
    );

    String userId = foundUser[0].id();

    Map<String, Object> credentials = Map.of(
        "type", "password",
        "value", password,
        "temporary", false
    );

    doPutNoBody(
      String.format("%s/users/%s/reset-password",
      keycloakAdminUrl, userId),
      token,
      credentials,
      "Definindo senha do usuário"
    );
      
    return userId;
  }

  public TokenResponseDTO getAccessToken(String username, String password) {
    MultiValueMap<String, String> formData = getFormData(username, password);

    return doPost(
        urlLoginToken,
        formData,
        MediaType.APPLICATION_FORM_URLENCODED,
        TokenResponseDTO.class,
        "Realizando login"
    );
  }

  public void assignRealmRole(String userId, String roleName, String token) {
    String roleUrl = keycloakAdminUrl + "/roles/" + roleName;

    RoleRepresentationDTO role = doGet(
      roleUrl,
      token,
      RoleRepresentationDTO.class,
      "Buscando role no Keycloak"
    );

    List<RoleRepresentationDTO> roles = List.of(role);
    String assignUrl = keycloakAdminUrl + "/users/" + userId + "/role-mappings/realm";

    doPostWithToken(
      assignUrl,
      token,
      roles,
      MediaType.APPLICATION_JSON,
      Void.class,
      "Atribuindo role ao usuário"
    );
  }
}
