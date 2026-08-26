package br.org.apae.api.patient.application.interfaces;

import br.org.apae.api.common.dto.patient.request.vaccine.CreateVaccineDTO;
import br.org.apae.api.common.dto.patient.request.vaccine.UpdateVaccineDTO;
import br.org.apae.api.common.dto.patient.request.vaccine.VaccineNameDTO;
import br.org.apae.api.common.dto.patient.response.vaccine.VaccineResponseDTO;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public interface VaccineApplicationService {

    VaccineResponseDTO createVaccine(CreateVaccineDTO createVaccineDto);

    List<VaccineResponseDTO> findAllVaccines();

    void deleteVaccine(UUID id);

    VaccineResponseDTO findVaccineById(UUID id);

    VaccineResponseDTO findVaccineByName(String name);

    Set<VaccineResponseDTO> findVaccines(Set<VaccineNameDTO> vaccineNames);

    Set<VaccineResponseDTO> findVaccinesFromUpdateDTOs(Set<UpdateVaccineDTO> vaccineNames);

    VaccineResponseDTO updateVaccine(UUID id, UpdateVaccineDTO dto);
}