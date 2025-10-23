package br.org.apae.api.controllers.patient;

import br.org.apae.api.common.dto.patient.create.CreatePatientDTO;
import br.org.apae.api.common.dto.patient.response.PatientResponseDTO;
import br.org.apae.api.common.dto.patient.update.UpdatePatientDTO;
import br.org.apae.api.patient.application.interfaces.PatientApplicationService;
import br.org.apae.api.patient.interfaces.controllers.PatientController;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
public class PatientControllerImpl implements PatientController {

    private final PatientApplicationService patientService;

    public PatientControllerImpl(PatientApplicationService patientService) {
        this.patientService = patientService;
    }

    @Override
    public ResponseEntity<Void> createPatient(CreatePatientDTO createPatientDTO, MultipartFile document) {
        patientService.createPatient(createPatientDTO, document);
        return ResponseEntity.status(201).build();
    }

    @Override
    public ResponseEntity<PatientResponseDTO> findById(UUID id) {
        PatientResponseDTO patient = patientService.findById(id);
        return ResponseEntity.ok(patient);
    }

    @Override
    public ResponseEntity<Page<PatientResponseDTO>> findAll(Pageable pageable) {
        Page<PatientResponseDTO> patientsPage = patientService.findAll(pageable);
        return ResponseEntity.ok(patientsPage);
    }

    @Override
    public ResponseEntity<List<PatientResponseDTO>> findByFilter(Map<String, String> filters) {
        List<PatientResponseDTO> patients = patientService.findByFilter(filters);
        return ResponseEntity.ok(patients);
    }

    @Override
    public ResponseEntity<PatientResponseDTO> updatePatient(UUID id, UpdatePatientDTO updatePatientDTO) {
        PatientResponseDTO updatedPatient = patientService.updatePatient(id, updatePatientDTO);
        return ResponseEntity.ok(updatedPatient);
    }

    @Override
    public ResponseEntity<Void> deletePatient(UUID id) {
        patientService.deletePatient(id);
        return ResponseEntity.noContent().build();
    }
}
