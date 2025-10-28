package br.org.apae.api.patient.domain.exceptions;

public class DisorderMismatchException extends RuntimeException {
  private static final String MESSAGE = "Uma ou mais doenças não foram encontradas.";

  public DisorderMismatchException() {
    super(MESSAGE);
  }
}
