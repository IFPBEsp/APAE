package br.org.apae.api.patient.domain.exceptions;

public class VaccineConflictException extends RuntimeException {
    private static final String MESSAGE = "Vacina já existe.";

    public VaccineConflictException() {
        super(MESSAGE);
    }

    public VaccineConflictException(String name) {
        super("Já existe uma vacina com o nome: " + name);
    }
}