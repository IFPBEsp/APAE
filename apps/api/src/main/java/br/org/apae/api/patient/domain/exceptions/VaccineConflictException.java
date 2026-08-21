package br.org.apae.api.patient.domain.exceptions;

public class VaccineConflictException extends RuntimeException {
    private static final String MESSAGE = "Transtorno já existe.";

    public VaccineConflictException() {
        super(MESSAGE);
    }

    public VaccineConflictException(String name) {
        super("Já existe um transtorno com o nome: " + name);
    }
}