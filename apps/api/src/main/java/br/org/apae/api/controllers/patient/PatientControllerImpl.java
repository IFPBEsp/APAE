package br.org.apae.api.controllers.patient;

import br.org.apae.api.common.dto.patient.request.patient.CreatePatientDTO;
import br.org.apae.api.common.dto.patient.request.patient.UpdatePatientDTO;
import br.org.apae.api.common.dto.patient.response.patient.PatientResponseDTO;
import br.org.apae.api.common.dto.patient.response.patient.PatientSummaryResponseDTO;
import br.org.apae.api.patient.application.interfaces.PatientApplicationService;
import br.org.apae.api.patient.interfaces.controllers.PatientController;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

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
    public ResponseEntity<PatientResponseDTO> createPatient(CreatePatientDTO createPatientDTO) {
        PatientResponseDTO patientCreated = patientService.createPatient(createPatientDTO);
        return ResponseEntity.ok(patientCreated);
    }

    @Override
    public ResponseEntity<PatientResponseDTO> findById(UUID id) {
        PatientResponseDTO patient = patientService.findPatientById(id);
        return ResponseEntity.ok(patient);
    }

    @Override
    public ResponseEntity<Page<PatientSummaryResponseDTO>> findAll(Pageable pageable) {
        Page<PatientSummaryResponseDTO> patientsPage = patientService.findAllPatients(pageable);
        return ResponseEntity.ok(patientsPage);
    }

    @Override
    public ResponseEntity<List<PatientSummaryResponseDTO>> findByFilter(Map<String, String> filters) {
        List<PatientSummaryResponseDTO> patients = patientService.findPatientByFilter(filters);
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
