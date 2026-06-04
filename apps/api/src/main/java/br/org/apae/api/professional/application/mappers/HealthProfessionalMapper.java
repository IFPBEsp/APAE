package br.org.apae.api.professional.application.mappers;

import br.org.apae.api.address.application.mapper.AddressMapper;
import br.org.apae.api.auth.domain.model.User;
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

    private final ServiceAreaMapper serviceAreaMapper;
    private final AvailabilityMapper availabilityMapper;
    private final AddressMapper addressMapper;

    public HealthProfessionalMapper(ServiceAreaMapper serviceAreaMapper,
                                    AvailabilityMapper availabilityMapper,
                                    AddressMapper addressMapper) {
        this.serviceAreaMapper = serviceAreaMapper;
        this.availabilityMapper = availabilityMapper;
        this.addressMapper = addressMapper;
    }

    public HealthProfessional toEntity(CreateHealthProfessionalDTO dto, ServiceAreaResponseDTO serviceAreaDto, User user) {
        ServiceArea serviceArea = serviceAreaMapper.toEntityFromResponse(serviceAreaDto);
        user.updateAddress(addressMapper.toEntity(dto.address()));

        HealthProfessional entity = new HealthProfessional(
                user,
                serviceArea,
                dto.professionalDocument());
        entity.setProfilePhoto(dto.profilePhoto());

        if (dto.availabilities() != null) {
            dto.availabilities().forEach(availDto -> {
                entity.addAvailability(availabilityMapper.toEntity(availDto, entity));
            });
        }

        return entity;
    }

    public HealthProfessional updateEntityFromDto(HealthProfessional professional, UpdateHealthProfessionalDTO dto,
                                                  ServiceAreaResponseDTO serviceAreaDto) {
        ServiceArea serviceArea = serviceAreaMapper.toEntityFromResponse(serviceAreaDto);

        professional.setName(dto.name());
        professional.setEmail(dto.email());
        professional.setServiceArea(serviceArea);
        professional.setPhoneNumber(dto.phoneNumber());
        professional.setIdentityDocument(dto.identityDocument());
        professional.setAddress(addressMapper.updateEntityFromDto(professional.getAddress(), dto.address()));
        professional.setProfessionalDocument(dto.professionalDocument());
        professional.setProfilePhoto(dto.profilePhoto());

        if (dto.availabilities() != null) {
            List<Availability> newAvailabilities = dto.availabilities().stream()
                    .map(a -> availabilityMapper.toEntity(a, professional))
                    .collect(Collectors.toList());

            professional.setAvailabilities(newAvailabilities);
        }

        return professional;
    }

    public HealthProfessionalResponseDTO toResponseDTO(HealthProfessional professional) {
        ServiceAreaResponseDTO serviceAreaResponseDTO = new ServiceAreaResponseDTO(professional.getServiceArea());

        List<AvailabilityResponseDTO> availabilityDTOs = (professional.getAvailabilities() != null)
                ? professional.getAvailabilities().stream()
                .map(availabilityMapper::toResponseDTO)
                .toList()
                : Collections.emptyList();

        return new HealthProfessionalResponseDTO(
                professional,
                serviceAreaResponseDTO,
                availabilityDTOs
        );
    }
}
