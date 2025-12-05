package br.org.apae.api.appointment.domain.exceptions;

public class ConflictException extends RuntimeException {
  public ConflictException() {
    super("O profissional já possui agendamento nesta hora.");
  }
}
