package br.org.apae.api.common.dto.professional.response;

import br.org.apae.api.common.dto.address.AddressResponseDTO;
import br.org.apae.api.common.dto.servicearea.response.ServiceAreaResponseDTO;
import br.org.apae.api.professional.domain.model.HealthProfessional;

import java.util.UUID;

public record HealthProfessionalResponseDTO(
        UUID id,
        String phoneNumber,
        String professionalDocument,
        String email,
        String name,
        String identityDocument,
        ServiceAreaResponseDTO serviceArea,
        AddressResponseDTO address) {

    public HealthProfessionalResponseDTO(HealthProfessional entity, ServiceAreaResponseDTO serviceAreaResponseDTO, AddressResponseDTO addressResponseDTO) {
        this(
                entity.getId(),
                entity.getPhoneNumber(),
                entity.getProfessionalDocument(),
                entity.getEmail(),
                entity.getName(),
                entity.getIdentityDocument(),
                serviceAreaResponseDTO,
                addressResponseDTO);
    }
}