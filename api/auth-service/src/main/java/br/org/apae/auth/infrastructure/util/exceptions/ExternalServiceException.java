package br.org.apae.auth.infrastructure.util.exceptions;

public class ExternalServiceException extends RuntimeException {
  public ExternalServiceException(String message) {
    super(message);
  }
}
