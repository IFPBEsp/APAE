package br.org.apae.api.controllers.patient;

import br.org.apae.api.common.dto.patient.request.documents.CreateDocumentsDTO;
import br.org.apae.api.common.dto.patient.request.patient.CreatePatientDTO;
import br.org.apae.api.common.dto.patient.request.patient.UpdatePatientDTO;
import br.org.apae.api.common.dto.patient.response.patient.PatientResponseDTO;
import br.org.apae.api.common.dto.patient.response.patient.PatientSummaryResponseDTO;
import br.org.apae.api.patient.application.interfaces.PatientApplicationService;
import br.org.apae.api.patient.interfaces.controllers.PatientController;

import br.org.apae.api.common.dto.patient.response.disorder.DisorderResponseDTO;
import br.org.apae.api.patient.application.interfaces.AnnualRegistryApplicationService;
import br.org.apae.api.patient.application.interfaces.DisorderApplicationService;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
public class PatientControllerImpl implements PatientController {

    private final PatientApplicationService patientService;
    private final DisorderApplicationService disorderService;
    private final AnnualRegistryApplicationService annualRegistryService;

    public PatientControllerImpl(
            PatientApplicationService patientService,
            DisorderApplicationService disorderService,
            AnnualRegistryApplicationService annualRegistryService
    ) {
        this.patientService = patientService;
        this.disorderService = disorderService;
        this.annualRegistryService = annualRegistryService;
    }

    @Override
    public ResponseEntity<PatientResponseDTO> createPatient(CreatePatientDTO patient, CreateDocumentsDTO documents) {
        PatientResponseDTO patientCreated = patientService.createPatient(patient, documents);
        return ResponseEntity.status(HttpStatus.CREATED).body(patientCreated);
    }

    @Override
    public ResponseEntity<PatientResponseDTO> findById(UUID id) {
        PatientResponseDTO patient = patientService.findPatientById(id);
        return ResponseEntity.ok(patient);
    }

    @Override
    public ResponseEntity<List<PatientSummaryResponseDTO>> findWithFilters(
            String nome, String transtorno, String ano, String cidade) {
        Map<String, String> filters = new HashMap<>();
        if (nome != null) filters.put("Nome", nome);
//        if (tipoAtendimento != null) filters.put("tipo_atendimento", tipoAtendimento);
        if (transtorno != null) filters.put("transtorno", transtorno);
        if (ano != null) filters.put("ano", ano);
        if (cidade != null) filters.put("cidade", cidade);

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

    @Override
    public ResponseEntity<List<String>> getTranstornos() {
        List<DisorderResponseDTO> disorderDtos = disorderService.findAllDisorders();
        List<String> disorderNames = disorderDtos.stream()
                .map(DisorderResponseDTO::name)
                .distinct()
                .collect(Collectors.toList());
        return ResponseEntity.ok(disorderNames);
    }

    @Override
    public ResponseEntity<List<String>> getAnos() {
        List<String> anos = annualRegistryService.findAllRegistryYears();
        return ResponseEntity.ok(anos);
    }

    @Override
    public ResponseEntity<List<String>> getCidades() {
        List<String> cidades = patientService.findAllPatientCities();
        return ResponseEntity.ok(cidades);
    }
}