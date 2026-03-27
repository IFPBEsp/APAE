package br.org.apae.api.auth.interfaces.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import br.org.apae.api.common.dto.auth.request.PasswordRecoveryRequestDTO;
import br.org.apae.api.common.dto.auth.request.PasswordResetDTO;
import br.org.apae.api.common.dto.auth.request.SignInDTO;
import br.org.apae.api.common.dto.auth.request.SignUpDTO;
import br.org.apae.api.common.dto.auth.response.MessageResponseDTO;
import br.org.apae.api.common.dto.auth.response.TokenResponseDTO;
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

  @Operation(summary = "Solicita recuperação de senha", description = "Recebe o e-mail do usuário e inicia o fluxo de recuperação.")
  @ApiResponse(responseCode = "200", description = "Solicitação processada com sucesso")
  @ApiResponse(responseCode = "400", description = "Dados inválidos")
  @PostMapping("/password-recovery/request")
  ResponseEntity<MessageResponseDTO> requestPasswordRecovery(
      @Valid @RequestBody PasswordRecoveryRequestDTO dto);

  @Operation(summary = "Redefine a senha", description = "Valida o token de recuperação e redefine a senha do usuário.")
  @ApiResponse(responseCode = "200", description = "Senha redefinida com sucesso")
  @ApiResponse(responseCode = "400", description = "Token inválido ou senha inválida")
  @PostMapping("/password-recovery/reset")
  ResponseEntity<MessageResponseDTO> resetPassword(
      @Valid @RequestBody PasswordResetDTO dto);
}