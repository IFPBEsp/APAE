package br.org.apae.api.auth.exceptions.types;

import br.org.apae.api.auth.exceptions.messages.ExceptionMessage;

public class InvalidCredentialsException extends RuntimeException {
  public InvalidCredentialsException() {
    super(ExceptionMessage.INVALID_CREDENTIALS.getMessage());
  }

  public InvalidCredentialsException(String message) {
    super(message);
  }
}