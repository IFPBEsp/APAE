package br.org.apae.laudos_medicos.infrastructure.persistences;

import java.util.Optional;

import org.springframework.stereotype.Repository;
import org.yaml.snakeyaml.events.Event.ID;

import br.org.apae.laudos_medicos.domain.repositories.GenericRepository;

@Repository
public class LaudoRepositoryImpl <T, ID> implements GenericRepository<T, ID>{
    private final LaudoRepositoryImpl <T, ID> laudoRepositoryImpl;

    public LaudoRepositoryImpl(LaudoRepositoryImpl<T, ID> laudoRepositoryImpl) {
        this.laudoRepositoryImpl = laudoRepositoryImpl;
    }

    @Override
    public T save(T entity) {
        return laudoRepositoryImpl.save(entity);
    }

    @Override
    public Optional<T> findById(ID id) {
       return laudoRepositoryImpl.findById(id);
    }

    @Override
    public void delete(Long id) {
        laudoRepositoryImpl.delete(id);
    }
    


}
