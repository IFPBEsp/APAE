package br.org.apae.api.patient.application.mappers;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import br.org.apae.api.common.dto.servicetype.response.ServiceTypeResponseDTO;
import br.org.apae.api.servicetype.application.mappers.ServiceTypeMapper;
import br.org.apae.api.servicetype.domain.model.ServiceType;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Component;

import br.org.apae.api.common.dto.patient.request.annualregistry.CreateAnnualRegistryDTO;
import br.org.apae.api.common.dto.patient.request.annualregistry.ReplaceAnnualRegistryDTO;
import br.org.apae.api.common.dto.patient.request.annualregistry.UpdateAnnualRegistryDTO;
import br.org.apae.api.common.dto.patient.response.annualregistry.AnnualRegistryResponseDTO;
import br.org.apae.api.common.dto.patient.response.disorder.DisorderResponseDTO;
import br.org.apae.api.patient.domain.model.AnnualRegistry;
import br.org.apae.api.patient.domain.model.Disorder;

@Component
public class AnnualRegistryMapper {
    private final DisorderMapper disorderMapper;
    private final ServiceTypeMapper serviceAreaMapper;

    public AnnualRegistryMapper(DisorderMapper disorderMapper, ServiceTypeMapper serviceAreaMapper) {
        this.disorderMapper = disorderMapper;
        this.serviceAreaMapper = serviceAreaMapper;
    }

    public AnnualRegistry toEntity(CreateAnnualRegistryDTO dto, Set<DisorderResponseDTO> disorderDtos, Set<ServiceTypeResponseDTO> serviceTypeResponseDtos, UUID patientId) {
        Set<Disorder> disorders = this.disorderMapper.toEntitySetFromResponse(disorderDtos);
        Set<ServiceType> serviceTypes = this.serviceAreaMapper.toEntitySetFromResponse(serviceTypeResponseDtos);

        return new AnnualRegistry(
                dto.bpc(),
                dto.diseases(),
                dto.continuousMedication(),
                dto.familyIncome(),
                dto.year().getValue(),
                patientId,
                disorders,
                serviceTypes
        );
    }

    public AnnualRegistry updateEntityFromDto(AnnualRegistry entity, UpdateAnnualRegistryDTO dto) {
        entity.setYear(dto.year().getValue());
        return entity;
    }

    public AnnualRegistry replaceEntityFromDto(AnnualRegistry entity, ReplaceAnnualRegistryDTO dto, Set<DisorderResponseDTO> disorderDtos, Set<ServiceTypeResponseDTO> serviceTypeResponseDtos) {
        Set<Disorder> disorders = this.disorderMapper.toEntitySetFromResponse(disorderDtos);
        Set<ServiceType> serviceTypes = this.serviceAreaMapper.toEntitySetFromResponse(serviceTypeResponseDtos);

        entity.setBpc(dto.bpc());
        entity.setDiseases(dto.diseases());
        entity.setContinuousMedication(dto.continuousMedication());
        entity.setFamilyIncome(dto.familyIncome());
        entity.setDisorders(disorders);
        entity.setServiceAreas(serviceTypes);

        return entity;
    }

    public AnnualRegistryResponseDTO toResponseDTO(AnnualRegistry entity) {
        Set<DisorderResponseDTO> disorderResponseDtos = getDisorderResponseDTOS(entity);

        Set<ServiceTypeResponseDTO> serviceTypeResponseDtos = getServiceTypeResponseDtos(entity);

        return new AnnualRegistryResponseDTO(entity, disorderResponseDtos, serviceTypeResponseDtos);
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

    private Set<ServiceTypeResponseDTO> getServiceTypeResponseDtos(AnnualRegistry entity) {
        Set<ServiceTypeResponseDTO> serviceTypeResponseDtos;

        if (entity.getServiceAreas() == null) {
            serviceTypeResponseDtos = Set.of();
        } else {
            serviceTypeResponseDtos = entity.getServiceAreas().stream()
                    .map(ServiceTypeResponseDTO::new)
                    .collect(Collectors.toSet());
        }
        return serviceTypeResponseDtos;
    }
}
