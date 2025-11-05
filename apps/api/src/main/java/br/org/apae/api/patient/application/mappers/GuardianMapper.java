package br.org.apae.api.patient.application.mappers;

import br.org.apae.api.address.application.mapper.AddressMapper;
import br.org.apae.api.address.domain.model.Address;
import br.org.apae.api.common.dto.address.AddressResponseDTO;
import br.org.apae.api.common.dto.patient.request.guardian.CreateGuardianDTO;
import br.org.apae.api.common.dto.patient.request.guardian.UpdateGuardianDTO;
import br.org.apae.api.common.dto.patient.response.guardian.GuardianResponseDTO;
import br.org.apae.api.patient.domain.model.Guardian;

import java.util.UUID;

import org.springframework.stereotype.Component;

@Component
public class GuardianMapper {

    private final AddressMapper addressMapper;

    public GuardianMapper(AddressMapper addressMapper) {
        this.addressMapper = addressMapper;
    }

    public Guardian toEntity(CreateGuardianDTO dto, AddressResponseDTO addressDto, UUID patientId) {
        Address address = addressMapper.toEntityFromResponse(addressDto);

        return new Guardian(
                dto.name(),
                dto.contact(),
                dto.kinship(),
                address,
                patientId);
    }

    public Guardian toEntityFromResponse(GuardianResponseDTO dto, UUID patientId) {
        Address address = addressMapper.toEntityFromResponse(dto.address());

        return new Guardian(
                dto.name(),
                dto.contact(),
                dto.kinship(),
                address,
                patientId);
    }

    public Guardian updateEntityFromDto(Guardian guardian, UpdateGuardianDTO dto, AddressResponseDTO addressDto,
            UUID patientId) {
        Address addressUpdated = addressMapper.toEntityFromResponse(addressDto);

        return new Guardian(
                guardian.getId(),
                dto.name(),
                dto.contact(),
                dto.kinship(),
                addressUpdated,
                patientId);
    }

    public GuardianResponseDTO toResponseDTO(Guardian guardian) {
        AddressResponseDTO addressResponseDTO = new AddressResponseDTO(guardian.getAddress());

        return new GuardianResponseDTO(guardian, addressResponseDTO);
    }
}