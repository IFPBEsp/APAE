package br.org.apae.api_crud_pacientes.domain.service;

import java.util.Optional;
import java.util.UUID;

import br.org.apae.api_crud_pacientes.domain.model.Pessoa;
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
public class PessoaResponsavelService {
    private final PessoaResponsavelRepository repository;
    private final PessoaResponsavelMapper pessoaResponsavelMapper;
    private final PessoaService pessoaService;

    public PessoaResponsavelService(PessoaResponsavelRepository repository, PessoaResponsavelMapper pessoaResponsavelMapper, PessoaService pessoaService) {
        this.repository = repository;
        this.pessoaResponsavelMapper = pessoaResponsavelMapper;
        this.pessoaService = pessoaService;
    }

    public PessoaResponsavelResponse create(PessoaResponsavelRequest pessoaReponsavelRequest, UUID pessoaId) {
        Pessoa pessoaExistente = pessoaService.getById(pessoaId);
        PessoaResponsavel pessoaResponsavel = pessoaResponsavelMapper.toEntity(pessoaReponsavelRequest, pessoaExistente);
        return pessoaResponsavelMapper.toResponse(repository.save(pessoaResponsavel));

    }

    public PessoaResponsavelResponse update(UUID id, PessoaResponsavelRequest request) {
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
            return pessoaResponsavelMapper.toResponse(pessoaResponsavelAtualizada);
        } else {
            throw new EntityNotFoundException("Pessoa Responsável não encontrada.");
        }
    }

    public void delete(UUID id) {
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException();
        }
        repository.deleteById(id);
    }

    public PessoaResponsavelResponse getById(UUID id) {
        Optional<PessoaResponsavel> optionalPessoaResponsavel = repository.findById(id);
        if(optionalPessoaResponsavel.isEmpty()) {
            throw new EntityNotFoundException("Pessoa responsável não encontrada."); 
        }
        PessoaResponsavel pessoaResponsavel = optionalPessoaResponsavel.get();
        return pessoaResponsavelMapper.toResponse(pessoaResponsavel);
    }

    public Page<PessoaResponsavelResponse> getAll(Pageable pageable, String cpf) {
        if (cpf != null) {
            return repository.findByCpfContaining(cpf, pageable).map(pessoaResponsavelMapper::toResponse);
        }
        // Mais conficionais com mais atríbutos relevantes
        return repository.findByCpfContaining(cpf, pageable).map(pessoaResponsavelMapper::toResponse);
    }
}
