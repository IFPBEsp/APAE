package br.org.apae.api.professional.application.mappers;

import br.org.apae.api.common.dto.professional.response.HealthProfessionalResponseDTO;
import br.org.apae.api.address.application.mapper.AddressMapper;
import br.org.apae.api.address.domain.model.Address;
import br.org.apae.api.common.dto.address.AddressResponseDTO;
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

    public HealthProfessional toEntity(CreateHealthProfessionalDTO dto, AddressResponseDTO addressDto) {
        Address address = addressMapper.toEntityFromResponse(addressDto);
        return new HealthProfessional(
                dto.name(),
                dto.email(),
                dto.healthSector(),
                dto.phoneNumber(),
                dto.identityDocument(),
                dto.professionalDocument(),
                address);
    }

    public HealthProfessional updateEntityFromDto(HealthProfessional professional, UpdateHealthProfessionalDTO dto,
            AddressResponseDTO addressDto) {
        Address address = addressMapper.toEntityFromResponse(addressDto);

        return new HealthProfessional(
                professional.getId(),
                dto.name(),
                dto.email(),
                dto.healthSector(),
                dto.phoneNumber(),
                dto.identityDocument(),
                dto.professionalDocument(),
                address);
    }

    public HealthProfessionalResponseDTO toResponseDTO(HealthProfessional professional) {
        return new HealthProfessionalResponseDTO(professional);
    }
}