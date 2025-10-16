package br.org.apae.api.patient.application.mappers;

import br.org.apae.api.common.dto.vaccine.create.CreateVaccineDTO;
import br.org.apae.api.common.dto.vaccine.response.ResponseVaccineDTO;
import br.org.apae.api.patient.domain.model.Vaccine;
import org.springframework.stereotype.Component;

@Component
public class VaccineMapper {

    public Vaccine toEntity(CreateVaccineDTO dto) {
        if (dto == null) {
            return null;
        }
        return new Vaccine(dto.name());
    }

    public ResponseVaccineDTO toResponseDTO(Vaccine vaccine) {
        if (vaccine == null) {
            return null;
        }
        return new ResponseVaccineDTO(vaccine.getId(), vaccine.getName());
    }
}