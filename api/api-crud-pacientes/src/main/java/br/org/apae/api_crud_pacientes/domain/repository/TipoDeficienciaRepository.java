package br.org.apae.api_crud_pacientes.domain.repository;

import br.org.apae.api_crud_pacientes.domain.model.TipoDeficiencia;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.UUID;

public interface TipoDeficienciaRepository {
    TipoDeficiencia save(TipoDeficiencia tipoDeficiencia);
    TipoDeficiencia findById(UUID id);
    Page<TipoDeficiencia> findAll(Pageable pageable);
    Page<TipoDeficiencia> findByDescricaoIgnoreCase(String descricao, Pageable pageable);
    TipoDeficiencia update(TipoDeficiencia tipoDeficiencia);
    void deleteById(UUID id);
}