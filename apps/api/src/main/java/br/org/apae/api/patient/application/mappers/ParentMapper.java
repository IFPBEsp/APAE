package br.org.apae.api.patient.application.mappers;

import br.org.apae.api.common.dto.patient.request.parent.CreateParentDTO;
import br.org.apae.api.common.dto.patient.request.parent.UpdateParentDTO;
import br.org.apae.api.common.dto.patient.response.parent.ParentResponseDTO;
import br.org.apae.api.patient.domain.model.Parent;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Component;

@Component
public class ParentMapper {

    public Parent toEntity(CreateParentDTO dto, UUID patientId) {
        return new Parent(
                dto.name(),
                dto.rg(),
                dto.cpf(),
                dto.isAlive(),
                dto.profession(),
                dto.kinship(),
                patientId);
    }

    public List<Parent> toEntityList(List<CreateParentDTO> dtos, UUID patientId) {
        return dtos.stream()
                .map(dto -> toEntity(dto, patientId))
                .toList();
    }

    public List<Parent> updateEntityListFromDto(List<UpdateParentDTO> updateParentDtos, UUID patientId) {
        return updateParentDtos.stream()
                .map(dto -> new Parent(
                        dto.name(),
                        dto.rg(),
                        dto.cpf(),
                        dto.isAlive(),
                        dto.profession(),
                        dto.kinship(),
                        patientId))
                .toList();
    }

    public List<Parent> toEntityListFromResponse(List<ParentResponseDTO> responseDTOs, UUID patientId) {
        return responseDTOs.stream()
                .map(dto -> new Parent(
                        dto.id(),
                        dto.name(),
                        dto.rg(),
                        dto.cpf(),
                        dto.isAlive(),
                        dto.profession(),
                        dto.kinship(),
                        patientId))
                .toList();
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
