package br.org.apae.api.professional.application.mappers;

import br.org.apae.api.common.dto.professional.response.HealthProfessionalResponseDTO;
import br.org.apae.api.address.application.mapper.AddressMapper;
import br.org.apae.api.address.domain.model.Address;
import br.org.apae.api.common.dto.professional.request.CreateHealthProfessionalDTO;
import br.org.apae.api.common.dto.professional.request.UpdateHealthProfessionalDTO;
import br.org.apae.api.professional.domain.model.HealthProfessional;
import org.springframework.stereotype.Component;

@Component
public class HealthProfessionalMapper {

    private final AddressMapper addressMapper;

    public HealthProfessionalMapper(AddressMapper addressMapper) {
        this.addressMapper = addressMapper;
    }

    public HealthProfessional toEntity(CreateHealthProfessionalDTO dto) {
        if (dto == null) {
            return null;
        }

        HealthProfessional professional = new HealthProfessional(
                dto.name(),
                dto.email(),
                dto.healthSector(),
                dto.phoneNumber(),
                dto.professionalDocument());

        professional.setIdentityDocument(dto.identityDocument());

        Address address = addressMapper.toEntity(dto.address());
        professional.setAddress(address);

        return professional;
    }

    public void updateEntityFromDto(HealthProfessional professional, UpdateHealthProfessionalDTO dto) {
        if (dto == null || professional == null) {
            return;
        }

        professional.setName(dto.name());
        professional.setEmail(dto.email());
        professional.setHealthSector(dto.healthSector());
        professional.setPhoneNumber(dto.phoneNumber());
        professional.setProfessionalDocument(dto.professionalDocument());
        professional.setIdentityDocument(dto.identityDocument());

        if (professional.getAddress() != null && dto.address() != null) {
            addressMapper.updateEntityFromDto(professional.getAddress(), dto.address());
        }
    }

    public HealthProfessionalResponseDTO toResponseDTO(HealthProfessional professional) {
        if (professional == null) {
            return null;
        }
        return new HealthProfessionalResponseDTO(professional);
    }
}