package br.org.apae.api.common.dto.paciente.dto.create;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateParentDTO(
        @NotBlank(message = "O nome não pode estar em branco")
        String name,

        String rg,

        String cpf,

        String profession,

        @NotNull(message = "O campo 'vivo' é obrigatório")
        Boolean isAlive,

        @NotBlank(message = "O parentesco não pode estar em branco")
        String kinship
) {

}