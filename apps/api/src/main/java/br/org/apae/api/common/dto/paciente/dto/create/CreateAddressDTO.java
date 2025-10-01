package br.org.apae.api.common.dto.paciente.dto.create;

import jakarta.validation.constraints.NotBlank;

public record CreateAddressDTO(
        @NotBlank(message = "A cidade não pode estar em branco")
        String city,

        @NotBlank(message = "O CEP não pode estar em branco")
        String zipCode,

        @NotBlank(message = "O estado não pode estar em branco")
        String state,

        @NotBlank(message = "O bairro não pode estar em branco")
        String neighborhood,

        @NotBlank(message = "A rua não pode estar em branco")
        String street,

        @NotBlank(message = "O número não pode estar em branco")
        String number,

        String complement
) {

}
