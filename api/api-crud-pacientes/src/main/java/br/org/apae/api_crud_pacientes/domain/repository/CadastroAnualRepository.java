package br.org.apae.api_crud_pacientes.domain.repository;

import br.org.apae.api_crud_pacientes.domain.model.CadastroAnual;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CadastroAnualRepository {
  CadastroAnual save(CadastroAnual cadastroAnual);

  CadastroAnual findById(UUID id);

  Page<CadastroAnual> findAll(Pageable pageable);

  CadastroAnual update(CadastroAnual cadastroAnual);

  void deleteById(UUID id);
}
