package br.org.apae.api.paciente.interfaces.dto.update;

import jakarta.validation.constraints.NotBlank;

public record UpdateParentDTO(
        @NotBlank(message = "O campo nome é obrigatório.") String name,
        String rg,
        String cpf,
        String profession,
        @NotBlank(message = "O campo parentesco é obrigatório.") String kinship) {
}
