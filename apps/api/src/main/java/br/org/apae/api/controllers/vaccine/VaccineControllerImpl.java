package br.org.apae.api.controllers.vaccine;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import br.org.apae.api.common.dto.patient.request.vaccine.CreateVaccineDTO;
import br.org.apae.api.common.dto.patient.request.vaccine.UpdateVaccineDTO;
import br.org.apae.api.common.dto.patient.response.vaccine.VaccineResponseDTO;
import br.org.apae.api.patient.application.interfaces.VaccineApplicationService;
import br.org.apae.api.patient.interfaces.controllers.VaccineController;
import jakarta.validation.Valid;

@RestController
public class VaccineControllerImpl implements VaccineController {

    private final VaccineApplicationService vaccineService;

    @Autowired
    public VaccineControllerImpl(VaccineApplicationService vaccineService) {
        this.vaccineService = vaccineService;
    }

    @Override
    public ResponseEntity<VaccineResponseDTO> findById(UUID id) {
        VaccineResponseDTO vaccine = vaccineService.findVaccineById(id);
        return ResponseEntity.ok(vaccine);
    }

    @Override
    public ResponseEntity<List<VaccineResponseDTO>> findAll() {
        List<VaccineResponseDTO> vaccines = vaccineService.findAllVaccines();
        return ResponseEntity.ok(vaccines);
    }

    @Override
    public ResponseEntity<VaccineResponseDTO> findByName(String name) {
        VaccineResponseDTO vaccine = vaccineService.findVaccineByName(name);
        return ResponseEntity.ok(vaccine);
    }

    @Override
    public ResponseEntity<VaccineResponseDTO> createVaccine(@Valid @RequestBody CreateVaccineDTO dto) {
        VaccineResponseDTO created = vaccineService.createVaccine(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @Override
    public ResponseEntity<VaccineResponseDTO> updateVaccine(@PathVariable UUID id, @Valid @RequestBody UpdateVaccineDTO dto) {
        VaccineResponseDTO updated = vaccineService.updateVaccine(id, dto);
        return ResponseEntity.ok(updated);
    }

    @Override
    public ResponseEntity<Void> deleteVaccine(@PathVariable UUID id) {
        vaccineService.deleteVaccine(id);
        return ResponseEntity.noContent().build();
    }
}
