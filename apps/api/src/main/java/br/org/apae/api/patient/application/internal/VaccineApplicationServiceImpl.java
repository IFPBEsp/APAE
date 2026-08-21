package br.org.apae.api.patient.application.internal;

import br.org.apae.api.patient.domain.exceptions.VaccineMismatchException;
import br.org.apae.api.patient.domain.exceptions.VaccineNotFoundException;
import br.org.apae.api.patient.domain.exceptions.VaccineConflictException;
import br.org.apae.api.patient.domain.exceptions.VaccineInUseException;
import org.springframework.dao.DataIntegrityViolationException;

import br.org.apae.api.patient.domain.model.Vaccine;
import br.org.apae.api.patient.domain.repository.PatientRepository;
import br.org.apae.api.patient.domain.repository.VaccineRepository;

import br.org.apae.api.common.dto.patient.request.vaccine.CreateVaccineDTO;
import br.org.apae.api.common.dto.patient.request.vaccine.UpdateVaccineDTO;
import br.org.apae.api.common.dto.patient.request.vaccine.VaccineNameDTO;
import br.org.apae.api.common.dto.patient.response.vaccine.VaccineResponseDTO;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

import br.org.apae.api.patient.application.interfaces.VaccineApplicationService;
import br.org.apae.api.patient.application.mappers.VaccineMapper;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class VaccineApplicationServiceImpl implements VaccineApplicationService {

    private final VaccineRepository vaccineRepository;
    private final VaccineMapper vaccineMapper;
    private final PatientRepository patientRepository;

    @Autowired
    public VaccineApplicationServiceImpl(VaccineRepository vaccineRepository, VaccineMapper vaccineMapper, PatientRepository patientRepository) {
        this.vaccineRepository = vaccineRepository;
        this.vaccineMapper = vaccineMapper;
        this.patientRepository = patientRepository;
    }

    @Override
    @Transactional
    public VaccineResponseDTO createVaccine(CreateVaccineDTO dto) {
        if (vaccineRepository.findByName(dto.name()).isPresent()) {
            throw new VaccineConflictException(dto.name());
        }

        Vaccine vaccine = vaccineMapper.toEntity(dto);
        Vaccine savedVaccine = vaccineRepository.save(vaccine);

        return vaccineMapper.toResponseDTO(savedVaccine);
    }

    @Override
    @Transactional
    public VaccineResponseDTO updateVaccine(UUID id, UpdateVaccineDTO dto) {
        Vaccine vaccine = vaccineRepository.findById(id)
                .orElseThrow(VaccineNotFoundException::new);

        if (vaccineRepository.existsByNameIgnoreCaseAndIdNot(dto.name(), id)) {
            throw new VaccineConflictException(dto.name());
        }

        vaccine.updateName(dto.name());

        return vaccineMapper.toResponseDTO(vaccine);
    }

    @Override
    @Transactional
    public void deleteVaccine(UUID id) {
        if (!vaccineRepository.existsById(id)) {
            throw new VaccineNotFoundException();
        }

        if (patientRepository.isVaccineInUse(id)) {
            throw new VaccineInUseException();
        }

        try {
            vaccineRepository.deleteById(id);
            vaccineRepository.flush();
        } catch (DataIntegrityViolationException e) {
            throw new VaccineInUseException();
        }
    }

    @Override
    @Transactional(readOnly = true)
    public VaccineResponseDTO findVaccineById(UUID id) {
        Vaccine vaccine = vaccineRepository.findById(id)
                .orElseThrow(VaccineNotFoundException::new);

        return vaccineMapper.toResponseDTO(vaccine);
    }

    @Override
    @Transactional(readOnly = true)
    public VaccineResponseDTO findVaccineByName(String name) {
        Vaccine vaccine = vaccineRepository.findByName(name)
                .orElseThrow(
                        () -> new VaccineNotFoundException(name));

        return vaccineMapper.toResponseDTO(vaccine);
    }

    @Override
    @Transactional(readOnly = true)
    public List<VaccineResponseDTO> findAllVaccines() {
        List<Vaccine> vaccines = vaccineRepository.findAll();

        return vaccines.stream().map( vaccine -> {
            boolean inUse = patientRepository.isVaccineInUse(vaccine.getId());
            return new VaccineResponseDTO(vaccine.getId(), vaccine.getName(), inUse);
        }).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Set<VaccineResponseDTO> findVaccines(Set<VaccineNameDTO> vaccineNames) {
        Set<String> names = vaccineNames.stream().map(dto -> dto.name())
                .collect(Collectors.toSet());

        Set<Vaccine> vaccines = vaccineRepository.findByNameInIgnoreCase(names);

        if (vaccines.size() != vaccineNames.size()) {
            throw new VaccineMismatchException();
        }

        return vaccineMapper.toResponseDTOSet(vaccines);
    }
}
