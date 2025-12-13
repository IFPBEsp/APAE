package br.org.apae.api.professional.application.mappers;

import br.org.apae.api.common.dto.availability.request.CreateAvailabilityDTO;
import br.org.apae.api.common.dto.availability.request.UpdateAvailabilityDTO;
import br.org.apae.api.common.dto.availability.response.AvailabilityResponseDTO;
import br.org.apae.api.professional.domain.model.Availability;
import br.org.apae.api.professional.domain.model.enums.Day;
import br.org.apae.api.professional.domain.model.enums.Shift;
import br.org.apae.api.professional.domain.model.HealthProfessional;
import org.springframework.stereotype.Component;

@Component
public class AvailabilityMapper {

    public Availability toEntity(CreateAvailabilityDTO dto, HealthProfessional professional) {
        return convert(dto.day(), dto.shift(), professional);
    }

    public Availability toEntity(UpdateAvailabilityDTO dto, HealthProfessional professional) {
        return convert(dto.day(), dto.shift(), professional);
    }

    private Availability convert(String dayStr, String shiftStr, HealthProfessional professional) {
        try {
            Day dayEnum = Day.valueOf(dayStr.toUpperCase());
            Shift shiftEnum = Shift.valueOf(shiftStr.toUpperCase());
            return new Availability(dayEnum, shiftEnum, professional);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Dia ou Turno inválido: " + dayStr + " / " + shiftStr);
        }
    }

    public AvailabilityResponseDTO toResponseDTO(Availability entity) {
        return new AvailabilityResponseDTO(entity);
    }
}