package br.org.apae.auth.infrastructure.client;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import br.org.apae.auth.api.dto.RoleRepresentationDTO;
import br.org.apae.auth.api.dto.UserRepresentationDTO;
import br.org.apae.auth.infrastructure.util.RestTemplateExecutor;

@Component
public class KeycloakAdminClient {
  private RestTemplate restTemplate;
  private final String keycloakAdminUrl;
  private final String urlLoginToken;
  private final String clientId;
  private final String clientSecret;

  public KeycloakAdminClient(
      RestTemplate restTemplate,
      @Value("${keycloak_admin_url}") String keycloakAdminUrl,
      @Value("${client_id}") String clientId,
      @Value("${client_secret}") String clientSecret,
      @Value("${url_login_token}") String urlLoginToken) {
    this.restTemplate = restTemplate;
    this.keycloakAdminUrl = keycloakAdminUrl;
    this.urlLoginToken = urlLoginToken;
    this.clientId = clientId;
    this.clientSecret = clientSecret;
  }

  public boolean userExistsByUsername(String username, String token) {
    HttpHeaders headers = getHeadersWithToken(token);

    String url = String.format("%s/users?username=%s", keycloakAdminUrl, username);
    ResponseEntity<UserRepresentationDTO[]> response = RestTemplateExecutor.execute(() -> {
      return restTemplate.exchange(
        url,
        HttpMethod.GET,
        new HttpEntity<>(headers),
        UserRepresentationDTO[].class
      );
    }, "Buscando usuário por username"); 

    return response.getBody() != null && response.getBody().length > 0;
  }

  public boolean userExistsByEmail(String email, String token) {
    HttpHeaders headers = getHeadersWithToken(token);

    String url = String.format("%s/users?email=%s", keycloakAdminUrl, email);
    ResponseEntity<UserRepresentationDTO[]> response = RestTemplateExecutor.execute(() -> {
      return restTemplate.exchange(
        url,
        HttpMethod.GET,
        new HttpEntity<>(headers),
        UserRepresentationDTO[].class
      );
    }, "Buscando usuário por email"); 

    return response.getBody() != null && response.getBody().length > 0;
  }

  public String createUser(UserRepresentationDTO user, String password, String token) {
    String createUserUrl = keycloakAdminUrl + "/users";
    HttpHeaders headers = getHeadersWithToken(token);

    RestTemplateExecutor.execute(() -> {
      restTemplate.exchange(
        createUserUrl,
        HttpMethod.POST,
        new HttpEntity<>(user, headers),
        Void.class
      );
    },
    "Criando usuário no Keycloak"
    );

    String url = keycloakAdminUrl + "/users" + "?username=" + user.username();
    ResponseEntity<UserRepresentationDTO[]> response = RestTemplateExecutor.execute(() ->
      restTemplate.exchange(
        url, 
        HttpMethod.GET,
        new HttpEntity<>(headers),
        UserRepresentationDTO[].class
      ),
      "Buscar usuário por username"
    );

    String userId = response.getBody()[0].id();

    Map<String, Object> credentials = Map.of(
        "type", "password",
        "value", password,
        "temporary", false
    );

    RestTemplateExecutor.execute(() -> {
      restTemplate.exchange(
          keycloakAdminUrl + "/users/" + userId + "/reset-password",
          HttpMethod.PUT,
          new HttpEntity<>(credentials, headers),
          Void.class);
    }, "Definindo senha do usuário");

    return userId;
  }

  public String getAccessToken(String username, String password) {
    HttpHeaders headers = new HttpHeaders();

    headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

    MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
    map.add("grant_type", "password");
    map.add("username", username);
    map.add("password", password);
    map.add("client_id", clientId);
    map.add("client_secret", clientSecret);

    HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);

    ResponseEntity<String> response = RestTemplateExecutor.execute(() -> 
      restTemplate.postForEntity(
        urlLoginToken,
        request,
        String.class
      ), "Realizando login"
    );

    return response.getBody();
  }

  public void assignRealmRole(String userId, String roleName, String token) {
    HttpHeaders headers = getHeadersWithToken(token);

    String roleUrl = keycloakAdminUrl + "/roles/" + roleName.toLowerCase();

    ResponseEntity<RoleRepresentationDTO> roleResponse = RestTemplateExecutor.execute(() -> 
      restTemplate.exchange(
        roleUrl,
        HttpMethod.GET,
        new HttpEntity<>(headers),
        RoleRepresentationDTO.class), "Buscando role no Keycloak"
    );
    
    RoleRepresentationDTO role = roleResponse.getBody();
    List<RoleRepresentationDTO> roles = List.of(role);

    String assignUrl = keycloakAdminUrl + "/users" + "/" + userId + "/role-mappings/realm";

    RestTemplateExecutor.execute(() -> 
      restTemplate.exchange(
        assignUrl,
        HttpMethod.POST,
        new HttpEntity<>(roles, headers),
        Void.class), "Atribuindo role ao usuário");
  }
  private HttpHeaders getHeadersWithToken(String token) {
    HttpHeaders headers = new HttpHeaders();
    headers.setBearerAuth(token);
    headers.setContentType(MediaType.APPLICATION_JSON);
    return headers;
  }
}
