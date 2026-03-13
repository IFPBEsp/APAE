package br.org.apae.api.patient.domain.exceptions;

public class ParentMismatchException extends RuntimeException {
  private static final String MESSAGE = "Um ou mais parente não foi encontrado.";

  public ParentMismatchException() {
    super(MESSAGE);
  }
}
