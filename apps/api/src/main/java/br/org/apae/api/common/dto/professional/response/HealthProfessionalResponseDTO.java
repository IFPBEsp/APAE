package br.org.apae.api.common.dto.professional.response;

import java.util.List;
import java.util.UUID;

import br.org.apae.api.common.dto.Avaliability.Response.AvaliabilityResponseDTO;
import br.org.apae.api.common.dto.address.AddressResponseDTO;
import br.org.apae.api.professional.domain.model.HealthProfessional;

public record HealthProfessionalResponseDTO(
        UUID id,
        String name,
        String email,
        String healthSector,
        String phoneNumber,
        String identityDocument,
        String professionalDocument,
        AddressResponseDTO address,
        List<AvaliabilityResponseDTO> availabilities
) {
    public HealthProfessionalResponseDTO(HealthProfessional professional,
                                         AddressResponseDTO addressResponseDTO,
                                         List<AvaliabilityResponseDTO> availabilities) {
        this(
            professional.getId(),
            professional.getName(),
            professional.getEmail(),
            professional.getHealthSector(),
            professional.getPhoneNumber(),
            professional.getIdentityDocument(),
            professional.getProfessionalDocument(),
            addressResponseDTO,
            availabilities
        );
    }
}
