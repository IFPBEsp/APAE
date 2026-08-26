package br.org.apae.api.auth.domain.exceptions;

public class InvalidPasswordException extends RuntimeException {

  public InvalidPasswordException() {
    super("E-mail ou senha incorretos");
  }
}
