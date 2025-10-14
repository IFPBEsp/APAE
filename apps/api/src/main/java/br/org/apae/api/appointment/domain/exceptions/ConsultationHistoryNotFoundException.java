package br.org.apae.api.appointment.domain.exceptions;

public class ConsultationHistoryNotFoundException extends RuntimeException {
  private static final String MESSAGE = "Histórico de consulta não encontrado.";

  public ConsultationHistoryNotFoundException() {
    super(MESSAGE);
  }
}
