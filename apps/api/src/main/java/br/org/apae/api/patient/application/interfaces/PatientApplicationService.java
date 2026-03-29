package br.org.apae.api.patient.application.interfaces;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import br.org.apae.api.common.dto.patient.request.documents.CreateDocumentsDTO;
import br.org.apae.api.common.dto.patient.request.patient.CreatePatientDTO;
import br.org.apae.api.common.dto.patient.request.patient.UpdatePatientDTO;
import br.org.apae.api.common.dto.patient.response.patient.PatientResponseDTO;
import br.org.apae.api.common.dto.patient.response.patient.PatientSummaryResponseDTO;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface PatientApplicationService {

    PatientResponseDTO createPatient(CreatePatientDTO createPatientDTO, CreateDocumentsDTO documents);

    PatientResponseDTO findPatientById(UUID id);

    Page<PatientSummaryResponseDTO> findAllPatients(Pageable pageable);

    List<PatientSummaryResponseDTO> findPatientByFilter(Map<String, String> filters);

    PatientResponseDTO updatePatient(UUID id, UpdatePatientDTO updatePatientDTO);

    PatientResponseDTO updatePatientPhoto(UUID id, org.springframework.web.multipart.MultipartFile photo);

    void disablePatient(UUID id);

    void deletePatient(UUID id);

    List<String> findAllPatientCities();

}
