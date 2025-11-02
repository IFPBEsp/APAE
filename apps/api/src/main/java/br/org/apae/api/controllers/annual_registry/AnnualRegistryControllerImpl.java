package br.org.apae.api.controllers.annual_registry;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import br.org.apae.api.common.dto.patient.request.annual_registry.CreateAnnualRegistryDTO;
import br.org.apae.api.common.dto.patient.response.annual_registry.AnnualRegistryResponseDTO;
import br.org.apae.api.patient.application.interfaces.AnnualRegistryApplicationService;
import br.org.apae.api.patient.interfaces.controllers.AnnualRegistryController;
import jakarta.validation.Valid;

@RestController
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

    return ResponseEntity
        .status(HttpStatus.CREATED)
        .body(registryCreated);
  }
}
