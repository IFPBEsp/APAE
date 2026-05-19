package br.org.apae.api.patient.domain.exceptions;

public class ParentNotFoundException extends RuntimeException {
  private static final String MESSAGE = "Parente não encontrado.";

  public ParentNotFoundException() {
    super(MESSAGE);
  }
}
