package br.org.apae.api.appointment.domain.exceptions;

public class ConsultationHistoryBadRequestException extends RuntimeException {
  private static final String MESSAGE = "Justificativa é obrigatória para consultas não realizadas.";

  public ConsultationHistoryBadRequestException() {
    super(MESSAGE);
  }
}
