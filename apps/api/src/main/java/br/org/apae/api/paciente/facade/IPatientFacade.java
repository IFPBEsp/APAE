package br.org.apae.api.paciente.facade;

import br.org.apae.api.paciente.dto.create.CreatePatientDTO;
import br.org.apae.api.paciente.dto.filter.PatientFilterDTO;
import br.org.apae.api.paciente.dto.response.PatientResponseDTO;
import br.org.apae.api.paciente.dto.update.UpdatePatientDTO;

import java.util.List;
import java.util.UUID;

public interface IPatientFacade {
    void createPatient(CreatePatientDTO createPatientDTO);

    PatientResponseDTO findById(UUID id);

    List<PatientResponseDTO> findAll();

    List<PatientResponseDTO> findByFilter(PatientFilterDTO filter);

    PatientResponseDTO updatePatient(UUID id, UpdatePatientDTO updatePatientDTO);

    void deletePatient(UUID id);
}

