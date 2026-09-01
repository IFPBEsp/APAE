package br.org.apae.api.patient.domain.exceptions;

public class VaccineConflictException extends RuntimeException {
    public VaccineConflictException(String message) {
        super(message);
    }

    public VaccineConflictException() {
        super("Já existe uma vacina cadastrada com este nome.");
    }
}