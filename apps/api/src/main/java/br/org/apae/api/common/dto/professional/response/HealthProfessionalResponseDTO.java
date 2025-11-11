package br.org.apae.api.common.dto.professional.response;

import java.util.List;
import java.util.UUID;

import br.org.apae.api.common.dto.availability.response.AvailabilityResponseDTO;
import br.org.apae.api.common.dto.address.AddressResponseDTO;

public record HealthProfessionalResponseDTO(
        UUID id,
        String name,
        String email,
        String healthSector,
        String phoneNumber,
        String identityDocument,
        String professionalDocument,
        AddressResponseDTO address,
        List<AvailabilityResponseDTO> availabilities
) {}
