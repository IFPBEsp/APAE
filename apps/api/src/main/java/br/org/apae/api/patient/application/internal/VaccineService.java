package br.org.apae.api.patient.application.internal;

import br.org.apae.api.patient.exception.types.DataIntegrityException;
import br.org.apae.api.patient.exception.types.VaccineNotFoundException;
import br.org.apae.api.patient.domain.model.Vaccine;
import br.org.apae.api.patient.domain.repository.VaccineRepository;
import br.org.apae.api.common.dto.vaccine.create.CreateVaccineDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
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
    public Vaccine create(CreateVaccineDTO vaccineDTO) {
        Optional<Vaccine> existingVaccine = vaccineRepository.findByName(vaccineDTO.name());
        if (existingVaccine.isPresent()) {
            throw new DataIntegrityException("Já existe uma vacina cadastrada com este nome.");
        }
        Vaccine newVaccine = vaccineMapper.toEntity(vaccineDTO);
        return vaccineRepository.save(newVaccine);
    }

    @Transactional(readOnly = true)
    public Vaccine findById(UUID id) {
        return vaccineRepository.findById(id)
                .orElseThrow(() -> new VaccineNotFoundException(id));
    }

    @Transactional(readOnly = true)
    public Vaccine findByName(String name) {
        return vaccineRepository.findByName(name)
                .orElseThrow(() -> new VaccineNotFoundException("Não foi possível encontrar a vacina com o nome: " + name));
    }

    @Transactional(readOnly = true)
    public List<Vaccine> findAll() {
        return vaccineRepository.findAll();
    }

    @Transactional
    public Vaccine update(UUID id, CreateVaccineDTO vaccineDTO) {
        Vaccine vaccineToUpdate = findById(id);

        Optional<Vaccine> existingVaccine = vaccineRepository.findByName(vaccineDTO.name());
        if (existingVaccine.isPresent() && !existingVaccine.get().getId().equals(id)) {
            throw new DataIntegrityException("O nome informado já está em uso por outra vacina.");
        }

        vaccineToUpdate.updateName(vaccineDTO.name());
        return vaccineRepository.save(vaccineToUpdate);
    }

    @Transactional
    public void delete(UUID id) {
        if (!vaccineRepository.existsById(id)) {
            throw new VaccineNotFoundException(id);
        }
        vaccineRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<Vaccine> findAndValidateVaccinesByNames(List<String> names) {
        if (names == null || names.isEmpty()) {
            return List.of();
        }
        List<Vaccine> vaccines = vaccineRepository.findByNameInIgnoreCase(names);
        if (vaccines.size() != names.size()) {
            throw new DataIntegrityException("Uma ou mais vacinas com os nomes fornecidos não foram encontradas.");
        }
        return vaccines;
    }
}