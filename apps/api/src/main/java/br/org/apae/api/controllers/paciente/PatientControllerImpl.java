package br.org.apae.api.controllers.paciente;

import br.org.apae.api.paciente.application.interfaces.IPatientApplicationService;
import br.org.apae.api.paciente.interfaces.controllers.PatientController;
import br.org.apae.api.common.dto.paciente.dto.create.CreatePatientDTO;
import br.org.apae.api.common.dto.paciente.dto.response.PatientResponseDTO;
import br.org.apae.api.common.dto.paciente.dto.update.UpdatePatientDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
public class PatientControllerImpl implements PatientController {

    private final IPatientApplicationService patientService;

    public PatientControllerImpl(IPatientApplicationService patientService) {
        this.patientService = patientService;
    }

    @Override
    public ResponseEntity<Void> createPatient(CreatePatientDTO createPatientDTO) {
        patientService.createPatient(createPatientDTO);
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
