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

        if (cpf.length() != 14) {
            throw new IllegalArgumentException("CPF must be exactly 14 characters long");
            
        }
    }

    private void validatePasswordLength() {
        if (password == null || password.isBlank()) {
            throw new IllegalArgumentException("Password cannot be null or blank");
        }

        if (password.length() < 6) {
            throw new IllegalArgumentException("Password must be at least 6 characters long");
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
