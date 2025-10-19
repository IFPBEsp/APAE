package br.org.apae.api.patient.application.mappers;

import br.org.apae.api.common.dto.patient.request.parent.CreateParentDTO;
import br.org.apae.api.common.dto.patient.request.parent.UpdateParentDTO;
import br.org.apae.api.common.dto.patient.response.parent.ParentResponseDTO;
import br.org.apae.api.patient.domain.model.Parent;
import br.org.apae.api.patient.domain.model.Patient;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Component;

@Component
public class ParentMapper {
    public Parent toEntity(CreateParentDTO dto, Patient patient) {
        return new Parent(
                dto.name(),
                dto.rg(),
                dto.cpf(),
                dto.isAlive(),
                dto.profession(),
                dto.kinship(),
                patient);
    }

    public List<Parent> toEntityList(List<CreateParentDTO> createParentDTOs, Patient patient) {
        return createParentDTOs.stream()
                .map(dto -> toEntity(dto, patient))
                .toList();
    }

    public List<Parent> updateEntityList(List<Parent> parents, List<UpdateParentDTO> dtoList, Patient patient) {
        return dtoList.stream()
                .map(dto -> {
                    Parent existing = findById(parents, dto.id());
                    return new Parent(
                            existing.getId(),
                            dto.name(),
                            dto.rg(),
                            dto.cpf(),
                            dto.isAlive(),
                            dto.profession(),
                            dto.kinship(),
                            patient);
                })
                .toList();
    }

    private Parent findById(List<Parent> parents, UUID id) {
        return parents.stream()
                .filter(p -> id.equals(p.getId()))
                .findFirst()
                .orElse(null);
    }

    public ParentResponseDTO toResponseDTO(Parent parent) {
        return new ParentResponseDTO(parent);
    }

    public List<ParentResponseDTO> toResponseDTOList(List<Parent> parents) {
        return parents.stream()
                .map(this::toResponseDTO)
                .toList();
    }
}
