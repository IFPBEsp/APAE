package br.org.apae.api.patient.domain.exceptions;

public class DisorderMismatchException extends RuntimeException {
  private static final String MESSAGE = "Um ou mais transtornos não foram encontrados.";

  public DisorderMismatchException() {
    super(MESSAGE);
  }
}
