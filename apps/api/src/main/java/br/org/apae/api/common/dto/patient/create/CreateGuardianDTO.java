package br.org.apae.api.common.dto.patient.create;

import br.org.apae.api.common.dto.address.CreateAddressDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateGuardianDTO(
        @NotBlank(message = "O nome não pode estar em branco")
        String name,

        @NotBlank(message = "O contato não pode estar em branco")
        String contact,

        @NotBlank(message = "O parentesco não pode estar em branco")
        String kinship,

        @NotNull(message = "O endereço do responsável é obrigatório.")
        @Valid
        CreateAddressDTO address
) {

}

