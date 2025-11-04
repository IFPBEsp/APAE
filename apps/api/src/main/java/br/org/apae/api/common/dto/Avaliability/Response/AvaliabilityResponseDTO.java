package br.org.apae.api.common.dto.Avaliability.Response;

import br.org.apae.api.professional.domain.model.Avaliability;

import java.util.UUID;


public record AvaliabilityResponseDTO(
        UUID id,
        String day,
        String shift
) {
    public AvaliabilityResponseDTO(br.org.apae.api.professional.domain.model.Avaliability entity) {
        this(
                entity.getId(),
                entity.getDay().name(),
                entity.getShift().name()
        );
    }
}
