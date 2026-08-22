package br.org.apae.api.patient.application.interfaces;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import br.org.apae.api.common.dto.patient.request.vaccine.CreateVaccineDTO;
import br.org.apae.api.common.dto.patient.request.vaccine.UpdateVaccineDTO;
import br.org.apae.api.common.dto.patient.request.vaccine.VaccineNameDTO;
import br.org.apae.api.common.dto.patient.response.vaccine.VaccineResponseDTO;

public interface VaccineApplicationService {
  VaccineResponseDTO createVaccine(CreateVaccineDTO vaccineDto);

  VaccineResponseDTO findVaccineById(UUID id);

  List<VaccineResponseDTO> findAllVaccines();

  VaccineResponseDTO findVaccineByName(String name);

  Set<VaccineResponseDTO> findVaccines(Set<VaccineNameDTO> vaccineNames);

  VaccineResponseDTO updateVaccine(UUID id, UpdateVaccineDTO vaccineDto);

  void deleteVaccine(UUID id);
}