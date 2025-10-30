package br.org.apae.api.common.dto.auth.request;

public record SignUpDTO(String username, String password, String cpf, String fullName) {

}
