package br.org.apae.api.patient.domain.exceptions;

public class AnnualRegistryConflictException extends RuntimeException {
  private static final String MESSAGE = "Registro anual já existe para este paciente no ano %d.";

  public AnnualRegistryConflictException(Integer year) {
    super(String.format(MESSAGE, year));
  }
}
