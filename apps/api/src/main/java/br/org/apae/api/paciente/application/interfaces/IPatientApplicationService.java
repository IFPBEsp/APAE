package br.org.apae.api.paciente.application.interfaces;

import br.org.apae.api.common.dto.paciente.dto.create.CreatePatientDTO;
import br.org.apae.api.common.dto.paciente.dto.response.PatientResponseDTO;
import br.org.apae.api.common.dto.paciente.dto.update.UpdatePatientDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface IPatientApplicationService {
    void createPatient(CreatePatientDTO createPatientDTO);

    PatientResponseDTO findById(UUID id);

    Page<PatientResponseDTO> findAll(Pageable pageable);

    List<PatientResponseDTO> findByFilter(Map<String, String> filters);

    PatientResponseDTO updatePatient(UUID id, UpdatePatientDTO updatePatientDTO);

    void deletePatient(UUID id);
}

