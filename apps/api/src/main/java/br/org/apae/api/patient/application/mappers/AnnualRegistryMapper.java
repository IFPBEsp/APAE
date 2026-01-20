package br.org.apae.api.patient.application.mappers;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import br.org.apae.api.common.dto.servicearea.response.ServiceAreaResponseDTO;
import br.org.apae.api.servicearea.application.mappers.ServiceAreaMapper;
import br.org.apae.api.servicearea.domain.model.ServiceArea;
import org.jspecify.annotations.NonNull;
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
    private final ServiceAreaMapper serviceAreaMapper;

    public AnnualRegistryMapper(DisorderMapper disorderMapper, ServiceAreaMapper serviceAreaMapper) {
        this.disorderMapper = disorderMapper;
        this.serviceAreaMapper = serviceAreaMapper;
    }

    public AnnualRegistry toEntity(CreateAnnualRegistryDTO dto, Set<DisorderResponseDTO> disorderDtos, Set<ServiceAreaResponseDTO> serviceAreaResponseDTOS, UUID patientId) {
        Set<Disorder> disorders = this.disorderMapper.toEntitySetFromResponse(disorderDtos);
        Set<ServiceArea> serviceAreas = this.serviceAreaMapper.toEntitySetFromResponse(serviceAreaResponseDTOS);

        return new AnnualRegistry(
                dto.bpc(),
                dto.diseases(),
                dto.continuousMedication(),
                dto.familyIncome(),
                dto.year(),
                patientId,
                disorders,
                serviceAreas
        );
    }

    public AnnualRegistry updateEntityFromDto(AnnualRegistry entity, UpdateAnnualRegistryDTO dto) {
        entity.setYear(dto.year());
        return entity;
    }

    public AnnualRegistry replaceEntityFromDto(AnnualRegistry entity, ReplaceAnnualRegistryDTO dto, Set<DisorderResponseDTO> disorderDtos, Set<ServiceAreaResponseDTO> serviceAreaResponseDTOS) {
        Set<Disorder> disorders = this.disorderMapper.toEntitySetFromResponse(disorderDtos);
        Set<ServiceArea> serviceAreas = this.serviceAreaMapper.toEntitySetFromResponse(serviceAreaResponseDTOS);

        entity.setBpc(dto.bpc());
        entity.setDiseases(dto.diseases());
        entity.setContinuousMedication(dto.continuousMedication());
        entity.setFamilyIncome(dto.familyIncome());
        entity.setDisorders(disorders);
        entity.setServiceAreas(serviceAreas);

        return entity;
    }

    public AnnualRegistryResponseDTO toResponseDTO(AnnualRegistry entity) {
        Set<DisorderResponseDTO> disorderResponseDtos = getDisorderResponseDTOS(entity);

        Set<ServiceAreaResponseDTO> serviceAreaResponseDTOS = getServiceAreaResponseDTOS(entity);

        return new AnnualRegistryResponseDTO(entity, disorderResponseDtos, serviceAreaResponseDTOS);
    }

    private Set<DisorderResponseDTO> getDisorderResponseDTOS(AnnualRegistry entity) {
        Set<DisorderResponseDTO> disorderResponseDtos;
        if (entity.getDisorders() == null) {
            disorderResponseDtos = Set.of();
        } else {
            disorderResponseDtos = entity.getDisorders().stream()
                    .map(DisorderResponseDTO::new)
                    .collect(Collectors.toSet());
        }
        return disorderResponseDtos;
    }

    private Set<ServiceAreaResponseDTO> getServiceAreaResponseDTOS(AnnualRegistry entity) {
        Set<ServiceAreaResponseDTO> serviceAreaResponseDTOS;

        if (entity.getServiceAreas() == null) {
            serviceAreaResponseDTOS = Set.of();
        } else {
            serviceAreaResponseDTOS = entity.getServiceAreas().stream()
                    .map(ServiceAreaResponseDTO::new)
                    .collect(Collectors.toSet());
        }
        return serviceAreaResponseDTOS;
    }
}