package br.org.apae.api.patient.application.interfaces;

import br.org.apae.api.common.dto.patient.request.disorder.CreateDisorderDTO;
import br.org.apae.api.common.dto.patient.request.disorder.UpdateDisorderDTO;
import br.org.apae.api.common.dto.patient.response.disorder.DisorderResponseDTO;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public interface DisorderService {

    DisorderResponseDTO save(CreateDisorderDTO dto);

    List<DisorderResponseDTO> findAll();

    void delete(UUID id);

    DisorderResponseDTO findById(UUID id);

    Set<DisorderResponseDTO> findDisordersByNames(Set<CreateDisorderDTO> createDisorderDTOs);

    DisorderResponseDTO update(UUID id, UpdateDisorderDTO dto);
}