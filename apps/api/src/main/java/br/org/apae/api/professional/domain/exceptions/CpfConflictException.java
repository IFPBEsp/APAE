package br.org.apae.api.professional.domain.exceptions;

public class CpfConflictException extends RuntimeException {
    private static final String MESSAGE = "CPF já cadastrado.";

    public CpfConflictException() {
        super(MESSAGE);
    }
}
