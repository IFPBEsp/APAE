package br.org.apae.api.auth.exceptions.types;

import br.org.apae.api.auth.exceptions.messages.ExceptionMessage;

public class UserNotFoundException extends RuntimeException {
  public UserNotFoundException() {
    super(ExceptionMessage.USER_NOT_FOUND.getMessage());
  }

  public UserNotFoundException(String message) {
    super(message);
  }
}