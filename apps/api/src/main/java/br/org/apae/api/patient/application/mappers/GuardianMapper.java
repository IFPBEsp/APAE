package br.org.apae.api.patient.application.mappers;

import br.org.apae.api.common.mappers.AddressMapper;
import br.org.apae.api.common.dto.patient.create.CreateGuardianDTO;
import br.org.apae.api.common.dto.patient.update.UpdateGuardianDTO;
import br.org.apae.api.common.model.Address;
import br.org.apae.api.patient.domain.model.Guardian;
import org.springframework.stereotype.Component;

@Component
public class GuardianMapper {

    private final AddressMapper addressMapper;

    public GuardianMapper(AddressMapper addressMapper) {
        this.addressMapper = addressMapper;
    }

    public Guardian toEntity(CreateGuardianDTO dto) {
        if (dto == null) return null;
        Address address = addressMapper.toEntity(dto.address());
        return new Guardian(dto.name(), dto.contact(), dto.kinship(), address);
    }

    public void updateEntityFromDto(Guardian guardian, UpdateGuardianDTO dto) {
        if (dto == null || guardian == null) return;
        guardian.setName(dto.name());
        guardian.setContact(dto.contact());
        guardian.setKinship(dto.kinship());
        if (guardian.getAddress() != null && dto.address() != null) {
            addressMapper.updateEntityFromDto(guardian.getAddress(), dto.address());
        }
    }
}