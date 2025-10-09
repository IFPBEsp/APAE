package br.org.apae.api.common.dto.professional.response;

import br.org.apae.api.common.dto.address.AddressResponseDTO;
import br.org.apae.api.professional.domain.model.HealthProfessional;

import java.util.UUID;

public record HealthProfessionalResponseDTO(
        UUID id,
        String healthSector,
        String phoneNumber,
        String professionalDocument,
        String email,
        String name,
        String identityDocument,
        AddressResponseDTO address) {

    public HealthProfessionalResponseDTO(HealthProfessional entity) {
        this(
                entity.getId(),
                entity.getHealthSector(),
                entity.getPhoneNumber(),
                entity.getProfessionalDocument(),
                entity.getEmail(),
                entity.getName(),
                entity.getIdentityDocument(),
                entity.getAddress() != null
                        ? new AddressResponseDTO(entity.getAddress())
                        : null);
    }
}