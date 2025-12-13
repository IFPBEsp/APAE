package br.org.apae.api.controllers.auth;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import br.org.apae.api.auth.application.interfaces.AuthApplicationService;
import br.org.apae.api.auth.interfaces.controllers.AuthController;
import br.org.apae.api.common.dto.auth.request.SignInDTO;
import br.org.apae.api.common.dto.auth.request.SignUpDTO;
import br.org.apae.api.common.dto.auth.response.TokenResponseDTO;
import jakarta.validation.Valid;

@RestController
public class AuthControllerImpl implements AuthController {
  private final AuthApplicationService authService;

  public AuthControllerImpl(AuthApplicationService authService) {
    this.authService = authService;
  }

  @Override
  public ResponseEntity<Void> signUp(@Valid @RequestBody SignUpDTO signUpDto) {
    authService.signUp(signUpDto);
    return ResponseEntity.status(HttpStatus.CREATED).build();
  }

  @Override
  public ResponseEntity<TokenResponseDTO> signIn(@Valid @RequestBody SignInDTO signInDto) {
    TokenResponseDTO tokenResponse = authService.signIn(signInDto);
    return ResponseEntity.ok(tokenResponse);
  }
}
