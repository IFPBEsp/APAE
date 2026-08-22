package br.org.apae.api.patient.application.interfaces;

import java.util.List;
import java.util.Set;
import java.util.UUID;

<<<<<<< HEAD
import br.org.apae.api.common.dto.patient.request.vaccine.VaccineNameDTO;
import br.org.apae.api.common.dto.patient.response.vaccine.VaccineResponseDTO;

public interface VaccineApplicationService {
=======
import br.org.apae.api.common.dto.patient.request.vaccine.CreateVaccineDTO;
import br.org.apae.api.common.dto.patient.request.vaccine.UpdateVaccineDTO;
import br.org.apae.api.common.dto.patient.response.vaccine.VaccineResponseDTO;

public interface VaccineApplicationService {
  VaccineResponseDTO createVaccine(CreateVaccineDTO vaccineDto);

>>>>>>> ea1a7055 (feat(vaccines): refatorar os formulários de criação e edição de vacinas)
  VaccineResponseDTO findVaccineById(UUID id);

  List<VaccineResponseDTO> findAllVaccines();

  VaccineResponseDTO findVaccineByName(String name);

<<<<<<< HEAD
  Set<VaccineResponseDTO> findVaccines(Set<VaccineNameDTO> vaccineNames);
=======
  Set<VaccineResponseDTO> findVaccines(Set<CreateVaccineDTO> createVaccineDtos);

  VaccineResponseDTO updateVaccine(UUID id, UpdateVaccineDTO vaccineDto);

  void deleteVaccine(UUID id);
>>>>>>> ea1a7055 (feat(vaccines): refatorar os formulários de criação e edição de vacinas)
}
