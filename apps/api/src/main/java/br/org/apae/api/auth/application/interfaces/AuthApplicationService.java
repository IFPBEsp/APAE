package br.org.apae.api.auth.application.interfaces;

import br.org.apae.api.common.dto.auth.dto.SignInDTO;
import br.org.apae.api.common.dto.auth.dto.TokenResponseDTO;
import jakarta.validation.Valid;

public interface AuthApplicationService {
  void signUp(br.org.apae.api.common.dto.auth.dto.@Valid SignUpDTO signUpDto);

  TokenResponseDTO signIn(SignInDTO signInDto);
}
