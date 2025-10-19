package br.org.apae.api.patient.application.internal;

import java.util.Set;

import org.springframework.stereotype.Service;

import br.org.apae.api.common.dto.patient.request.annual_registry.CreateAnnualRegistryDTO;
import br.org.apae.api.common.dto.patient.response.annual_registry.AnnualRegistryResponseDTO;
import br.org.apae.api.common.dto.patient.response.disorder.DisorderResponseDTO;
import br.org.apae.api.patient.application.interfaces.DisorderService;
import br.org.apae.api.patient.application.mappers.AnnualRegistryMapper;
import br.org.apae.api.patient.domain.model.AnnualRegistry;
import br.org.apae.api.patient.domain.model.Patient;
import br.org.apae.api.patient.domain.repository.AnnualRegistryRepository;
import jakarta.transaction.Transactional;

@Service
public class AnnualRegistryService {
  private AnnualRegistryRepository annualRegistryRepository;
  private AnnualRegistryMapper annualRegistryMapper;
  private DisorderService disorderService;

  public AnnualRegistryService(AnnualRegistryRepository annualRegistryRepository,
      AnnualRegistryMapper annualRegistryMapper, DisorderService disorderService) {
    this.annualRegistryRepository = annualRegistryRepository;
    this.annualRegistryMapper = annualRegistryMapper;
    this.disorderService = disorderService;
  }

  @Transactional
  public AnnualRegistryResponseDTO createRegistry(CreateAnnualRegistryDTO createAnnualRegistryDTO, Patient patient) {
    Set<DisorderResponseDTO> disorderDtos = this.disorderService
        .findDisordersByNames(createAnnualRegistryDTO.disorders());

    AnnualRegistry registry = this.annualRegistryMapper.toEntity(createAnnualRegistryDTO, disorderDtos, patient);
    AnnualRegistry registrySaved = this.annualRegistryRepository.save(registry);

    return this.annualRegistryMapper.toResponseDTO(registrySaved);
  }
}
