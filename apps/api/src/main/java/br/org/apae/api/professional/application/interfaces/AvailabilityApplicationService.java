package br.org.apae.api.professional.application.interfaces;

import java.util.List;
import java.util.UUID;

import br.org.apae.api.common.dto.availability.request.CreateAvailabilityDTO;
import br.org.apae.api.common.dto.availability.request.UpdateAvailabilityDTO;
import br.org.apae.api.common.dto.availability.response.AvailabilityResponseDTO;

public interface AvailabilityApplicationService {

    AvailabilityResponseDTO createAvailability(
            UUID professionalId,
            CreateAvailabilityDTO dto
    );

    List<AvailabilityResponseDTO> findAllByProfessional(UUID professionalId);

    AvailabilityResponseDTO updateAvailability(
            UUID professionalId,
            UUID availabilityId,
            UpdateAvailabilityDTO dto
    );

    void deleteAvailability(UUID professionalId, UUID availabilityId);
}
