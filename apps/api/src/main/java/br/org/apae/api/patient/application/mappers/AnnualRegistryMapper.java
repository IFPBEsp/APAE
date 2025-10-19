package br.org.apae.api.patient.application.mappers;

import java.util.Set;

import org.springframework.stereotype.Component;

import br.org.apae.api.common.dto.patient.request.annual_registry.CreateAnnualRegistryDTO;
import br.org.apae.api.common.dto.patient.response.annual_registry.AnnualRegistryResponseDTO;
import br.org.apae.api.common.dto.patient.response.disorder.DisorderResponseDTO;
import br.org.apae.api.patient.domain.model.AnnualRegistry;
import br.org.apae.api.patient.domain.model.Disorder;
import br.org.apae.api.patient.domain.model.Patient;

@Component
public class AnnualRegistryMapper {
  private final DisorderMapper disorderMapper;

  public AnnualRegistryMapper(DisorderMapper disorderMapper) {
    this.disorderMapper = disorderMapper;
  }

  public AnnualRegistry toEntity(CreateAnnualRegistryDTO dto, Set<DisorderResponseDTO> disorderDtos, Patient patient) {
    Set<Disorder> disorders = this.disorderMapper.toEntitySetFromResponse(disorderDtos);

    return new AnnualRegistry(
        dto.bpc(),
        dto.diseases(),
        dto.familyIncome(),
        dto.year(),
        patient,
        disorders);
  }

  public AnnualRegistryResponseDTO toResponseDTO(AnnualRegistry entity) {
    return new AnnualRegistryResponseDTO(
        entity.getId(),
        entity.getBpc(),
        entity.getDiseases(),
        entity.getFamilyIncome(),
        entity.getYear(),
        entity.getPatient(),
        entity.getDisorders());
  }
}
