package br.org.apae.api.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import br.org.apae.api.auth.dto.SignInDTO;
import br.org.apae.api.auth.dto.SignUpDTO;
import br.org.apae.api.auth.dto.TokenResponseDTO;
import br.org.apae.api.auth.facade.IAuthFacade;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/auth")
public class AuthController {
  private final IAuthFacade authService;

  public AuthController(IAuthFacade authService) {
    this.authService = authService;
  }

  @PostMapping("/signup")
  public ResponseEntity<Void> register(@Valid @RequestBody SignUpDTO signUpDto) {
    authService.signUp(signUpDto);
    return ResponseEntity.status(HttpStatus.CREATED).build();
  }

  @PostMapping("/signin")
  public ResponseEntity<TokenResponseDTO> login(@Valid @RequestBody SignInDTO signInDto) {
    TokenResponseDTO tokenResponse = authService.signIn(signInDto);
    return ResponseEntity.ok(tokenResponse);
  }
}
