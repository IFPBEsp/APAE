package br.org.apae.api.patient.application.internal;

import br.org.apae.api.patient.exception.types.DataIntegrityException;
import br.org.apae.api.patient.exception.types.VaccineNotFoundException;
import br.org.apae.api.patient.domain.model.Vaccine;
import br.org.apae.api.patient.domain.repository.VaccineRepository;
import br.org.apae.api.common.dto.patient.request.vaccine.CreateVaccineDTO;
import br.org.apae.api.common.dto.patient.response.vaccine.VaccineResponseDTO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import br.org.apae.api.patient.application.mappers.VaccineMapper;
import java.util.UUID;

@Service
public class VaccineService {

    private final VaccineRepository vaccineRepository;
    private final VaccineMapper vaccineMapper;

    @Autowired
    public VaccineService(VaccineRepository vaccineRepository, VaccineMapper vaccineMapper) {
        this.vaccineRepository = vaccineRepository;
        this.vaccineMapper = vaccineMapper;
    }

    @Transactional
    public VaccineResponseDTO createVaccine(CreateVaccineDTO vaccineDTO) {
        Optional<Vaccine> existingVaccine = vaccineRepository.findByName(vaccineDTO.name());

        if (existingVaccine.isPresent()) {
            throw new DataIntegrityException("Já existe uma vacina cadastrada com este nome.");
        }

        Vaccine newVaccine = vaccineMapper.toEntity(vaccineDTO);

        Vaccine vaccineSaved = vaccineRepository.save(newVaccine);

        return this.vaccineMapper.toResponseDTO(vaccineSaved);
    }

    @Transactional
    public Set<VaccineResponseDTO> createVaccines(Set<CreateVaccineDTO> vaccineDTOs) {
        Set<Vaccine> savedVaccines = new HashSet<>();

        for (CreateVaccineDTO dto : vaccineDTOs) {
            Optional<Vaccine> existingVaccine = vaccineRepository.findByName(dto.name());
            if (existingVaccine.isPresent()) {
                throw new DataIntegrityException("Já existe uma vacina cadastrada com o nome: " + dto.name());
            }

            Vaccine vaccine = vaccineMapper.toEntity(dto);
            savedVaccines.add(vaccineRepository.save(vaccine));
        }

        return this.vaccineMapper.toResponseDTOSet(savedVaccines);
    }

    @Transactional(readOnly = true)
    public VaccineResponseDTO findById(UUID id) {
        Vaccine vaccine = vaccineRepository.findById(id)
                .orElseThrow(() -> new VaccineNotFoundException(id));

        return this.vaccineMapper.toResponseDTO(vaccine);
    }

    @Transactional(readOnly = true)
    public VaccineResponseDTO findByName(String name) {
        Vaccine vaccine = vaccineRepository.findByName(name)
                .orElseThrow(
                        () -> new VaccineNotFoundException("Não foi possível encontrar a vacina com o nome: " + name));

        return this.vaccineMapper.toResponseDTO(vaccine);
    }

    @Transactional(readOnly = true)
    public List<VaccineResponseDTO> findAll() {
        List<Vaccine> vaccines = vaccineRepository.findAll();

        return vaccines.stream().map(this.vaccineMapper::toResponseDTO).toList();
    }

    @Transactional
    public VaccineResponseDTO update(UUID id, CreateVaccineDTO vaccineDTO) {
        Vaccine vaccineToUpdate = this.vaccineRepository.findById(id)
                .orElseThrow(() -> new VaccineNotFoundException("Não foi possível encontrar a vacina."));

        Optional<Vaccine> existingVaccine = vaccineRepository.findByName(vaccineDTO.name());

        if (existingVaccine.isPresent() && !existingVaccine.get().getId().equals(id)) {
            throw new DataIntegrityException("O nome informado já está em uso por outra vacina.");
        }

        vaccineToUpdate.updateName(vaccineDTO.name());
        Vaccine vaccineUpdated = vaccineRepository.save(vaccineToUpdate);

        return this.vaccineMapper.toResponseDTO(vaccineUpdated);
    }

    @Transactional
    public void delete(UUID id) {
        if (!vaccineRepository.existsById(id)) {
            throw new VaccineNotFoundException(id);
        }
        vaccineRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public Set<VaccineResponseDTO> findVaccinesByNames(Set<CreateVaccineDTO> createVaccineDTOs) {
        if (createVaccineDTOs == null || createVaccineDTOs.isEmpty()) {
            return Set.of();
        }

        List<String> names = createVaccineDTOs.stream().map(dto -> dto.name()).toList();

        Set<Vaccine> vaccines = vaccineRepository.findByNameInIgnoreCase(names);

        if (vaccines.size() != createVaccineDTOs.size()) {
            throw new DataIntegrityException("Uma ou mais vacinas com os nomes fornecidos não foram encontradas.");
        }

        return vaccineMapper.toResponseDTOSet(vaccines);
    }
}