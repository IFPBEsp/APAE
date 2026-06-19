package br.org.apae.api.common.dto.servicetype.request;

import jakarta.validation.constraints.NotBlank;

public record UpdateServiceTypeDTO(
        @NotBlank(message = "O nome é obrigatório.") String name) {
}

