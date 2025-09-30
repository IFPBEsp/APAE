package br.org.apae.api.auth.domain.exceptions;

public class UserNotFoundException extends RuntimeException {
  private static final String MESSAGE = "Usuário não encontrado.";

  public UserNotFoundException() {
    super(MESSAGE);
  }
}
