package br.org.apae.api.controllers.annual_registry;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import br.org.apae.api.common.dto.patient.request.annual_registry.CreateAnnualRegistryDTO;
import br.org.apae.api.common.dto.patient.request.annual_registry.ReplaceAnnualRegistryDTO;
import br.org.apae.api.common.dto.patient.request.annual_registry.UpdateAnnualRegistryDTO;
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
    public ResponseEntity<AnnualRegistryResponseDTO> createRegistry(
            @PathVariable("id") UUID patientId,
            @RequestBody @Valid CreateAnnualRegistryDTO createAnnualRegistryDTO) {
        AnnualRegistryResponseDTO registryCreated = annualRegistryApplicationService.createRegistry(createAnnualRegistryDTO,
                patientId);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(registryCreated);
    }

    @Override
    public ResponseEntity<AnnualRegistryResponseDTO> getRegistryByYear(
            @PathVariable("id") UUID patientId,
            @PathVariable("year") Integer year) {

        AnnualRegistryResponseDTO registryDto = annualRegistryApplicationService
                .findRegistryByPatientAndYear(patientId, year);
        return ResponseEntity.ok(registryDto);
    }

    @Override
    public ResponseEntity<AnnualRegistryResponseDTO> updateRegistry(
            @PathVariable("id") UUID patientId,
            @PathVariable("registryId") UUID registryId,
            @RequestBody @Valid UpdateAnnualRegistryDTO updateDto) {

        AnnualRegistryResponseDTO registryDto = annualRegistryApplicationService
                .updateRegistry(patientId, registryId, updateDto);
        return ResponseEntity.ok(registryDto);
    }

    @Override
    public ResponseEntity<AnnualRegistryResponseDTO> replaceRegistry(
            @PathVariable("id") UUID patientId,
            @PathVariable("registryId") UUID registryId,
            @RequestBody @Valid ReplaceAnnualRegistryDTO replaceDto) {

        AnnualRegistryResponseDTO registryDto = annualRegistryApplicationService
                .replaceRegistry(patientId, registryId, replaceDto);
        return ResponseEntity.ok(registryDto);
    }

    @Override
    public ResponseEntity<Void> deleteRegistry(
            @PathVariable("id") UUID patientId,
            @PathVariable("registryId") UUID registryId) {

        annualRegistryApplicationService.deleteRegistry(patientId, registryId);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<List<Integer>> getRegistryYears(@PathVariable("id") UUID patientId) {
        List<Integer> years = annualRegistryApplicationService.listYearsByPatient(patientId);
        return ResponseEntity.ok(years);
    }
}