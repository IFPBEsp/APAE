package br.org.apae.api.patient.application.internal;

import br.org.apae.api.patient.domain.exceptions.VaccineConflictException;
import br.org.apae.api.patient.domain.exceptions.VaccineInUseException;
import br.org.apae.api.patient.domain.exceptions.VaccineMismatchException;
import br.org.apae.api.patient.domain.exceptions.VaccineNotFoundException;
import br.org.apae.api.patient.domain.model.Vaccine;
import br.org.apae.api.patient.domain.repository.PatientRepository;
import br.org.apae.api.patient.domain.repository.VaccineRepository;
import br.org.apae.api.common.dto.patient.request.vaccine.CreateVaccineDTO;
import br.org.apae.api.common.dto.patient.response.vaccine.VaccineResponseDTO;
import org.springframework.dao.DataIntegrityViolationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
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
    public VaccineResponseDTO createVaccine(CreateVaccineDTO vaccineDTO) {
        Optional<Vaccine> existingVaccine = vaccineRepository.findByName(vaccineDTO.name());

        if (existingVaccine.isPresent()) {
            throw new VaccineConflictException();
        }

        Vaccine newVaccine = vaccineMapper.toEntity(vaccineDTO);

        Vaccine vaccineSaved = vaccineRepository.save(newVaccine);

        return vaccineMapper.toResponseDTO(vaccineSaved);
    }

    @Override
    @Transactional
    public Set<VaccineResponseDTO> createManyVaccines(Set<CreateVaccineDTO> vaccineDTOs) {
        Set<Vaccine> savedVaccines = new HashSet<>();

        for (CreateVaccineDTO dto : vaccineDTOs) {
            Optional<Vaccine> existingVaccine = vaccineRepository.findByName(dto.name());

            if (existingVaccine.isPresent()) {
                throw new VaccineConflictException(existingVaccine.get().getName());
            }

            Vaccine vaccine = vaccineMapper.toEntity(dto);
            savedVaccines.add(vaccineRepository.save(vaccine));
        }

        return vaccineMapper.toResponseDTOSet(savedVaccines);
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
    public Set<VaccineResponseDTO> findVaccines(Set<CreateVaccineDTO> createVaccineDTOs) {
        Set<String> names = createVaccineDTOs.stream().map(dto -> dto.name())
                .collect(Collectors.toSet());

        Set<Vaccine> vaccines = vaccineRepository.findByNameInIgnoreCase(names);

        if (vaccines.size() != createVaccineDTOs.size()) {
            throw new VaccineMismatchException();
        }

        return vaccineMapper.toResponseDTOSet(vaccines);
    }

    @Override
    @Transactional
    public VaccineResponseDTO updateVaccine(UUID id, CreateVaccineDTO vaccineDTO) {
        Vaccine vaccineToUpdate = vaccineRepository.findById(id)
                .orElseThrow(VaccineNotFoundException::new);

        Optional<Vaccine> existingVaccine = vaccineRepository.findByName(vaccineDTO.name());

        if (existingVaccine.isPresent() && !existingVaccine.get().getId().equals(id)) {
            throw new VaccineConflictException(vaccineDTO.name());
        }

        vaccineToUpdate.updateName(vaccineDTO.name());
        Vaccine vaccineUpdated = vaccineRepository.save(vaccineToUpdate);

        return vaccineMapper.toResponseDTO(vaccineUpdated);
    }

    @Override
    @Transactional
    public void deleteVaccine(UUID id) {
        if (!vaccineRepository.existsById(id)) {
            throw new VaccineNotFoundException();
        }

        try {
            vaccineRepository.deleteById(id);
            // Oculto, mas poderoso: o flush() obriga o Spring a testar a deleção no banco AGORA,
            // permitindo que o catch capture o erro de integridade se o banco recusar!
            vaccineRepository.flush();
        } catch (DataIntegrityViolationException e) {
            // Adeus, gambiarra! Olá, código limpo e com semântica.
            throw new VaccineInUseException();
        }
    }
}