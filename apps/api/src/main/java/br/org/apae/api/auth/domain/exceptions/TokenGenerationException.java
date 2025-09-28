package br.org.apae.api.auth.domain.exceptions;

public class TokenGenerationException extends RuntimeException {
  private static final String MESSAGE = "Erro na criação do token.";

  public TokenGenerationException() {
    super(MESSAGE);
  }

  public TokenGenerationException(Throwable cause) {
    super(MESSAGE, cause);
  }
}
