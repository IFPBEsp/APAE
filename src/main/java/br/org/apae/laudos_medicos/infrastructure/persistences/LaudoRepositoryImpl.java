package br.org.apae.laudos_medicos.infrastructure.persistences;

import java.util.List;
import java.util.Optional;

import br.org.apae.laudos_medicos.domain.entities.Laudo;
import br.org.apae.laudos_medicos.domain.repositories.LaudoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class LaudoRepositoryImpl implements LaudoRepository {

    @Autowired
    private LaudoJpaRepository repository;

    @Override
    public Laudo save(Laudo laudo) {
        return this.repository.save(laudo);
    }

    @Override
    public Optional<Laudo> findById(Long id) {
        return this.repository.findById(id);
    }

    @Override
    public List<Laudo> findAll() {
        return this.repository.findAll();
    }

    @Override
    public void delete(Laudo laudo) {
        this.repository.delete(laudo);
    }

    @Override
    public List<Laudo> findLaudosByPacientId(Long idPaciente) {
        return this.repository.findByPacienteId(idPaciente);
    }
}
