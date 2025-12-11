package br.org.apae.api.common.dto.availability.request;

import jakarta.validation.constraints.NotBlank;

public record UpdateAvailabilityDTO(
        @NotBlank(message = "O dia não pode estar em branco")
        String day,

        @NotBlank(message = "O turno não pode estar em branco")
        String shift
) {}
