package br.org.apae.api.patient.application.mappers;

import br.org.apae.api.common.dto.patient.request.disorder.CreateDisorderDTO;
import br.org.apae.api.common.dto.patient.response.disorder.DisorderResponseDTO;
import br.org.apae.api.patient.domain.model.Disorder;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
public class DisorderMapper {

    public Disorder toEntity(CreateDisorderDTO dto) {
        return new Disorder(dto.name());
    }

    public Set<Disorder> toEntitySet(Set<CreateDisorderDTO> dtos) {
        return dtos.stream()
                .map(this::toEntity)
                .collect(Collectors.toSet());
    }

    public Disorder toEntityFromResponse(DisorderResponseDTO dto) {
        return new Disorder(dto.id(), dto.name());
    }

    public Set<Disorder> toEntitySetFromResponse(Set<DisorderResponseDTO> dtos) {
        return dtos.stream()
                .map(this::toEntityFromResponse)
                .collect(Collectors.toSet());
    }

    public DisorderResponseDTO toResponseDTO(Disorder disorder) {
        return new DisorderResponseDTO(
                disorder.getId(),
                disorder.getName());
    }

    public Set<DisorderResponseDTO> toResponseDTOSet(Set<Disorder> disorders) {
        return disorders.stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toSet());
    }
}
