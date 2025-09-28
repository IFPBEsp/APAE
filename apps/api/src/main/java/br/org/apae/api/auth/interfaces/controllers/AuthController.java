package br.org.apae.api.auth.interfaces.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import br.org.apae.api.auth.interfaces.dto.SignInDTO;
import br.org.apae.api.auth.interfaces.dto.SignUpDTO;
import br.org.apae.api.auth.interfaces.dto.TokenResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;

@RequestMapping("/auth")
public interface AuthController {
  @Operation(summary = "Registra um novo usuário", description = "Cria um novo usuário no sistema com username e senha.")
  @ApiResponse(responseCode = "201", description = "Usuário criado com sucesso")
  @ApiResponse(responseCode = "400", description = "Dados inválidos")
  @PostMapping("/signup")
  ResponseEntity<Void> signUp(@Valid @RequestBody SignUpDTO signUpDto);

  @Operation(summary = "Realiza login", description = "Autentica um usuário existente e retorna um token JWT.")
  @ApiResponse(responseCode = "200", description = "Login bem-sucedido")
  @ApiResponse(responseCode = "401", description = "Credenciais inválidas")
  @PostMapping("/signin")
  ResponseEntity<TokenResponseDTO> signIn(@Valid @RequestBody SignInDTO signInDto);
}
