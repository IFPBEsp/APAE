package br.org.apae.api.patient.domain.exceptions;

import java.time.Year;

public class AnnualRegistryConflictException extends RuntimeException {
  private static final String MESSAGE = "Registro anual já existe para este paciente no ano %d.";

  public AnnualRegistryConflictException(Year year) {
    super(String.format(MESSAGE, year));
  }
}
