package br.org.apae.api.auth.application.interfaces;

import br.org.apae.api.common.dto.auth.dto.SignInDTO;
import br.org.apae.api.common.dto.auth.dto.SignUpDTO;
import br.org.apae.api.common.dto.auth.dto.TokenResponseDTO;

public interface AuthApplicationService {
  void signUp(SignUpDTO signUpDto);

  TokenResponseDTO signIn(SignInDTO signInDto);
}
