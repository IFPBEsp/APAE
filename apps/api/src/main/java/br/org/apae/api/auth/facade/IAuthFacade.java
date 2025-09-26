package br.org.apae.api.auth.facade;

import br.org.apae.api.auth.dto.SignInDTO;
import br.org.apae.api.auth.dto.SignUpDTO;
import br.org.apae.api.auth.dto.TokenResponseDTO;

public interface IAuthFacade {

  /**
   * Registra um novo usuário no sistema.
   * <p>
   * Realiza o hash da senha recebida e persiste os dados do usuário.
   *
   * @param signUpRequest objeto contendo username e password.
   */
  void signUp(SignUpDTO signUpRequest);

  /**
   * Autentica um usuário existente.
   * <p>
   * Valida as credenciais fornecidas, gera um token JWT e retorna
   * uma resposta contendo o token.
   *
   * @param signInRequest objeto contendo username e password.
   * @return {TokenResponseDTO} com o token JWT gerado.
   */
  TokenResponseDTO signIn(SignInDTO signInRequest);
}
