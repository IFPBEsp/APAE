package br.org.apae.api.professional.domain.exceptions;

public class EmailConflictException extends RuntimeException {
  private static final String MESSAGE = "Email já cadastrado.";
    public EmailConflictException() {
        super(MESSAGE);
    }
}
