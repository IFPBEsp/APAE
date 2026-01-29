package br.org.apae.api.patient.domain.exceptions;

public class VaccineMismatchException extends RuntimeException {
  private static final String MESSAGE = "Uma ou mais vacinas não foram encontradas.";

  public VaccineMismatchException() {
    super(MESSAGE);
  }
}
