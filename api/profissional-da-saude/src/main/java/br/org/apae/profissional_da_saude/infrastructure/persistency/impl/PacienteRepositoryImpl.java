package br.org.apae.profissional_da_saude.infrastructure.persistency.impl;

import br.org.apae.profissional_da_saude.domain.model.Paciente;
import br.org.apae.profissional_da_saude.domain.repository.PacienteRepository;
import br.org.apae.profissional_da_saude.infrastructure.entity.PacienteEntity;
import br.org.apae.profissional_da_saude.infrastructure.persistency.jpa.PacienteRepositoryJpa;
import br.org.apae.profissional_da_saude.infrastructure.persistency.mapper.PacienteMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public class PacienteRepositoryImpl implements PacienteRepository {

    private final PacienteRepositoryJpa repository;

    public PacienteRepositoryImpl(PacienteRepositoryJpa repository) {
        this.repository = repository;
    }


    @Override
    public Paciente save(Paciente paciente) {
        PacienteEntity entity = PacienteMapper.toEntity(paciente);
        return PacienteMapper.toModel(this.repository.save(entity));
    }

    @Override
    public Page<Paciente> findAll(Pageable pageable) {
        return this.repository.findAll(pageable).map(PacienteMapper::toModel);
    }

    @Override
    public Optional<Paciente> findById(UUID id) {
        return this.repository.findById(id).map(PacienteMapper::toModel);
    }

    @Override
    public Paciente update(Paciente paciente) {
        return this.save(paciente);
    }

    @Override
    public void deleteById(UUID id) {
        this.repository.deleteById(id);
    }
}
