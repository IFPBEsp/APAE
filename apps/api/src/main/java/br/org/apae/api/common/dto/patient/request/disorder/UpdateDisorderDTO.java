package br.org.apae.api.common.dto.patient.request.disorder;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UpdateDisorderDTO(
                @NotBlank(message = "O nome é obrigatório.")
                @Size(max = 100, message = "O nome deve ter no máximo 100 caracteres.") String name) {
}
