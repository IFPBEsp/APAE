package br.org.apae.api.common.dto.auth.request;

import br.org.apae.api.common.validations.CPF;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record SignUpDTO(
    @Email(message = "O e-mail informado é inválido.")
    @NotBlank(message = "O e-mail é obrigatório.")
    String email,

    @NotBlank(message = "A senha é obrigatória.")
    @Size(min = 8, message = "A senha deve ter pelo menos 8 caracteres.")
    String password,

    @CPF
    @NotBlank(message = "O CPF é obrigatório.")
    String cpf,

    @NotBlank(message = "O nome completo é obrigatório.")
    String fullName) {

}
