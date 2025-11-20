package br.org.apae.api.common.dto.auth.request;

public record SignUpDTO(String email, String password, String cpf, String fullName) {

}
