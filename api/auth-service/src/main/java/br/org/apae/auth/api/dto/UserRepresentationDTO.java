package br.org.apae.auth.api.dto;

public record UserRepresentationDTO(
    String id,
    String username,
    String email,
    String firstName,
    String lastName,
    boolean enabled) {
}
