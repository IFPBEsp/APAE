package br.org.apae.api_crud_pacientes.domain.repository;

import br.org.apae.api_crud_pacientes.domain.model.PessoaResponsavel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.UUID;

public interface PessoaResponsavelRepository {
    PessoaResponsavel save(PessoaResponsavel pessoaResponsavel);
    PessoaResponsavel findById(UUID id);
    Page<PessoaResponsavel> findAll(Pageable pageable);
    Page<PessoaResponsavel> findByCpf(String cpf, Pageable pageable);
    PessoaResponsavel update(PessoaResponsavel pessoaResponsavel);
    void deleteById(UUID id);
}