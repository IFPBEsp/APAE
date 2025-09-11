package br.org.apae.api_crud_pacientes.domain.repository;

import br.org.apae.api_crud_pacientes.domain.model.pessoa.Pessoa;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PessoaRepository {
  Pessoa save(Pessoa pessoa);

  Pessoa findById(UUID id);

  Page<Pessoa> findAll(Pageable pageable);

  Page<Pessoa> findByNomeIgnoreCase(String nome, Pageable pageable);

  Pessoa update(Pessoa pessoa);

  void deleteById(UUID id);
}
