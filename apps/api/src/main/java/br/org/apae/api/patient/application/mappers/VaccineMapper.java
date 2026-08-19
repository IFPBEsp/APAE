package br.org.apae.api.patient.application.mappers;

import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import br.org.apae.api.common.dto.patient.request.vaccine.CreateVaccineDTO;
import br.org.apae.api.common.dto.patient.response.vaccine.VaccineResponseDTO;
import br.org.apae.api.patient.domain.model.Vaccine;

@Component
public class VaccineMapper {

    public Set<Vaccine> toEntitySetFromResponse(Set<VaccineResponseDTO> responseDTOs) {
        return responseDTOs.stream()
                .map(dto -> new Vaccine(dto.id(), dto.name()))
                .collect(Collectors.toSet());
    }

    public VaccineResponseDTO toResponseDTO(Vaccine vaccine) {
        return new VaccineResponseDTO(vaccine.getId(), vaccine.getName(), false);
    }

    public Set<VaccineResponseDTO> toResponseDTOSet(Set<Vaccine> vaccines) {
        return vaccines.stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toSet());
    }

    public Vaccine toEntity(CreateVaccineDTO dto) {
        return new Vaccine(dto.name());
    }
}
