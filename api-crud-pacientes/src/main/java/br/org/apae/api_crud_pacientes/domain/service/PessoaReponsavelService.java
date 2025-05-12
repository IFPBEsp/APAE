package br.org.apae.api_crud_pacientes.domain.service;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import br.org.apae.api_crud_pacientes.api.dtos.pessoa_responsavel.PessoaResponsavelRequest;
import br.org.apae.api_crud_pacientes.api.dtos.pessoa_responsavel.PessoaResponsavelResponse;
import br.org.apae.api_crud_pacientes.application.pessoa_responsavel.PessoaResponsavelMapper;
import br.org.apae.api_crud_pacientes.domain.model.PessoaResponsavel;
import br.org.apae.api_crud_pacientes.domain.repository.PessoaResponsavelRepository;
import jakarta.persistence.EntityNotFoundException;

@Service
public class PessoaReponsavelService {
    private final PessoaResponsavelRepository repository;

    public PessoaReponsavelService(PessoaResponsavelRepository repository) {
        this.repository = repository;
    }

    public PessoaResponsavelResponse criarPessoaResponsavel(PessoaResponsavelRequest pessoaReponsavelRequest) {
        PessoaResponsavelMapper mapper = new PessoaResponsavelMapper();
        PessoaResponsavel pessoaResponsavel = mapper.toEntity(pessoaReponsavelRequest);
        PessoaResponsavel pessoaResponsavelSalva = repository.save(pessoaResponsavel);
        return mapper.toResponse(pessoaResponsavelSalva);
    }

    public PessoaResponsavelResponse atualizarPessoaReponsavel(UUID id, PessoaResponsavelRequest request) {
        Optional<PessoaResponsavel> optionalPessoaResponsavel = repository.findById(id);
        PessoaResponsavel pessoaResponsavelExistente;

        if (optionalPessoaResponsavel.isPresent()) {
            pessoaResponsavelExistente = optionalPessoaResponsavel.get();

            pessoaResponsavelExistente.setCpf(request.getCpf());
            pessoaResponsavelExistente.setEmergencia(request.getEmergencia());
            pessoaResponsavelExistente.setOnde_Procurar(request.getOnde_Procurar());
            pessoaResponsavelExistente.setProfissao(request.getProfissao());
            pessoaResponsavelExistente.setRg(request.getRg());
            pessoaResponsavelExistente.setVivo(request.isVivo());

            PessoaResponsavel pessoaResponsavelAtualizada = repository.save(pessoaResponsavelExistente);
            return new PessoaResponsavelMapper().toResponse(pessoaResponsavelAtualizada);
        } else {
            throw new EntityNotFoundException("Pessoa Responsável não encontrada.");
        }
    }

    public void deletarPessoaReponsavel(UUID id) {
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException();
        }
        repository.deleteById(id);
    }

    public PessoaResponsavelResponse buscarPorId(UUID id) {
        Optional<PessoaResponsavel> optionalPessoaResponsavel = repository.findById(id);
        if(optionalPessoaResponsavel.isEmpty()) {
            throw new EntityNotFoundException("Pessoa responsável não encontrada."); 
        }
        PessoaResponsavel pessoaResponsavel = optionalPessoaResponsavel.get();
        return new PessoaResponsavelMapper().toResponse(pessoaResponsavel);
    }

    public Page<PessoaResponsavelResponse> listarPessoasResponsaveis(Pageable pageable, String cpf) {
        PessoaResponsavelMapper mapper = new PessoaResponsavelMapper();
        if (cpf != null) {
            return repository.findByCpfContaining(cpf, pageable).map(mapper::toResponse);
        }
        // Mais conficionais com mais atríbutos relevantes
        return repository.findByCpfContaining(cpf, pageable).map(mapper::toResponse);
    }
}
