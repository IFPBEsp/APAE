package br.org.apae.api.common.dto.professional.response;

import br.org.apae.api.common.dto.address.AddressResponseDTO;
import br.org.apae.api.common.dto.availability.response.AvailabilityResponseDTO;
import br.org.apae.api.common.dto.servicetype.response.ServiceTypeResponseDTO;
import br.org.apae.api.professional.domain.model.HealthProfessional;

import java.util.List;
import java.util.UUID;

public record HealthProfessionalResponseDTO(
        UUID id,
        String name,
        String email,
        String professionalDocument,
        String identityDocument,
        String phoneNumber,
        String healthSector,
        Boolean ativo,
        AddressResponseDTO address,
        ServiceTypeResponseDTO serviceArea,
        List<AvailabilityResponseDTO> availabilities,
        String profilePhoto
) {

    public HealthProfessionalResponseDTO(HealthProfessional entity,
                                         ServiceTypeResponseDTO serviceArea,
                                         AddressResponseDTO address,
                                         List<AvailabilityResponseDTO> availabilities) {
        this(
                entity.getId(),
                entity.getName(),
                entity.getEmail(),
                entity.getProfessionalDocument(),
                entity.getIdentityDocument(),
                entity.getPhoneNumber(),
                (entity.getServiceArea() != null) ? entity.getServiceArea().getArea() : null,
                entity.getAtivo(),
                address,
                serviceArea,
                availabilities,
                entity.getProfilePhoto()
        );
    }
}
