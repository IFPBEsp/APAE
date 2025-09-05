package br.org.apae.auth.application.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import br.org.apae.auth.api.dto.LoginRequestDTO;
import br.org.apae.auth.api.dto.SignUpRequestDTO;
import br.org.apae.auth.api.dto.TokenResponseDTO;
import br.org.apae.auth.api.dto.UserRepresentationDTO;
import br.org.apae.auth.application.service.exceptions.IncorrectLoginException;
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

  private UserRepresentationDTO getUserRepresentation(SignUpRequestDTO objDto, String firstName, String lastName) {
    UserRepresentationDTO user = new UserRepresentationDTO(
        null,
        objDto.cpf(),
        objDto.email(),
        firstName,
        lastName,
        true
    );
    return user;
  }

  @Override
  public void registerUser(SignUpRequestDTO objDto, String token) {
    objDto.validateAttributes();

    if (keycloakClient.userExistsByUsername(objDto.cpf(), token) || keycloakClient.userExistsByEmail(objDto.email(), token)) {
      throw new IllegalArgumentException("CPF or Email already exists");
    }

    String[] splitedName = objDto.fullName().trim().split(" ", 2);
    String firstName = splitedName[0];
    String lastName = splitedName.length > 1 ? splitedName[1] : "";

    UserRepresentationDTO user = getUserRepresentation(objDto, firstName, lastName);

    String userId = keycloakClient.createUser(user, objDto.password(), token);
    keycloakClient.assignRealmRole(userId, defaultRole, token);
  }

  @Override
  public TokenResponseDTO login(LoginRequestDTO login) {
    try {
      login.validateAttributes();
      return keycloakClient.getAccessToken(login.username(), login.password());
    } catch (Exception e) {
      throw new IncorrectLoginException("Username or password is incorrect.");
    }
  }
}