package br.org.apae.api.professional.application.interfaces;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import br.org.apae.api.common.dto.professional.request.CreateHealthProfessionalDTO;
import br.org.apae.api.common.dto.professional.request.UpdateHealthProfessionalDTO;
import br.org.apae.api.common.dto.professional.response.HealthProfessionalResponseDTO;

import java.util.UUID;

public interface HealthProfessionalService {

    HealthProfessionalResponseDTO save(CreateHealthProfessionalDTO dto);

    Page<HealthProfessionalResponseDTO> findAll(Pageable pageable);

    void delete(UUID id);

    HealthProfessionalResponseDTO findById(UUID id);

    HealthProfessionalResponseDTO update(UUID id, UpdateHealthProfessionalDTO dto);
}