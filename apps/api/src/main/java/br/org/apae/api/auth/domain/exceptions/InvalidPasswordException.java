package br.org.apae.api.auth.domain.exceptions;

public class InvalidPasswordException extends RuntimeException {
  private static final String MESSAGE = "Senha incorreta.";

  public InvalidPasswordException() {
    super(MESSAGE);
  }
}
