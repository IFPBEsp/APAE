package br.org.apae.api.patient.application.internal;

import br.org.apae.api.common.dto.disorder.request.CreateDisorderDTO;
import br.org.apae.api.common.dto.disorder.response.DisorderResponseDTO;
import br.org.apae.api.patient.domain.model.Disorder;
import org.springframework.stereotype.Component;

@Component
public class DisorderMapper {

    public Disorder toEntity(CreateDisorderDTO dto) {
        return new Disorder(dto.name());
    }

    public DisorderResponseDTO toResponseDTO(Disorder disorder) {
        return new DisorderResponseDTO(
                disorder.getId(),
                disorder.getName()
        );
    }
}