package br.org.apae.auth.api.dto;

public record SignUpRequestDTO(
    String cpf,
    String password,
    String email,
    String fullName) {
}
