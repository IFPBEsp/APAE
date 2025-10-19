package br.org.apae.api.patient.application.mappers;

import br.org.apae.api.address.application.mapper.AddressMapper;
import br.org.apae.api.address.domain.model.Address;
import br.org.apae.api.common.dto.address.AddressResponseDTO;
import br.org.apae.api.common.dto.patient.request.guardian.CreateGuardianDTO;
import br.org.apae.api.common.dto.patient.request.guardian.UpdateGuardianDTO;
import br.org.apae.api.common.dto.patient.response.guardian.GuardianResponseDTO;
import br.org.apae.api.patient.domain.model.Guardian;
import org.springframework.stereotype.Component;

@Component
public class GuardianMapper {

    private final AddressMapper addressMapper;

    public GuardianMapper(AddressMapper addressMapper) {
        this.addressMapper = addressMapper;
    }

    public Guardian toEntity(CreateGuardianDTO dto, AddressResponseDTO addressDto) {
        Address address = addressMapper.toEntityFromResponse(addressDto);

        return new Guardian(
                dto.name(),
                dto.contact(),
                dto.kinship(),
                address);
    }

    public Guardian toEntityFromResponse(GuardianResponseDTO dto) {
        Address address = addressMapper.toEntityFromResponse(dto.address());

        return new Guardian(
                dto.name(),
                dto.contact(),
                dto.kinship(),
                address);
    }

    public Guardian updateEntityFromDto(Guardian guardian, UpdateGuardianDTO dto) {
        Address updatedAddress = addressMapper.updateEntityFromDto(guardian.getAddress(), dto.address());

        return new Guardian(
                guardian.getId(),
                dto.name(),
                dto.contact(),
                dto.kinship(),
                updatedAddress);
    }

    public GuardianResponseDTO toResponseDTO(Guardian guardian) {
        return new GuardianResponseDTO(guardian);
    }
}