package br.org.apae.api.controllers.patient;

import br.org.apae.api.patient.application.internal.VaccineService;
import br.org.apae.api.patient.domain.model.Vaccine;
import br.org.apae.api.common.dto.vaccine.create.VaccineCreateDTO;
import br.org.apae.api.common.dto.vaccine.response.VaccineResponseDTO;
import br.org.apae.api.patient.interfaces.controllers.VaccineController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class VaccineControllerImpl implements VaccineController {

    private final VaccineService vaccineService;

    @Autowired
    public VaccineControllerImpl(VaccineService vaccineService) {
        this.vaccineService = vaccineService;
    }

    @Override
    public ResponseEntity<VaccineResponseDTO> createVaccine(VaccineCreateDTO vaccineDTO) {
        Vaccine createdVaccine = vaccineService.create(vaccineDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(toResponseDTO(createdVaccine));
    }

    @Override
    public ResponseEntity<VaccineResponseDTO> findById(Long id) {
        Vaccine vaccine = vaccineService.findById(id);
        return ResponseEntity.ok(toResponseDTO(vaccine));
    }

    @Override
    public ResponseEntity<Page<VaccineResponseDTO>> findAll(Pageable pageable) {
        Page<Vaccine> vaccinePage = vaccineService.findAll(pageable);
        Page<VaccineResponseDTO> responseDTOPage = vaccinePage.map(this::toResponseDTO);
        return ResponseEntity.ok(responseDTOPage);
    }

    @Override
    public ResponseEntity<VaccineResponseDTO> updateVaccine(Long id, VaccineCreateDTO vaccineDTO) {
        Vaccine updatedVaccine = vaccineService.update(id, vaccineDTO);
        return ResponseEntity.ok(toResponseDTO(updatedVaccine));
    }

    @Override
    public ResponseEntity<Void> deleteVaccine(Long id) {
        vaccineService.delete(id);
        return ResponseEntity.noContent().build();
    }

    private VaccineResponseDTO toResponseDTO(Vaccine vaccine) {
        return new VaccineResponseDTO(vaccine.getId(), vaccine.getName());
    }
}