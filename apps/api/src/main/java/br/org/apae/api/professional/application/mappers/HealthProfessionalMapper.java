package br.org.apae.api.professional.application.mappers;

import br.org.apae.api.address.application.mapper.AddressMapper;
import br.org.apae.api.common.dto.availability.response.AvailabilityResponseDTO;
import br.org.apae.api.common.dto.address.AddressResponseDTO;
import br.org.apae.api.common.dto.professional.request.CreateHealthProfessionalDTO;
import br.org.apae.api.common.dto.professional.request.UpdateHealthProfessionalDTO;
import br.org.apae.api.common.dto.professional.response.HealthProfessionalResponseDTO;
import br.org.apae.api.professional.domain.model.Availability;
import br.org.apae.api.professional.domain.model.Enum.Day;
import br.org.apae.api.professional.domain.model.Enum.Shift;
import br.org.apae.api.professional.domain.model.HealthProfessional;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class HealthProfessionalMapper {

    private final AddressMapper addressMapper;

    public HealthProfessionalMapper(AddressMapper addressMapper) {
        this.addressMapper = addressMapper;
    }

    // Converte CreateHealthProfessionalDTO em HealthProfessional
    public HealthProfessional toEntity(CreateHealthProfessionalDTO dto) {
        HealthProfessional professional = new HealthProfessional(
                dto.name(),
                dto.email(),
                dto.healthSector(),
                dto.phoneNumber(),
                dto.identityDocument(),
                dto.professionalDocument(),
                null // endereço será setado pelo service
        );

        if (dto.availabilities() != null) {
            List<Availability> availabilities = dto.availabilities().stream()
                    .map(a -> {
                        Day dayEnum = Day.valueOf(a.day().toUpperCase());
                        Shift shiftEnum = Shift.valueOf(a.shift().toUpperCase());
                        Availability availability = new Availability(shiftEnum, dayEnum,professional);
                        availability.setProfessional(professional);
                        return availability;
                    })
                    .toList();
            professional.setAvailabilities(availabilities);
        }

        return professional;
    }

    // Converte HealthProfessional em DTO de resposta
    public HealthProfessionalResponseDTO toResponseDTO(HealthProfessional entity) {
        if (entity == null) return null;

        AddressResponseDTO addressDto = entity.getAddress() != null
                ? new AddressResponseDTO(entity.getAddress())
                : null;

        List<AvailabilityResponseDTO> availabilities = Optional.ofNullable(entity.getAvailabilities())
                .orElseGet(List::of)
                .stream()
                .map(AvailabilityResponseDTO::new)
                .toList();

        return new HealthProfessionalResponseDTO(
                entity.getId(),
                entity.getName(),
                entity.getEmail(),
                entity.getHealthSector(),
                entity.getPhoneNumber(),
                entity.getIdentityDocument(),
                entity.getProfessionalDocument(),
                addressDto,
                availabilities
        );
    }

    // Atualiza HealthProfessional com UpdateHealthProfessionalDTO
    public void updateEntityFromDto(
            HealthProfessional entity,
            UpdateHealthProfessionalDTO dto,
            AddressResponseDTO addressDto
    ) {
        entity.setName(dto.name());
        entity.setEmail(dto.email());
        entity.setHealthSector(dto.healthSector());
        entity.setPhoneNumber(dto.phoneNumber());
        entity.setIdentityDocument(dto.identityDocument());
        entity.setProfessionalDocument(dto.professionalDocument());

        if (addressDto != null) {
            entity.setAddress(addressMapper.toEntityFromResponse(addressDto));
        }
    }
}
