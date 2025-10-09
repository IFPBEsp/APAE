package br.org.apae.api.common.dto.professional.response;

import br.org.apae.api.common.model.Address;

import java.util.UUID;

public record HealthProfessionalResponseDTO(
    UUID id,
    String healthSector,
    String phoneNumber,
    String professionalDocument,
    String email,
    String name,
    String identityDocument,
    Address address) {
}