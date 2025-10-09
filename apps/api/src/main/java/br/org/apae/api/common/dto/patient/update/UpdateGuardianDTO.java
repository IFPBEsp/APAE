package br.org.apae.api.common.dto.patient.update;

import jakarta.validation.constraints.NotBlank;

public record UpdateGuardianDTO(
        @NotBlank(message = "O campo nome é obrigatório.") String name,
        @NotBlank(message = "O campo contato é obrigatório.") String contact,
        @NotBlank(message = "O campo parentesco é obrigatório.") String kinship) {
}
