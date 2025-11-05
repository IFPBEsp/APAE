package br.org.apae.api.patient.domain.exceptions;

public class DisorderConflictException extends RuntimeException {
    private static final String MESSAGE = "Transtorno já existe.";

    public DisorderConflictException() {
        super(MESSAGE);
    }

    public DisorderConflictException(String name) {
        super("Já existe um transtorno com o nome: " + name);
    }
}