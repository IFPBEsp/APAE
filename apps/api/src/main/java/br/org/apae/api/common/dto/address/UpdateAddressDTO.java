package br.org.apae.api.common.dto.address;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UpdateAddressDTO(
    @NotBlank(message = "A cidade não pode estar em branco") String city,

    @NotBlank(message = "O CEP não pode estar em branco") @Pattern(regexp = "^\\d{5}-\\d{3}$", message = "O CEP deve estar no formato 99999-999") String cep,

    @NotBlank(message = "O estado não pode estar em branco") String state,

    @NotBlank(message = "O bairro não pode estar em branco") @Size(min = 2, max = 60, message = "O bairro deve ter entre 2 e 60 caracteres") @Pattern(regexp = "^[A-Za-zÀ-ÖØ-öø-ÿ0-9\\s'-]+$", message = "O bairro deve conter apenas letras, números e espaços") String neighborhood,

    @NotBlank(message = "A rua não pode estar em branco") @Size(min = 2, max = 80, message = "A rua deve ter entre 2 e 80 caracteres") @Pattern(regexp = "^[A-Za-zÀ-ÖØ-öø-ÿ0-9\\s'.,-]+$", message = "A rua deve conter apenas letras, números e espaços") String street,

    @NotBlank(message = "O número não pode estar em branco") @Pattern(regexp = "^[0-9A-Za-z/-]{1,10}$", message = "O número deve conter apenas letras, números, / ou -") String number,

    @Size(max = 60, message = "O complemento deve ter no máximo 60 caracteres") @Pattern(regexp = "^[A-Za-zÀ-ÖØ-öø-ÿ0-9\\s'°º.,-]*$", message = "O complemento contém caracteres inválidos") String complement) {
}
