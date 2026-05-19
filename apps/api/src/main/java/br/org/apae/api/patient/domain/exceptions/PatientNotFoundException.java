package br.org.apae.api.patient.domain.exceptions;

public class PatientNotFoundException extends RuntimeException {
    private static final String MESSAGE = "Paciente não encontrado.";

    public PatientNotFoundException() {
        super(MESSAGE);
    }
}
