package br.org.apae.api.auth.domain.exceptions;

public class AuthenticationException extends RuntimeException {
  private static final String DEFAULT_MESSAGE = "Falha na autenticação.";

  public AuthenticationException() {
    super(DEFAULT_MESSAGE);
  }

  public AuthenticationException(String message) {
    super(message);
  }

  public AuthenticationException(Throwable cause) {
    super(DEFAULT_MESSAGE, cause);
  }

  public AuthenticationException(String message, Throwable cause) {
    super(message, cause);
  }
}