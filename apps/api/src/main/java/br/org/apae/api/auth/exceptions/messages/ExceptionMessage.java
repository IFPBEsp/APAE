package br.org.apae.api.auth.exceptions.messages;

public enum ExceptionMessage {
  USER_NOT_FOUND("Usuário não encontrado"),
  INVALID_CREDENTIALS("Credenciais inválidas"),
  TOKEN_GENERATION_ERROR("Erro ao gerar token JWT"),
  AUTHENTICATION_FAILED("Falha na autenticação"),
  ACCESS_DENIED("Acesso negado");

  private final String message;

  ExceptionMessage(String message) {
    this.message = message;
  }

  public String getMessage() {
    return message;
  }
}
