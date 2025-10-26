package br.org.apae.api.patient.application.interfaces;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import br.org.apae.api.common.dto.patient.request.patient.CreatePatientDTO;
import br.org.apae.api.common.dto.patient.request.patient.UpdatePatientDTO;
import br.org.apae.api.common.dto.patient.response.patient.PatientResponseDTO;
import br.org.apae.api.common.dto.patient.response.patient.PatientSummaryResponseDTO;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface PatientApplicationService {
    PatientResponseDTO createPatient(CreatePatientDTO createPatientDTO);

    PatientResponseDTO findPatientById(UUID id);

    Page<PatientSummaryResponseDTO> findAllPatients(Pageable pageable);

    List<PatientSummaryResponseDTO> findPatientByFilter(Map<String, String> filters);

    PatientResponseDTO updatePatient(UUID id, UpdatePatientDTO updatePatientDTO);

    void deletePatient(UUID id);
}
