package br.org.apae.api.patient.application.interfaces;

import br.org.apae.api.common.dto.patient.request.disorder.CreateDisorderDTO;
import br.org.apae.api.common.dto.patient.request.disorder.UpdateDisorderDTO;
import br.org.apae.api.common.dto.patient.response.disorder.DisorderResponseDTO;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public interface DisorderApplicationService {

    DisorderResponseDTO createDisorder(CreateDisorderDTO createDisorderDto);

    List<DisorderResponseDTO> findAllDisorders();

    void deleteDisorder(UUID id);

    DisorderResponseDTO findDisorderById(UUID id);

    Set<DisorderResponseDTO> findDisorders(Set<CreateDisorderDTO> disorderNames);

    Set<DisorderResponseDTO> findDisordersFromUpdateDTOs(Set<UpdateDisorderDTO> disorderNames);

    DisorderResponseDTO updateDisorder(UUID id, UpdateDisorderDTO dto);
}