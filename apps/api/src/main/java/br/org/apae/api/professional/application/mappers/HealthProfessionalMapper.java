package br.org.apae.api.professional.application.mappers;

import br.org.apae.api.address.application.mapper.AddressMapper;
import br.org.apae.api.address.domain.model.Address;
import br.org.apae.api.common.dto.address.AddressResponseDTO;
import br.org.apae.api.common.dto.availability.response.AvailabilityResponseDTO;
import br.org.apae.api.common.dto.professional.request.CreateHealthProfessionalDTO;
import br.org.apae.api.common.dto.professional.request.UpdateHealthProfessionalDTO;
import br.org.apae.api.common.dto.professional.response.HealthProfessionalResponseDTO;
import br.org.apae.api.common.dto.servicearea.response.ServiceAreaResponseDTO;
import br.org.apae.api.professional.domain.model.Availability;
import br.org.apae.api.professional.domain.model.HealthProfessional;
import br.org.apae.api.servicearea.application.mappers.ServiceAreaMapper;
import br.org.apae.api.servicearea.domain.model.ServiceArea;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class HealthProfessionalMapper {

    private final AddressMapper addressMapper;
    private final ServiceAreaMapper serviceAreaMapper;
    private final AvailabilityMapper availabilityMapper;

    public HealthProfessionalMapper(AddressMapper addressMapper,
                                    ServiceAreaMapper serviceAreaMapper,
                                    AvailabilityMapper availabilityMapper) {
        this.addressMapper = addressMapper;
        this.serviceAreaMapper = serviceAreaMapper;
        this.availabilityMapper = availabilityMapper;
    }

    public HealthProfessional toEntity(CreateHealthProfessionalDTO dto, ServiceAreaResponseDTO serviceAreaDto) {
        Address address = addressMapper.toEntity(dto.address());
        ServiceArea serviceArea = serviceAreaMapper.toEntityFromResponse(serviceAreaDto);

        HealthProfessional entity = new HealthProfessional(
                dto.name(),
                dto.email(),
                serviceArea,
                dto.phoneNumber(),
                dto.identityDocument(),
                dto.professionalDocument(),
                address);

        if (dto.availabilities() != null) {
            dto.availabilities().forEach(availDto -> {
                entity.addAvailability(availabilityMapper.toEntity(availDto, entity));
            });
        }

        return entity;
    }

    public HealthProfessional updateEntityFromDto(HealthProfessional professional, UpdateHealthProfessionalDTO dto,
                                                  ServiceAreaResponseDTO serviceAreaDto) {
        Address address = addressMapper.toEntity(dto.address());
        ServiceArea serviceArea = serviceAreaMapper.toEntityFromResponse(serviceAreaDto);

        professional.setName(dto.name());
        professional.setEmail(dto.email());
        professional.setServiceArea(serviceArea);
        professional.setPhoneNumber(dto.phoneNumber());
        professional.setIdentityDocument(dto.identityDocument());
        professional.setProfessionalDocument(dto.professionalDocument());
        professional.setAddress(address);

        if (dto.availabilities() != null) {
            List<Availability> newAvailabilities = dto.availabilities().stream()
                    .map(a -> availabilityMapper.toEntity(a, professional))
                    .collect(Collectors.toList());

            professional.setAvailabilities(newAvailabilities);
        }

        return professional;
    }

    public HealthProfessionalResponseDTO toResponseDTO(HealthProfessional professional) {
        AddressResponseDTO addressResponseDTO = new AddressResponseDTO(professional.getAddress());
        ServiceAreaResponseDTO serviceAreaResponseDTO = new ServiceAreaResponseDTO(professional.getServiceArea());

        List<AvailabilityResponseDTO> availabilityDTOs = (professional.getAvailabilities() != null)
                ? professional.getAvailabilities().stream()
                .map(availabilityMapper::toResponseDTO)
                .toList()
                : Collections.emptyList();

        return new HealthProfessionalResponseDTO(
                professional,
                serviceAreaResponseDTO,
                addressResponseDTO,
                availabilityDTOs
        );
    }
}