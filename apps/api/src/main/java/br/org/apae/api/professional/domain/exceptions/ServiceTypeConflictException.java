package br.org.apae.api.professional.domain.exceptions;

public class ServiceTypeConflictException extends RuntimeException {
    private static final String MESSAGE = "Tipo de atendimento já existe.";

    public ServiceTypeConflictException() {
        super(MESSAGE);
    }
}
