package br.org.apae.api.professional.domain.exceptions;

public class ServiceTypeNotFoundException extends RuntimeException {
    private static final String MESSAGE = "Tipo de atendimento não encontrado.";

    public ServiceTypeNotFoundException() {
        super(MESSAGE);
    }
}
