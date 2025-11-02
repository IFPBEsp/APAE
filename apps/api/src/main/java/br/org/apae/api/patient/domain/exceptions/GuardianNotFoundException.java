package br.org.apae.api.patient.domain.exceptions;

public class GuardianNotFoundException extends RuntimeException {
  private static final String MESSAGE = "Responsável não encontrado.";

  public GuardianNotFoundException() {
    super(MESSAGE);
  }
}
