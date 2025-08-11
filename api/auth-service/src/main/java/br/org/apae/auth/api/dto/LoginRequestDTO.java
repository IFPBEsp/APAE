package br.org.apae.auth.api.dto;

public record LoginRequestDTO(
    String username,
    String password) 
{
    public void validateAttributes() {
        validateUsername();
        validatePasswordLength();
    }

    private void validateUsername() {
        if (username == null || username.isBlank()) {
            throw new IllegalArgumentException("Username cannot be null or blank");
        }
    }

    private void validatePasswordLength() {
        if (password == null || password.isBlank()) {
            throw new IllegalArgumentException("Password cannot be null or blank");
        }
    }
}
