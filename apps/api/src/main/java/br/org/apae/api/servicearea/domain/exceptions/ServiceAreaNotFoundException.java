package br.org.apae.api.servicearea.domain.exceptions;

public class ServiceAreaNotFoundException extends RuntimeException {
    private static final String MESSAGE = "Área de atendimento não encontrada.";

    public ServiceAreaNotFoundException() {
        super(MESSAGE);
    }
}

