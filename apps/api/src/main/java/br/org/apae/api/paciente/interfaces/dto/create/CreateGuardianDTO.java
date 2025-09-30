package br.org.apae.api.paciente.interfaces.dto.create;

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

