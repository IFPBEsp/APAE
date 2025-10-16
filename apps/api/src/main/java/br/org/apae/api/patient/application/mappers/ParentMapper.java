package br.org.apae.api.patient.application.mappers;

import br.org.apae.api.common.dto.patient.create.CreateParentDTO;
import br.org.apae.api.common.dto.patient.update.UpdateParentDTO;
import br.org.apae.api.patient.domain.model.Parent;
import br.org.apae.api.patient.domain.model.Patient;
import org.springframework.stereotype.Component;

@Component
public class ParentMapper {
    public Parent toEntity(CreateParentDTO dto, Patient patient) {
        if (dto == null) {
            return null;
        }
        return Parent.builder()
                .name(dto.name())
                .rg(dto.rg())
                .cpf(dto.cpf())
                .isAlive(dto.isAlive())
                .profession(dto.profession())
                .kinship(dto.kinship())
                .patient(patient)
                .build();
    }

    public Parent toEntity(UpdateParentDTO dto, Patient patient) {
        if (dto == null) {
            return null;
        }
        return Parent.builder()
                .name(dto.name())
                .rg(dto.rg())
                .cpf(dto.cpf())
                .isAlive(dto.isAlive())
                .profession(dto.profession())
                .kinship(dto.kinship())
                .patient(patient)
                .build();
    }
}