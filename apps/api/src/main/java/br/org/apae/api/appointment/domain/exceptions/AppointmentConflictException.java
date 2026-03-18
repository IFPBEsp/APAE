package br.org.apae.api.appointment.domain.exceptions;

public class AppointmentConflictException extends RuntimeException {
    private static final String MESSAGE = "Já existe um agendamento ativo para este profissional na data e horário solicitados.";

    public AppointmentConflictException() {
        super(MESSAGE);
    }
}
