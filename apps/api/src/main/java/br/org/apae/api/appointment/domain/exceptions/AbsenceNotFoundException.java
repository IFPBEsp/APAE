package br.org.apae.api.appointment.domain.exceptions;

public class AbsenceNotFoundException extends RuntimeException {
    private static final String MESSAGE = "Falta não encontrada.";

    public AbsenceNotFoundException() {
        super(MESSAGE);
    }


}