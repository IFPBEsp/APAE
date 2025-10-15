package br.org.apae.api.controllers.patient;

import br.org.apae.api.patient.application.internal.VaccineService;
import br.org.apae.api.patient.domain.model.Vaccine;
import br.org.apae.api.common.dto.vaccine.create.CreateVaccineDTO;
import br.org.apae.api.common.dto.vaccine.response.ResponseVaccineDTO;
import br.org.apae.api.patient.interfaces.controllers.VaccineController;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
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
    public ResponseEntity<ResponseVaccineDTO> createVaccine(CreateVaccineDTO vaccineDTO) {
        Vaccine createdVaccine = vaccineService.create(vaccineDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(toResponseDTO(createdVaccine));
    }

    @Override
    public ResponseEntity<ResponseVaccineDTO> findById(UUID id) {
        Vaccine vaccine = vaccineService.findById(id);
        return ResponseEntity.ok(toResponseDTO(vaccine));
    }

    @Override
    public ResponseEntity<List<ResponseVaccineDTO>> findAll() {
        List<Vaccine> vaccines = vaccineService.findAll();
        List<ResponseVaccineDTO> responseDTOs = vaccines.stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responseDTOs);
    }

    @Override
    public ResponseEntity<ResponseVaccineDTO> findByName(String name) {
        Vaccine vaccine = vaccineService.findByName(name);
        return ResponseEntity.ok(toResponseDTO(vaccine));
    }

    @Override
    public ResponseEntity<ResponseVaccineDTO> updateVaccine(UUID id, CreateVaccineDTO vaccineDTO) {
        Vaccine updatedVaccine = vaccineService.update(id, vaccineDTO);
        return ResponseEntity.ok(toResponseDTO(updatedVaccine));
    }

    @Override
    public ResponseEntity<Void> deleteVaccine(UUID id) {
        vaccineService.delete(id);
        return ResponseEntity.noContent().build();
    }

    private ResponseVaccineDTO toResponseDTO(Vaccine vaccine) {
        return new ResponseVaccineDTO(vaccine.getId(), vaccine.getName());
    }
}