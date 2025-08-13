package br.org.apae.auth.api.dto;

public record SignUpRequestDTO(
    String cpf,
    String password,
    String email,
    String fullName) 
{
    public void validateAttributes() {
        validateCpf();
        validatePasswordLength();
        validateEmail();
        validateFullName();
    }

    private void validateCpf() {
        if (cpf == null || cpf.isBlank()) {
            throw new IllegalArgumentException("CPF cannot be null or blank");
        }

        if (cpf.length() != 11) {
            throw new IllegalArgumentException("CPF must be exactly 11 characters long");
            
        }
    }

    private void validatePasswordLength() {
        if (password == null || password.isBlank()) {
            throw new IllegalArgumentException("Password cannot be null or blank");
        }

        if (password.length() < 8) {
            throw new IllegalArgumentException("Password must be at least 8 characters long");
        }
    }

    private void validateEmail() {
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("Email cannot be null or blank");
        }
    }

    private void validateFullName() {
        if (fullName == null || fullName.isBlank()) {
            throw new IllegalArgumentException("Full name cannot be null or blank");
        }
    }
}
