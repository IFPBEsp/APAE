package br.org.apae.api.professional.application.internal;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.org.apae.api.common.dto.availability.request.CreateAvailabilityDTO;
import br.org.apae.api.common.dto.availability.request.UpdateAvailabilityDTO;
import br.org.apae.api.common.dto.availability.response.AvailabilityResponseDTO;
import br.org.apae.api.professional.application.interfaces.AvailabilityApplicationService;
import br.org.apae.api.professional.application.mappers.AvailabilityMapper;
import br.org.apae.api.professional.domain.exceptions.AvailabilityConflictException;
import br.org.apae.api.professional.domain.exceptions.AvailabilityNotFoundException;
import br.org.apae.api.professional.domain.exceptions.HealthProfessionalNotFoundException;
import br.org.apae.api.professional.domain.model.Availability;
import br.org.apae.api.professional.domain.model.HealthProfessional;
import br.org.apae.api.professional.domain.model.enums.Day;
import br.org.apae.api.professional.domain.model.enums.Shift;
import br.org.apae.api.professional.domain.repository.AvailabilityRepository;
import br.org.apae.api.professional.domain.repository.HealthProfessionalRepository;

@Service
public class AvailabilityApplicationServiceImpl implements AvailabilityApplicationService {

    private final AvailabilityRepository availabilityRepository;
    private final HealthProfessionalRepository professionalRepository;
    private final AvailabilityMapper mapper;

    public AvailabilityApplicationServiceImpl(
            AvailabilityRepository availabilityRepository,
            HealthProfessionalRepository professionalRepository,
            AvailabilityMapper mapper
    ) {
        this.availabilityRepository = availabilityRepository;
        this.professionalRepository = professionalRepository;
        this.mapper = mapper;
    }

    @Override
    @Transactional
    public AvailabilityResponseDTO createAvailability(UUID professionalId, CreateAvailabilityDTO dto) {
        HealthProfessional professional = professionalRepository.findById(professionalId)
                .orElseThrow(HealthProfessionalNotFoundException::new);

        Day day = parseDay(dto.day());
        Shift shift = parseShift(dto.shift());

        boolean conflict = availabilityRepository.existsByProfessional_IdAndDayAndShift(professionalId, day, shift);
        if (conflict) {
            throw new AvailabilityConflictException();
        }

        Availability entity = mapper.toEntity(dto, professional);
        Availability saved = availabilityRepository.save(entity);

        return mapper.toResponseDTO(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AvailabilityResponseDTO> findAllByProfessional(UUID professionalId) {
        if (!professionalRepository.existsById(professionalId)) {
            throw new HealthProfessionalNotFoundException();
        }

        return availabilityRepository.findAllByProfessional_Id(professionalId).stream()
                .map(mapper::toResponseDTO)
                .toList();
    }

    @Override
    @Transactional
    public AvailabilityResponseDTO updateAvailability(UUID professionalId, UUID availabilityId, UpdateAvailabilityDTO dto) {
        if (!professionalRepository.existsById(professionalId)) {
            throw new HealthProfessionalNotFoundException();
        }

        Availability availability = availabilityRepository.findById(availabilityId)
                .orElseThrow(AvailabilityNotFoundException::new);

        UUID ownerId = availability.getProfessional() != null ? availability.getProfessional().getId() : null;
        if (ownerId == null || !ownerId.equals(professionalId)) {
            throw new AvailabilityNotFoundException();
        }

        Day newDay = parseDay(dto.day());
        Shift newShift = parseShift(dto.shift());

        boolean isSame = newDay == availability.getDay() && newShift == availability.getShift();
        if (!isSame) {
            boolean conflict = availabilityRepository.existsByProfessional_IdAndDayAndShift(professionalId, newDay, newShift);
            if (conflict) {
                throw new AvailabilityConflictException();
            }
        }

        availability.setDay(newDay);
        availability.setShift(newShift);

        Availability saved = availabilityRepository.save(availability);
        return mapper.toResponseDTO(saved);
    }

    @Override
    @Transactional
    public void deleteAvailability(UUID professionalId, UUID availabilityId) {
        if (!professionalRepository.existsById(professionalId)) {
            throw new HealthProfessionalNotFoundException();
        }

        Availability availability = availabilityRepository.findById(availabilityId)
                .orElseThrow(AvailabilityNotFoundException::new);

        UUID ownerId = availability.getProfessional() != null ? availability.getProfessional().getId() : null;
        if (ownerId == null || !ownerId.equals(professionalId)) {
            throw new AvailabilityNotFoundException();
        }

        availabilityRepository.delete(availability);
    }

    private Day parseDay(String value) {
        try {
            return Day.valueOf(value.toUpperCase());
        } catch (Exception e) {
            throw new IllegalArgumentException("Dia inválido: " + value);
        }
    }

    private Shift parseShift(String value) {
        try {
            return Shift.valueOf(value.toUpperCase());
        } catch (Exception e) {
            throw new IllegalArgumentException("Turno inválido: " + value);
        }
    }
}
