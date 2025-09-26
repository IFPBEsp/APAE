package br.org.apae.api.auth.exceptions.types;

import br.org.apae.api.auth.exceptions.messages.ExceptionMessage;

public class AuthenticationException extends RuntimeException {

  public AuthenticationException(ExceptionMessage message) {
    super(message.getMessage());
  }

  public AuthenticationException(ExceptionMessage message, Throwable cause) {
    super(message.getMessage(), cause);
  }
}