package br.org.apae.api.auth.exceptions.types;

import br.org.apae.api.auth.exceptions.messages.ExceptionMessage;

public class TokenGenerationException extends RuntimeException {
  public TokenGenerationException() {
    super(ExceptionMessage.TOKEN_GENERATION_ERROR.getMessage());
  }

  public TokenGenerationException(String message, Throwable cause) {
    super(message, cause);
  }

  public TokenGenerationException(Throwable cause) {
    super(ExceptionMessage.TOKEN_GENERATION_ERROR.getMessage(), cause);
  }
}