package br.org.apae.api.patient.application.internal;

import br.org.apae.api.patient.domain.exceptions.VaccineMismatchException;
import br.org.apae.api.patient.domain.exceptions.VaccineNotFoundException;
import br.org.apae.api.patient.domain.model.Vaccine;
import br.org.apae.api.patient.domain.repository.PatientRepository;
import br.org.apae.api.patient.domain.repository.VaccineRepository;
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

import br.org.apae.api.common.dto.patient.request.vaccine.CreateVaccineDTO;
import br.org.apae.api.patient.domain.exceptions.VaccineConflictException;
import br.org.apae.api.patient.domain.exceptions.VaccineInUseException;
import org.springframework.dao.DataIntegrityViolationException;

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

    @Override
    @Transactional
    public VaccineResponseDTO createVaccine(CreateVaccineDTO vaccineDTO) {
        String normalizedName = vaccineDTO.name().trim();

        vaccineRepository.findByNameIgnoreCase(normalizedName)
                .ifPresent(existing -> {
                    throw new VaccineConflictException(normalizedName);
                });

        Vaccine newVaccine = new Vaccine(normalizedName);
        Vaccine saved = vaccineRepository.save(newVaccine);
        return vaccineMapper.toResponseDTO(saved);
    }

    @Override
    @Transactional
    public VaccineResponseDTO updateVaccine(UUID id, CreateVaccineDTO vaccineDTO) {
        String normalizedName = vaccineDTO.name().trim();

        Vaccine vaccineToUpdate = vaccineRepository.findById(id)
                .orElseThrow(VaccineNotFoundException::new);

        vaccineRepository.findByNameIgnoreCase(normalizedName)
                .ifPresent(existing -> {
                    if (!existing.getId().equals(id)) {
                        throw new VaccineConflictException(normalizedName);
                    }
                });

        vaccineToUpdate.updateName(normalizedName);
        Vaccine updated = vaccineRepository.save(vaccineToUpdate);
        return vaccineMapper.toResponseDTO(updated);
    }

    @Override
    @Transactional
    public void deleteVaccine(UUID id) {
        if (!vaccineRepository.existsById(id)) {
            throw new VaccineNotFoundException();
        }

        try {
            vaccineRepository.deleteById(id);
            vaccineRepository.flush();
        } catch (DataIntegrityViolationException e) {
            throw new VaccineInUseException();
        }
    }
}
