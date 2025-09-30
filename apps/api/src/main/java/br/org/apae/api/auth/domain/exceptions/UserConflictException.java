package br.org.apae.api.auth.domain.exceptions;

public class UserConflictException extends RuntimeException {
  private static final String MESSAGE = "Usuário já cadastrado com este username.";

  public UserConflictException() {
    super(MESSAGE);
  }
}
