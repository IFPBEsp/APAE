package br.org.apae.api.controllers.annual_registry;

import java.util.UUID;

import org.springframework.http.ResponseEntity;

import br.org.apae.api.common.dto.patient.request.annual_registry.CreateAnnualRegistryDTO;
import br.org.apae.api.common.dto.patient.response.annual_registry.AnnualRegistryResponseDTO;
import br.org.apae.api.patient.application.interfaces.AnnualRegistryApplicationService;
import br.org.apae.api.patient.interfaces.controllers.AnnualRegistryController;
import jakarta.validation.Valid;

public class AnnualRegistryControllerImpl implements AnnualRegistryController {

  private final AnnualRegistryApplicationService annualRegistryApplicationService;

  public AnnualRegistryControllerImpl(AnnualRegistryApplicationService annualRegistryApplicationService) {
    this.annualRegistryApplicationService = annualRegistryApplicationService;
  }

  @Override
  public ResponseEntity<AnnualRegistryResponseDTO> createRegistry(UUID patientId,
      @Valid CreateAnnualRegistryDTO createAnnualRegistryDTO) {
    AnnualRegistryResponseDTO registryCreated = annualRegistryApplicationService.createRegistry(createAnnualRegistryDTO,
        patientId);

    return ResponseEntity.ok(registryCreated);
  }

}
