package br.org.apae.api.appointment.domain.exceptions;

public class AppointmentNotFoundException extends RuntimeException {
  private static final String MESSAGE = "Agendamento não encontrado.";

  public AppointmentNotFoundException() {
    super(MESSAGE);
  }
}
