package br.org.apae.api.patient.application.internal;

import br.org.apae.api.patient.exception.types.DataIntegrityException;
import br.org.apae.api.patient.exception.types.VaccineNotFoundException;
import br.org.apae.api.patient.domain.model.Vaccine;
import br.org.apae.api.patient.domain.repository.VaccineRepository;
import br.org.apae.api.common.dto.vaccine.create.VaccineCreateDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.Optional;

@Service
public class VaccineService {

    private final VaccineRepository vaccineRepository;

    @Autowired
    public VaccineService(VaccineRepository vaccineRepository) {
        this.vaccineRepository = vaccineRepository;
    }

    @Transactional
    public Vaccine create(VaccineCreateDTO vaccineDTO) {
        Optional<Vaccine> existingVaccine = vaccineRepository.findByName(vaccineDTO.getName());
        if (existingVaccine.isPresent()) {
            throw new DataIntegrityException("Já existe uma vacina cadastrada com este nome.");
        }

        Vaccine newVaccine = new Vaccine(vaccineDTO.getName());
        return vaccineRepository.save(newVaccine);
    }

    @Transactional(readOnly = true)
    public Vaccine findById(Long id) {
        return vaccineRepository.findById(id)
                .orElseThrow(() -> new VaccineNotFoundException(id));
    }

    @Transactional(readOnly = true)
    public Page<Vaccine> findAll(Pageable pageable) {
        return vaccineRepository.findAll(pageable);
    }

    @Transactional
    public Vaccine update(Long id, VaccineCreateDTO vaccineDTO) {
        Vaccine vaccineToUpdate = findById(id);

        Optional<Vaccine> existingVaccine = vaccineRepository.findByName(vaccineDTO.getName());
        if (existingVaccine.isPresent() && !existingVaccine.get().getId().equals(id)) {
            throw new DataIntegrityException("O nome informado já está em uso por outra vacina.");
        }

        vaccineToUpdate.mapForUpdate(vaccineDTO.getName());
        return vaccineRepository.save(vaccineToUpdate);
    }

    @Transactional
    public void delete(Long id) {
        if (!vaccineRepository.existsById(id)) {
            throw new VaccineNotFoundException(id);
        }
        vaccineRepository.deleteById(id);
    }
}