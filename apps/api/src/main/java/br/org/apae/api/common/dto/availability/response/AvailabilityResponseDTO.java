package br.org.apae.api.common.dto.availability.response;

import br.org.apae.api.professional.domain.model.Availability;

import java.util.UUID;


public record AvailabilityResponseDTO(
        UUID id,
        String day,
        String shift
) {
    public AvailabilityResponseDTO(Availability entity) {
        this(
                entity.getId(),
                entity.getDay().name(),
                entity.getShift().name()
        );
    }
}
