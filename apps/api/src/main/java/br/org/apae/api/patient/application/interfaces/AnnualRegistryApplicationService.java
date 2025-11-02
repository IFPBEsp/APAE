package br.org.apae.api.patient.application.interfaces;

import java.util.UUID;

import br.org.apae.api.common.dto.patient.request.annual_registry.CreateAnnualRegistryDTO;
import br.org.apae.api.common.dto.patient.response.annual_registry.AnnualRegistryResponseDTO;

public interface AnnualRegistryApplicationService {
  AnnualRegistryResponseDTO createRegistry(CreateAnnualRegistryDTO createAnnualRegistryDto, UUID patientId);

  void deleteRegistry(UUID patientId);
}
