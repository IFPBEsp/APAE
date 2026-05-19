package br.org.apae.api.auth.application.interfaces;

import br.org.apae.api.common.dto.auth.request.PasswordRecoveryRequestDTO;
import br.org.apae.api.common.dto.auth.request.PasswordResetDTO;
import br.org.apae.api.common.dto.auth.request.SignInDTO;
import br.org.apae.api.common.dto.auth.request.SignUpDTO;
import br.org.apae.api.common.dto.auth.response.TokenResponseDTO;

public interface AuthApplicationService {
  void signUp(SignUpDTO signUpDto);

  TokenResponseDTO signIn(SignInDTO signInDto);

  void requestPasswordRecovery(PasswordRecoveryRequestDTO dto);

  void resetPassword(PasswordResetDTO dto);
}
