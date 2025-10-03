package br.org.apae.api.common.dto.paciente.dto.create;

import jakarta.validation.constraints.NotBlank;

public record CreateGuardianDTO(
        @NotBlank(message = "O nome não pode estar em branco")
        String name,

        @NotBlank(message = "O contato não pode estar em branco")
        String contact,

        @NotBlank(message = "O parentesco não pode estar em branco")
        String kinship
) {

}

