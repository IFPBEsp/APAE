package br.org.apae.api.auth.domain.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class UserWithoutProductAccessException extends ResponseStatusException {
  private static final String DEFAULT_MESSAGE = "Usuário sem acesso ao APAE Geral.";

  public UserWithoutProductAccessException() {
    super(HttpStatus.FORBIDDEN, DEFAULT_MESSAGE);
  }

  public UserWithoutProductAccessException(String message) {
    super(HttpStatus.FORBIDDEN, message);
  }
}

