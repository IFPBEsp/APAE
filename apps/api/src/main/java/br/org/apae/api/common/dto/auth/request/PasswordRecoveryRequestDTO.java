package br.org.apae.api.common.dto.auth.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record PasswordRecoveryRequestDTO(
    @NotBlank(message = "E-mail é obrigatório.")
    @Email(message = "E-mail inválido.")
    String email) {
}