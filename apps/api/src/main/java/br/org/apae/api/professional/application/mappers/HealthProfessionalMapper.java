package br.org.apae.api.professional.application.mappers;

import br.org.apae.api.common.dto.Avaliability.Request.AvaliabilityRequestCreateDTO;
import br.org.apae.api.common.dto.Avaliability.Response.AvaliabilityResponseDTO;
import br.org.apae.api.common.dto.address.AddressResponseDTO;
import br.org.apae.api.common.dto.professional.request.CreateHealthProfessionalDTO;
import br.org.apae.api.common.dto.professional.request.UpdateHealthProfessionalDTO;
import br.org.apae.api.common.dto.professional.response.HealthProfessionalResponseDTO;
import br.org.apae.api.professional.domain.model.Avaliability;
import br.org.apae.api.professional.domain.model.Enum.Day;
import br.org.apae.api.professional.domain.model.Enum.Shift;
import br.org.apae.api.professional.domain.model.HealthProfessional;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class HealthProfessionalMapper {

    // Converte CreateHealthProfessionalDTO em HealthProfessional
    public HealthProfessional toEntity(CreateHealthProfessionalDTO dto, AddressResponseDTO addressDto) {
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
            List<Avaliability> availabilities = dto.availabilities().stream()
                    .map(a -> {
                        Day dayEnum = Day.valueOf(a.day().toUpperCase());
                        Shift shiftEnum = Shift.valueOf(a.shift().toUpperCase());
                        Avaliability availability = new Avaliability(dayEnum, shiftEnum);
                        availability.setProfessional(professional);
                        return availability;
                    })
                    .collect(Collectors.toList());
            professional.setAvailabilities(availabilities);
        }

        return professional;
    }

    // Converte HealthProfessional em DTO de resposta
    public HealthProfessionalResponseDTO toResponseDTO(HealthProfessional entity, AddressResponseDTO addressDto) {
        List<AvaliabilityResponseDTO> availabilities = entity.getAvailabilities().stream()
                .map(AvaliabilityResponseDTO::new)
                .collect(Collectors.toList());

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

    // Atualiza HealthProfessional com CreateHealthProfessionalDTO
    public HealthProfessional updateEntityFromDto(HealthProfessional entity,
                                                   CreateHealthProfessionalDTO dto,
                                                   AddressResponseDTO addressDto) {
        entity.setName(dto.name());
        entity.setEmail(dto.email());
        entity.setHealthSector(dto.healthSector());
        entity.setPhoneNumber(dto.phoneNumber());
        entity.setIdentityDocument(dto.identityDocument());
        entity.setProfessionalDocument(dto.professionalDocument());
        entity.setAddress(addressDto != null ? addressDto.toEntity() : entity.getAddress());

        if (dto.availabilities() != null) {
            List<Avaliability> availabilities = dto.availabilities().stream()
                    .map(a -> {
                        Day dayEnum = Day.valueOf(a.day().toUpperCase());
                        Shift shiftEnum = Shift.valueOf(a.shift().toUpperCase());
                        Avaliability availability = new Avaliability(dayEnum, shiftEnum);
                        availability.setProfessional(entity);
                        return availability;
                    })
                    .collect(Collectors.toList());
            entity.setAvailabilities(availabilities);
        }

        return entity;
    }

    // Atualiza HealthProfessional com UpdateHealthProfessionalDTO
    public HealthProfessional updateEntityFromDto(HealthProfessional entity,
                                                   UpdateHealthProfessionalDTO dto,
                                                   AddressResponseDTO addressDto) {
        entity.setName(dto.name());
        entity.setEmail(dto.email());
        entity.setHealthSector(dto.healthSector());
        entity.setPhoneNumber(dto.phoneNumber());
        entity.setIdentityDocument(dto.identityDocument());
        entity.setProfessionalDocument(dto.professionalDocument());
        entity.setAddress(addressDto != null ? addressDto.toEntity() : entity.getAddress());

        if (dto.availabilities() != null) {
            List<Avaliability> availabilities = dto.availabilities().stream()
                    .map(a -> {
                        Day dayEnum = Day.valueOf(a.day().toUpperCase());
                        Shift shiftEnum = Shift.valueOf(a.shift().toUpperCase());
                        Avaliability availability = new Avaliability(dayEnum, shiftEnum);
                        availability.setProfessional(entity);
                        return availability;
                    })
                    .collect(Collectors.toList());
            entity.setAvailabilities(availabilities);
        }

        return entity;
    }
}
