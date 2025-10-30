package br.org.apae.api.patient.application.interfaces;

import br.org.apae.api.common.dto.disorder.request.CreateDisorderDTO;
import br.org.apae.api.common.dto.disorder.request.UpdateDisorderDTO;
import br.org.apae.api.common.dto.disorder.response.DisorderResponseDTO;
import java.util.List;
import java.util.UUID;

public interface DisorderService {

    DisorderResponseDTO save(CreateDisorderDTO dto);

    List<DisorderResponseDTO> findAll();

    void delete(UUID id);

    DisorderResponseDTO findById(UUID id);

    DisorderResponseDTO update(UUID id, UpdateDisorderDTO dto);
}