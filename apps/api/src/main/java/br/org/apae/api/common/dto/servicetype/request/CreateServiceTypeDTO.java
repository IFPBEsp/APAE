package br.org.apae.api.common.dto.servicetype.request;

import jakarta.validation.constraints.NotBlank;

public record CreateServiceTypeDTO(
        @NotBlank(message = "A área é obrigatória.") String area) {
}

