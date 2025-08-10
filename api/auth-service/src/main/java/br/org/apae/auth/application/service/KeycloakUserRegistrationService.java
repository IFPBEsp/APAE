package br.org.apae.auth.application.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import br.org.apae.auth.api.dto.UserRepresentationDTO;
import br.org.apae.auth.application.service.interfaces.IKeycloakUserRegistrationService;
import br.org.apae.auth.infrastructure.client.KeycloakAdminClient;

@Service
public class KeycloakUserRegistrationService implements IKeycloakUserRegistrationService {

  private final KeycloakAdminClient keycloakClient;
  @Value("${default_role}")
  private String defaultRole;

  @Autowired
  public KeycloakUserRegistrationService(KeycloakAdminClient keycloakClient) {
    this.keycloakClient = keycloakClient;
  }

  @Override
  public void registerUser(String cpf, String password, String email, String fullName, String token) {
    validateAttributes(cpf, password, email, fullName);

    if (keycloakClient.userExistsByUsername(cpf, token) || keycloakClient.userExistsByEmail(email, token)) {
      throw new IllegalArgumentException("CPF or Email already exists");
    }

    String firstName = fullName.split(" ")[0];
    String lastName = fullName.substring(firstName.length()).trim();

    UserRepresentationDTO user = new UserRepresentationDTO(
        null,
        cpf,
        email,
        firstName,
        lastName,
        true);

    String userId = keycloakClient.createUser(user, password, token);
    keycloakClient.assignRealmRole(userId, defaultRole, token);
  }

  @Override
  public String login(String username, String password) {
    try {
      return keycloakClient.getAccessToken(username, password);
    } catch (Exception e) {
      throw new IllegalArgumentException("Username or password is incorrect.");
    }
  }


  private void validateAttributes(String cpf, String password, String email, String fullName) {
    if (cpf == null || cpf.isBlank())
      throw new IllegalArgumentException("CPF cannot be empty");

    if (password == null || password.isBlank() || password.length() < 8) {
      throw new IllegalArgumentException("Password must be at least 8 characters");
    }
    if (email == null || email.isBlank())
      throw new IllegalArgumentException("Email cannot be empty");

    if (fullName == null || fullName.isBlank())
      throw new IllegalArgumentException("Full name cannot be empty");
  }
}