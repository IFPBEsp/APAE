package br.org.apae.api.patient.application.mappers;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import br.org.apae.api.common.dto.patient.request.annual_registry.CreateAnnualRegistryDTO;
import br.org.apae.api.common.dto.patient.request.annual_registry.ReplaceAnnualRegistryDTO;
import br.org.apae.api.common.dto.patient.request.annual_registry.UpdateAnnualRegistryDTO;
import br.org.apae.api.common.dto.patient.response.annual_registry.AnnualRegistryResponseDTO;
import br.org.apae.api.common.dto.patient.response.disorder.DisorderResponseDTO;
import br.org.apae.api.patient.domain.model.AnnualRegistry;
import br.org.apae.api.patient.domain.model.Disorder;

@Component
public class AnnualRegistryMapper {
    private final DisorderMapper disorderMapper;

    public AnnualRegistryMapper(DisorderMapper disorderMapper) {
        this.disorderMapper = disorderMapper;
    }

    public AnnualRegistry toEntity(CreateAnnualRegistryDTO dto, Set<DisorderResponseDTO> disorderDtos, UUID patientId) {
        Set<Disorder> disorders = this.disorderMapper.toEntitySetFromResponse(disorderDtos);

        return new AnnualRegistry(
                dto.bpc(),
                dto.diseases(),
                dto.familyIncome(),
                dto.year(),
                patientId,
                disorders);
    }

    public AnnualRegistry updateEntityFromDto(AnnualRegistry entity, UpdateAnnualRegistryDTO dto, Set<DisorderResponseDTO> disorderDtos) {

        if (dto.bpc() != null) {
            entity.setBpc(dto.bpc());
        }
        if (dto.diseases() != null) {
            entity.setDiseases(dto.diseases());
        }
        if (dto.familyIncome() != null) {
            entity.setFamilyIncome(dto.familyIncome());
        }
        if (dto.year() != null) {
            entity.setYear(dto.year());
        }

        if (disorderDtos != null) {
            Set<Disorder> disorders = this.disorderMapper.toEntitySetFromResponse(disorderDtos);
            entity.setDisorders(disorders);
        }

        return entity;
    }

    public AnnualRegistry replaceEntityFromDto(AnnualRegistry entity, ReplaceAnnualRegistryDTO dto, Set<DisorderResponseDTO> disorderDtos) {
        Set<Disorder> disorders = this.disorderMapper.toEntitySetFromResponse(disorderDtos);

        entity.setBpc(dto.bpc());
        entity.setDiseases(dto.diseases());
        entity.setFamilyIncome(dto.familyIncome());
        entity.setDisorders(disorders);

        return entity;
    }

    public AnnualRegistryResponseDTO toResponseDTO(AnnualRegistry entity) {
        Set<DisorderResponseDTO> disorderResponseDtos;

        if (entity.getDisorders() == null) {
            disorderResponseDtos = Set.of();
        } else {
            disorderResponseDtos = entity.getDisorders().stream()
                    .map(DisorderResponseDTO::new)
                    .collect(Collectors.toSet());
        }

        return new AnnualRegistryResponseDTO(entity, disorderResponseDtos);
    }
}