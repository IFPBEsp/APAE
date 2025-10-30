package br.org.apae.api.appointment.domain.exceptions;

public class ConsultationHistoryConflictException extends RuntimeException {
  private static final String MESSAGE = "Consulta com agendamento, data e horário já existe.";

  public ConsultationHistoryConflictException() {
    super(MESSAGE);
  }
}
