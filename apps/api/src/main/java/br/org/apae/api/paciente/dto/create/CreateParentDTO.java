package br.org.apae.api.paciente.dto.create;

import jakarta.validation.constraints.NotBlank;

public record CreateParentDTO(
        @NotBlank(message = "O nome não pode estar em branco")
        String name,

        String rg,

        String cpf,

        String profession,

        @NotBlank(message = "O parentesco não pode estar em branco")
        String kinship
) {

}