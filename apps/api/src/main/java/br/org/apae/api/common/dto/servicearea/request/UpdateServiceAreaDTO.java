package br.org.apae.api.common.dto.servicearea.request;

import jakarta.validation.constraints.NotBlank;

public record UpdateServiceAreaDTO(
        @NotBlank(message = "A área é obrigatória.") String area) {
}

