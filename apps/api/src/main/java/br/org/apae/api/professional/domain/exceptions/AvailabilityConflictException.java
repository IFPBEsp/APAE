package br.org.apae.api.professional.domain.exceptions;

public class AvailabilityConflictException extends RuntimeException {
    public AvailabilityConflictException() {
        super("Já existe uma disponibilidade para este dia e turno");
    }
}
