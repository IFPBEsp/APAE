package br.org.apae.api.patient.application.interfaces;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import br.org.apae.api.common.dto.patient.request.vaccine.CreateVaccineDTO;
import br.org.apae.api.common.dto.patient.response.vaccine.VaccineResponseDTO;

public interface VaccineApplicationService {
  VaccineResponseDTO createVaccine(CreateVaccineDTO vaccineDto);

  Set<VaccineResponseDTO> createManyVaccines(Set<CreateVaccineDTO> vaccineDtos);

  VaccineResponseDTO findVaccineById(UUID id);

  List<VaccineResponseDTO> findAllVaccines();

  VaccineResponseDTO findVaccineByName(String name);

  Set<VaccineResponseDTO> findVaccines(Set<CreateVaccineDTO> createVaccineDtos);

  VaccineResponseDTO updateVaccine(UUID id, CreateVaccineDTO vaccineDto);

  void deleteVaccine(UUID id);
}
