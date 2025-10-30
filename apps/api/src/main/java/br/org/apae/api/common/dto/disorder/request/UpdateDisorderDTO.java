package br.org.apae.api.common.dto.disorder.request;

import jakarta.validation.constraints.Size;

public record UpdateDisorderDTO(
        @Size(max = 100, message = "O nome deve ter no máximo 100 caracteres.")
        String name
) {
}
