package br.org.apae.api.appointment.domain.exceptions;

public class AppointmentAlreadyCancelledException extends RuntimeException {
  public AppointmentAlreadyCancelledException() {
    super("Appointment already cancelled");
  }
}
