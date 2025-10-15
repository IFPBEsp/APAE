package br.org.apae.api.disorder.application.interfaces;

import br.org.apae.api.common.dto.disorder.request.CreateDisorderDTO;
import br.org.apae.api.common.dto.disorder.request.UpdateDisorderDTO;
import br.org.apae.api.common.dto.disorder.response.DisorderResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface DisorderService {

    DisorderResponseDTO save(CreateDisorderDTO dto);

    Page<DisorderResponseDTO> findAll(Pageable pageable);

    void delete(UUID id);

    DisorderResponseDTO findById(UUID id);

    DisorderResponseDTO update(UUID id, UpdateDisorderDTO dto);
}