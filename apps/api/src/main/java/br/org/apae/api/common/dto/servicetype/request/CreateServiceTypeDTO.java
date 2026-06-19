package br.org.apae.api.common.dto.servicetype.request;

import jakarta.validation.constraints.NotBlank;

public record CreateServiceTypeDTO(
        @NotBlank(message = "O nome é obrigatório.") String name) {
}

