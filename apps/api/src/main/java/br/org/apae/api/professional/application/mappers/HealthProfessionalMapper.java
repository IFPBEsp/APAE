package br.org.apae.api.professional.application.mappers;

import br.org.apae.api.common.dto.professional.response.HealthProfessionalResponseDTO;
import br.org.apae.api.common.dto.servicearea.response.ServiceAreaResponseDTO;
import br.org.apae.api.address.application.mapper.AddressMapper;
import br.org.apae.api.address.domain.model.Address;
import br.org.apae.api.common.dto.address.AddressResponseDTO;
import br.org.apae.api.common.dto.professional.request.CreateHealthProfessionalDTO;
import br.org.apae.api.common.dto.professional.request.UpdateHealthProfessionalDTO;
import br.org.apae.api.professional.domain.model.HealthProfessional;
import br.org.apae.api.servicearea.application.mappers.ServiceAreaMapper;
import br.org.apae.api.servicearea.domain.model.ServiceArea;

import org.springframework.stereotype.Component;

@Component
public class HealthProfessionalMapper {

    private final AddressMapper addressMapper;
    private final ServiceAreaMapper serviceAreaMapper;

    public HealthProfessionalMapper(AddressMapper addressMapper, ServiceAreaMapper serviceAreaMapper) {
        this.addressMapper = addressMapper;
        this.serviceAreaMapper = serviceAreaMapper;
    }

    public HealthProfessional toEntity(CreateHealthProfessionalDTO dto, ServiceAreaResponseDTO serviceAreaDto, AddressResponseDTO addressDto) {
        Address address = addressMapper.toEntityFromResponse(addressDto);
        ServiceArea serviceArea = serviceAreaMapper.toEntityFromResponse(serviceAreaDto);

        return new HealthProfessional(
                dto.name(),
                dto.email(),
                serviceArea,
                dto.phoneNumber(),
                dto.identityDocument(),
                dto.professionalDocument(),
                address);
    }

    public HealthProfessional updateEntityFromDto(HealthProfessional professional, UpdateHealthProfessionalDTO dto, 
            ServiceAreaResponseDTO serviceAreaDto, AddressResponseDTO addressDto) {
        Address address = addressMapper.toEntityFromResponse(addressDto);
        ServiceArea serviceArea = serviceAreaMapper.toEntityFromResponse(serviceAreaDto);

        return new HealthProfessional(
                professional.getId(),
                dto.name(),
                dto.email(),
                serviceArea,
                dto.phoneNumber(),
                dto.identityDocument(),
                dto.professionalDocument(),
                address);
    }

    public HealthProfessionalResponseDTO toResponseDTO(HealthProfessional professional) {
        AddressResponseDTO addressResponseDTO = new AddressResponseDTO(professional.getAddress());
        ServiceAreaResponseDTO serviceAreaResponseDTO = new ServiceAreaResponseDTO(professional.getServiceArea());

        return new HealthProfessionalResponseDTO(professional, serviceAreaResponseDTO, addressResponseDTO);
    }
}
