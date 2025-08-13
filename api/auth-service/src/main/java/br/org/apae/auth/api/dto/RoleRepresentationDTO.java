package br.org.apae.auth.api.dto;

public record RoleRepresentationDTO(
    String id,
    String name,
    String description,
    boolean composite,
    boolean clientRole,
    String containerId) {
}
