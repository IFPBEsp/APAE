package br.org.apae.api.auth.domain.exceptions;

public class TokenVerificationException extends RuntimeException {
  private static final String MESSAGE = "Erro na validação do token.";

  public TokenVerificationException() {
    super(MESSAGE);
  }

  public TokenVerificationException(Throwable cause) {
    super(MESSAGE, cause);
  }
}
