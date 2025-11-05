package br.org.apae.api.servicearea.domain.exceptions;

public class ServiceAreaConflictException extends RuntimeException {
    private static final String MESSAGE = "Área de atendimento já existe.";

    public ServiceAreaConflictException() {
        super(MESSAGE);
    }
}

