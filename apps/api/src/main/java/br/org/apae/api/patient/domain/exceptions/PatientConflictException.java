package br.org.apae.api.patient.domain.exceptions;

public class PatientConflictException extends RuntimeException {
  private static final String MESSAGE = "Paciente já cadastrado.";

  public PatientConflictException() {
    super(MESSAGE);
  }
}
