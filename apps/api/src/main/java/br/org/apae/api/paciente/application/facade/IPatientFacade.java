package br.org.apae.api.paciente.application.facade;

import br.org.apae.api.paciente.interfaces.dto.create.CreatePatientDTO;
import br.org.apae.api.paciente.interfaces.dto.response.PatientResponseDTO;
import br.org.apae.api.paciente.interfaces.dto.update.UpdatePatientDTO;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface IPatientFacade {
    void createPatient(CreatePatientDTO createPatientDTO);

    PatientResponseDTO findById(UUID id);

    List<PatientResponseDTO> findAll();

    List<PatientResponseDTO> findByFilter(Map<String, String> filters);

    PatientResponseDTO updatePatient(UUID id, UpdatePatientDTO updatePatientDTO);

    void deletePatient(UUID id);
}

