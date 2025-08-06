package br.org.apae.api_crud_pacientes.domain.repository;

import br.org.apae.api_crud_pacientes.domain.model.TipoAtendimento;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TipoAtendimentoRepository {
  TipoAtendimento save(TipoAtendimento tipoAtendimento);

  TipoAtendimento findById(UUID id);

  Page<TipoAtendimento> findAll(Pageable pageable);

  Page<TipoAtendimento> findByDescricaoIgnoreCase(String descricao, Pageable pageable);

  TipoAtendimento update(TipoAtendimento tipoAtendimento);

  void deleteById(UUID id);
}
