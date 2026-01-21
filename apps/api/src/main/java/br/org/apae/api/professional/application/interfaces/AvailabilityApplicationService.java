package br.org.apae.api.professional.application.interfaces;

import br.org.apae.api.common.dto.availability.request.CreateAvailabilityDTO;
import br.org.apae.api.common.dto.availability.request.UpdateAvailabilityDTO;
import br.org.apae.api.common.dto.availability.response.AvailabilityResponseDTO;

import java.util.List;
import java.util.UUID;

public interface AvailabilityApplicationService {
    
    AvailabilityResponseDTO createAvailability(UUID professionalId, CreateAvailabilityDTO dto);
    
    AvailabilityResponseDTO findAvailabilityById(UUID id);
    
    List<AvailabilityResponseDTO> findAllAvailabilities();
    
    List<AvailabilityResponseDTO> findAvailabilitiesByProfessional(UUID professionalId);
    
    AvailabilityResponseDTO updateAvailability(UUID id, UpdateAvailabilityDTO dto);
    
    void deleteAvailability(UUID id);
}
