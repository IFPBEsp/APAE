package br.org.apae.api.professional.application.internal;

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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class AvailabilityApplicationServiceImpl implements AvailabilityApplicationService {

    private final AvailabilityRepository availabilityRepository;
    private final HealthProfessionalRepository healthProfessionalRepository;
    private final AvailabilityMapper availabilityMapper;

    public AvailabilityApplicationServiceImpl(
            AvailabilityRepository availabilityRepository,
            HealthProfessionalRepository healthProfessionalRepository,
            AvailabilityMapper availabilityMapper) {
        this.availabilityRepository = availabilityRepository;
        this.healthProfessionalRepository = healthProfessionalRepository;
        this.availabilityMapper = availabilityMapper;
    }

    @Override
    @Transactional
    public AvailabilityResponseDTO createAvailability(UUID professionalId, CreateAvailabilityDTO dto) {
        HealthProfessional professional = healthProfessionalRepository.findById(professionalId)
                .orElseThrow(HealthProfessionalNotFoundException::new);

        // Validar campos obrigatórios
        if (dto.day() == null || dto.day().isBlank()) {
            throw new IllegalArgumentException("O dia não pode estar em branco");
        }
        if (dto.shift() == null || dto.shift().isBlank()) {
            throw new IllegalArgumentException("O turno não pode estar em branco");
        }

        try {
            Day day = Day.valueOf(dto.day().toUpperCase());
            Shift shift = Shift.valueOf(dto.shift().toUpperCase());

            if (availabilityRepository.existsByProfessionalAndDayAndShift(professional, day, shift)) {
                throw new AvailabilityConflictException();
            }

            Availability availability = availabilityMapper.toEntity(dto, professional);
            Availability savedAvailability = availabilityRepository.save(availability);

            return availabilityMapper.toResponseDTO(savedAvailability);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Dia ou Turno inválido: " + dto.day() + " / " + dto.shift(), e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public AvailabilityResponseDTO findAvailabilityById(UUID id) {
        return availabilityRepository.findById(id)
                .map(availabilityMapper::toResponseDTO)
                .orElseThrow(AvailabilityNotFoundException::new);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AvailabilityResponseDTO> findAllAvailabilities() {
        return availabilityRepository.findAll().stream()
                .map(availabilityMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<AvailabilityResponseDTO> findAvailabilitiesByProfessional(UUID professionalId) {
        HealthProfessional professional = healthProfessionalRepository.findById(professionalId)
                .orElseThrow(HealthProfessionalNotFoundException::new);

        return availabilityRepository.findByProfessional(professional).stream()
                .map(availabilityMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public AvailabilityResponseDTO updateAvailability(UUID id, UpdateAvailabilityDTO dto) {
        Availability availability = availabilityRepository.findById(id)
                .orElseThrow(AvailabilityNotFoundException::new);

        // Validar campos obrigatórios
        if (dto.day() == null || dto.day().isBlank()) {
            throw new IllegalArgumentException("O dia não pode estar em branco");
        }
        if (dto.shift() == null || dto.shift().isBlank()) {
            throw new IllegalArgumentException("O turno não pode estar em branco");
        }

        try {
            Day newDay = Day.valueOf(dto.day().toUpperCase());
            Shift newShift = Shift.valueOf(dto.shift().toUpperCase());

            // Verificar conflito apenas se dia ou turno mudaram
            if (!availability.getDay().equals(newDay) || !availability.getShift().equals(newShift)) {
                if (availabilityRepository.existsByProfessionalAndDayAndShift(
                        availability.getProfessional(), newDay, newShift)) {
                    throw new AvailabilityConflictException();
                }
            }

            availability.setDay(newDay);
            availability.setShift(newShift);

            Availability updatedAvailability = availabilityRepository.save(availability);
            return availabilityMapper.toResponseDTO(updatedAvailability);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Dia ou Turno inválido: " + dto.day() + " / " + dto.shift(), e);
        }
    }

    @Override
    @Transactional
    public void deleteAvailability(UUID id) {
        if (!availabilityRepository.existsById(id)) {
            throw new AvailabilityNotFoundException();
        }
        availabilityRepository.deleteById(id);
    }
}
