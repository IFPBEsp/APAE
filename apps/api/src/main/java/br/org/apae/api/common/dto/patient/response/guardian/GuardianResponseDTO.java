package br.org.apae.api.common.dto.patient.response.guardian;

import br.org.apae.api.common.dto.address.AddressResponseDTO;
import br.org.apae.api.patient.domain.model.Guardian;

import java.util.UUID;

public record GuardianResponseDTO(
        UUID id,
        String name,
        String contact,
        String kinship,
        AddressResponseDTO address) {

    public GuardianResponseDTO(Guardian guardian, AddressResponseDTO addressResponseDto) {
        this(
                guardian.getId(),
                guardian.getName(),
                guardian.getContact(),
                guardian.getKinship(),
                addressResponseDto);
    }
}
