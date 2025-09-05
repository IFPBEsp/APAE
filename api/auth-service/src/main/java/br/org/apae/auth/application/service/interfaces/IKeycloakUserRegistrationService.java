package br.org.apae.auth.application.service.interfaces;

import br.org.apae.auth.api.dto.LoginRequestDTO;
import br.org.apae.auth.api.dto.SignUpRequestDTO;
import br.org.apae.auth.api.dto.TokenResponseDTO;

public interface IKeycloakUserRegistrationService {
  void registerUser(SignUpRequestDTO signUpRequestDto, String token);

  TokenResponseDTO login(LoginRequestDTO loginRequestDto);
}
