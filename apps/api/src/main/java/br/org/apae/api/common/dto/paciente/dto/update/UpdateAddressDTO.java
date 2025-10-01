package br.org.apae.api.common.dto.paciente.dto.update;

import jakarta.validation.constraints.NotBlank;

public record UpdateAddressDTO(
        @NotBlank(message = "O campo cidade é obrigatório.") String city,
        @NotBlank(message = "O campo CEP é obrigatório.") String zipCode,
        @NotBlank(message = "O campo estado é obrigatório.") String state,
        @NotBlank(message = "O campo bairro é obrigatório.") String neighborhood,
        @NotBlank(message = "O campo rua é obrigatório.") String street,
        @NotBlank(message = "O campo número é obrigatório.") String number,
        String complement) {
}
