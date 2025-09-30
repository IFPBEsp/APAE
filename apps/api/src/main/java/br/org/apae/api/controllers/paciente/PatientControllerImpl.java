package br.org.apae.api.controllers.paciente;

import br.org.apae.api.paciente.application.facade.IPatientFacade;
import br.org.apae.api.paciente.interfaces.controllers.PatientController;
import br.org.apae.api.paciente.interfaces.dto.create.CreatePatientDTO;
import br.org.apae.api.paciente.interfaces.dto.response.PatientResponseDTO;
import br.org.apae.api.paciente.interfaces.dto.update.UpdatePatientDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
public class PatientControllerImpl implements PatientController {

    private final IPatientFacade patientFacade;

    public PatientControllerImpl(IPatientFacade patientFacade) {
        this.patientFacade = patientFacade;
    }

    @Override
    public ResponseEntity<Void> createPatient(CreatePatientDTO createPatientDTO) {
        patientFacade.createPatient(createPatientDTO);
        return ResponseEntity.status(201).build();
    }

    @Override
    public ResponseEntity<PatientResponseDTO> findById(UUID id) {
        PatientResponseDTO patient = patientFacade.findById(id);
        return ResponseEntity.ok(patient);
    }

    @Override
    public ResponseEntity<List<PatientResponseDTO>> findAll() {
        List<PatientResponseDTO> patients = patientFacade.findAll();
        return ResponseEntity.ok(patients);
    }

    @Override
    public ResponseEntity<List<PatientResponseDTO>> findByFilter(Map<String, String> filters) {
        List<PatientResponseDTO> patients = patientFacade.findByFilter(filters);
        return ResponseEntity.ok(patients);
    }

    @Override
    public ResponseEntity<PatientResponseDTO> updatePatient(UUID id, UpdatePatientDTO updatePatientDTO) {
        PatientResponseDTO updatedPatient = patientFacade.updatePatient(id, updatePatientDTO);
        return ResponseEntity.ok(updatedPatient);
    }

    @Override
    public ResponseEntity<Void> deletePatient(UUID id) {
        patientFacade.deletePatient(id);
        return ResponseEntity.noContent().build();
    }
}
