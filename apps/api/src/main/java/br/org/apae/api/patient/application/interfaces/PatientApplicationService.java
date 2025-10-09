package br.org.apae.api.patient.application.interfaces;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import br.org.apae.api.common.dto.patient.create.CreatePatientDTO;
import br.org.apae.api.common.dto.patient.response.PatientResponseDTO;
import br.org.apae.api.common.dto.patient.update.UpdatePatientDTO;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface PatientApplicationService {
    void createPatient(CreatePatientDTO createPatientDTO);

    PatientResponseDTO findById(UUID id);

    Page<PatientResponseDTO> findAll(Pageable pageable);

    List<PatientResponseDTO> findByFilter(Map<String, String> filters);

    PatientResponseDTO updatePatient(UUID id, UpdatePatientDTO updatePatientDTO);

    void deletePatient(UUID id);
}

