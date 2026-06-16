package br.org.apae.api.common.dto.auth.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record SignUpDTO(
        @NotBlank(message = "O e-mail é obrigatório")
        @Email(message = "O formato do e-mail é inválido")
        String email,

        @NotBlank(message = "A senha é obrigatória")
        String password,

        @NotBlank(message = "O CPF é obrigatório")
        String cpf,

        @NotBlank(message = "O nome completo é obrigatório")
        String fullName
) {}
