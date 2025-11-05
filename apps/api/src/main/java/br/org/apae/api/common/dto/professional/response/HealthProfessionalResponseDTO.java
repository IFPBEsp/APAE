package br.org.apae.api.common.dto.professional.response;

import java.util.List;
import java.util.UUID;

import br.org.apae.api.common.dto.Avaliability.Response.AvaliabilityResponseDTO;
import br.org.apae.api.common.dto.address.AddressResponseDTO;
import br.org.apae.api.professional.domain.model.HealthProfessional;

public record HealthProfessionalResponseDTO(
        UUID id,
        String healthSector,
        String phoneNumber,
        String professionalDocument,
        String email,
        String name,
        String identityDocument,
        AddressResponseDTO address,
        List<AvaliabilityResponseDTO> availability
) {
    public HealthProfessionalResponseDTO(
            HealthProfessional entity,
            AddressResponseDTO addressResponseDTO,
            List<AvaliabilityResponseDTO> availabilityResponseDTOs
    ) {
        this(
                entity.getId(),
                entity.getHealthSector(),
                entity.getPhoneNumber(),
                entity.getProfessionalDocument(),
                entity.getEmail(),
                entity.getName(),
                entity.getIdentityDocument(),
                addressResponseDTO,
                availabilityResponseDTOs
        );
    }

    public HealthProfessionalResponseDTO(HealthProfessional professional, AddressResponseDTO addressResponseDTO) {
        this(
                professional.getId(),
                professional.getHealthSector(),
                professional.getPhoneNumber(),
                professional.getProfessionalDocument(),
                professional.getEmail(),
                professional.getName(),
                professional.getIdentityDocument(),
                addressResponseDTO,
                professional.getAvailabilities() != null
                        ? professional.getAvailabilities().stream()
                        .map(AvaliabilityResponseDTO::new)
                        .toList()
                        : null
        );
    }
}
