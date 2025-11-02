package br.org.apae.api.patient.domain.exceptions;

public class DisorderNotFoundException extends RuntimeException {
  private static final String MESSAGE = "Transtorno não encontrado.";

  public DisorderNotFoundException() {
    super(MESSAGE);
  }
}
