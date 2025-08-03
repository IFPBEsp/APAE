package br.org.apae.auth.application.service.interfaces;

public interface IKeycloakUserRegistrationService {
  void registerUser(String cpf, String password, String email, String fullName, String token);

  String login(String username, String password);
}
