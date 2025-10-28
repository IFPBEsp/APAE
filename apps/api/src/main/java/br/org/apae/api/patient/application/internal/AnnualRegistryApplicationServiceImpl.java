package br.org.apae.api.patient.application.internal;

import java.util.Set;
import java.util.UUID;

import org.springframework.stereotype.Service;

import br.org.apae.api.common.dto.patient.request.annual_registry.CreateAnnualRegistryDTO;
import br.org.apae.api.common.dto.patient.response.annual_registry.AnnualRegistryResponseDTO;
import br.org.apae.api.common.dto.patient.response.disorder.DisorderResponseDTO;
import br.org.apae.api.patient.application.interfaces.AnnualRegistryApplicationService;
import br.org.apae.api.patient.application.interfaces.DisorderApplicationService;
import br.org.apae.api.patient.application.mappers.AnnualRegistryMapper;
import br.org.apae.api.patient.domain.exceptions.DisorderMismatchException;
import br.org.apae.api.patient.domain.model.AnnualRegistry;
import br.org.apae.api.patient.domain.repository.AnnualRegistryRepository;
import jakarta.transaction.Transactional;

@Service
public class AnnualRegistryApplicationServiceImpl implements AnnualRegistryApplicationService {
  private AnnualRegistryRepository annualRegistryRepository;
  private AnnualRegistryMapper annualRegistryMapper;

  private DisorderApplicationService disorderService;
  private PatientDomainService patientDomainService;

  public AnnualRegistryApplicationServiceImpl(AnnualRegistryRepository annualRegistryRepository,
      AnnualRegistryMapper annualRegistryMapper, DisorderApplicationService disorderService,
      PatientDomainService patientDomainService) {
    this.annualRegistryRepository = annualRegistryRepository;
    this.annualRegistryMapper = annualRegistryMapper;
    this.disorderService = disorderService;
    this.patientDomainService = patientDomainService;
  }

  @Override
  @Transactional
  public AnnualRegistryResponseDTO createRegistry(CreateAnnualRegistryDTO createAnnualRegistryDTO, UUID patientId) {
    patientDomainService.getByIdOrThrow(patientId);

    Set<DisorderResponseDTO> disorderDtos = disorderService
        .findDisorders(createAnnualRegistryDTO.disorders());

    if (createAnnualRegistryDTO.disorders().size() != disorderDtos.size()) {
      throw new DisorderMismatchException();
    }

    AnnualRegistry registry = annualRegistryMapper.toEntity(createAnnualRegistryDTO, disorderDtos, patientId);
    AnnualRegistry registrySaved = annualRegistryRepository.save(registry);

    return annualRegistryMapper.toResponseDTO(registrySaved);
  }
}
