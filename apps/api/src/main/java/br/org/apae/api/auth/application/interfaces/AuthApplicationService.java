package br.org.apae.api.auth.application.interfaces;

import br.org.apae.api.auth.interfaces.dto.SignInDTO;
import br.org.apae.api.auth.interfaces.dto.SignUpDTO;
import br.org.apae.api.auth.interfaces.dto.TokenResponseDTO;

public interface AuthApplicationService {
  void signUp(SignUpDTO signUpDto);

  TokenResponseDTO signIn(SignInDTO signInDto);
}
