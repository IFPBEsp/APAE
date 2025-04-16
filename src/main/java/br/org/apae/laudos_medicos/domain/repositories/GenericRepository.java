package br.org.apae.laudos_medicos.domain.repositories;

import java.util.Optional;

public interface GenericRepository<T, ID> {
    T save(T entity);

    Optional<T> findById(ID id);

    void delete(Long id);
}
