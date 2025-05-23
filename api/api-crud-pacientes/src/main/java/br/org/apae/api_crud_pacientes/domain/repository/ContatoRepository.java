package br.org.apae.api_crud_pacientes.domain.repository;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import br.org.apae.api_crud_pacientes.domain.model.Contato;

public interface ContatoRepository extends JpaRepository<Contato, UUID> {
    Page<Contato> findByEnderecoIgnoreCase(String endereco, Pageable pageable);
}
