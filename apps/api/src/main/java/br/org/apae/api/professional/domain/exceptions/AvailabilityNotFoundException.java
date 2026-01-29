package br.org.apae.api.professional.domain.exceptions;

public class AvailabilityNotFoundException extends RuntimeException {
    public AvailabilityNotFoundException() {
        super("Disponibilidade não encontrada");
    }
}
