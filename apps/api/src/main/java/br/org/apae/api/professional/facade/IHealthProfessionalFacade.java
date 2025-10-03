package br.org.apae.api.professional.facade;

import br.org.apae.api.professional.dto.HealthProfessionalCreateDTO;
import br.org.apae.api.professional.dto.HealthProfessionalResponseDTO;
import br.org.apae.api.professional.dto.HealthProfessionalUpdateDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface IHealthProfessionalFacade {

    HealthProfessionalResponseDTO save(HealthProfessionalCreateDTO dto);

    Page<HealthProfessionalResponseDTO> findAll(Pageable pageable);

    void delete(UUID id);

    HealthProfessionalResponseDTO findById(UUID id);

    HealthProfessionalResponseDTO update(UUID id, HealthProfessionalUpdateDTO dto);
}