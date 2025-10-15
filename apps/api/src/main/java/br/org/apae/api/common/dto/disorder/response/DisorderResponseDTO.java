package br.org.apae.api.common.dto.disorder.response;

import java.util.UUID;

public record DisorderResponseDTO(
        UUID id,
        String name,
        String description
) {
}
