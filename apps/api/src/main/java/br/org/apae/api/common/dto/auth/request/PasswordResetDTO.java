package br.org.apae.api.common.dto.auth.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record PasswordResetDTO(
    @NotBlank(message = "Token é obrigatório.")
    String token,

    @NotBlank(message = "Nova senha é obrigatória.")
    @Size(min = 8, message = "A senha deve ter no mínimo 8 caracteres.")
    String newPassword,

    @NotBlank(message = "Confirmação de senha é obrigatória.")
    String confirmPassword) {
}