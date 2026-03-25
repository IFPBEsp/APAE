package br.org.apae.api.notification.domain.exceptions;

public class EmailSendingException extends RuntimeException {
  private static final String DEFAULT_MESSAGE = "Erro ao enviar e-mail.";

  public EmailSendingException() {
    super(DEFAULT_MESSAGE);
  }

  public EmailSendingException(String message) {
    super(message);
  }

  public EmailSendingException(Throwable cause) {
    super(DEFAULT_MESSAGE, cause);
  }

  public EmailSendingException(String message, Throwable cause) {
    super(message, cause);
  }
}