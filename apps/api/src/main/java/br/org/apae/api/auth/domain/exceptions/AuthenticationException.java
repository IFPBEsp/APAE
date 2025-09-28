package br.org.apae.api.auth.domain.exceptions;

public class AuthenticationException extends RuntimeException {
  private static final String MESSAGE = "Falha na autenticação.";

  public AuthenticationException() {
    super(MESSAGE);
  }

  public AuthenticationException(Throwable cause) {
    super(MESSAGE, cause);
  }
}
