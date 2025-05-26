package br.org.apae.api_crud_pacientes.domain.repository;

import br.org.apae.api_crud_pacientes.domain.model.Contato;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.UUID;

public interface ContatoRepository {
    Contato save(Contato contato);
    Contato findById(UUID id);
    Page<Contato> findAll(Pageable pageable);
    Page<Contato> findByEnderecoIgnoreCase(String endereco, Pageable pageable);
    Contato update(Contato contato);
    void deleteById(UUID id);
}