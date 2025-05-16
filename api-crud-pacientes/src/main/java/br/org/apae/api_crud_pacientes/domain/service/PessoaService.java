package br.org.apae.api_crud_pacientes.domain.service;

import br.org.apae.api_crud_pacientes.api.dtos.pessoa.PessoaRequest;
import br.org.apae.api_crud_pacientes.api.dtos.pessoa.PessoaResponse;
import br.org.apae.api_crud_pacientes.application.pessoa.PessoaMapper;
import br.org.apae.api_crud_pacientes.domain.model.*;
import br.org.apae.api_crud_pacientes.domain.repository.PessoaRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class PessoaService {
    private final PessoaRepository pessoaRepository;
    private final PessoaMapper pessoaMapper;

    public PessoaService(PessoaRepository pessoaRepository, PessoaMapper pessoaMapper) {
        this.pessoaRepository = pessoaRepository;
        this.pessoaMapper = pessoaMapper;
    }

    public PessoaResponse getById(UUID id) {
        Optional<Pessoa> optionalPaciente = pessoaRepository.findById(id);
        if(optionalPaciente.isEmpty()){
            throw new EntityNotFoundException("Pessoa não encontrada");
        }

        Pessoa pessoa = optionalPaciente.get();
        return pessoaMapper.toResponse(pessoa);
    }

    public Pessoa create(PessoaRequest pessoaRequest) {
        Pessoa pessoa = pessoaMapper.toEntity(pessoaRequest);
        return pessoaRepository.save(pessoa);

    }

    public Page<PessoaResponse> getAll(Pageable pageable, String cpf, String nome) {

        if (cpf != null && nome != null) {
            return pessoaRepository.findByCpfContainingAndNomeCompletoContainingIgnoreCase(cpf, nome, pageable)
                    .map(pessoaMapper::toResponse);
        } else if (cpf != null) {
            return pessoaRepository.findByCpfContaining(cpf, pageable)
                    .map(pessoaMapper::toResponse);
        } else if (nome != null) {
            return pessoaRepository.findByNomeCompletoContainingIgnoreCase(nome, pageable)
                    .map(pessoaMapper::toResponse);
        } else {
            return pessoaRepository.findAll(pageable)
                    .map(pessoaMapper::toResponse);
        }
    }

    public PessoaResponse update(UUID id, PessoaRequest request) {
        Optional<Pessoa> optionalPessoa = pessoaRepository.findById(id);
        Pessoa pessoaExistente;

        if (optionalPessoa.isPresent()) {
            pessoaExistente = optionalPessoa.get();

            // Atualiza os campos necessários
            pessoaExistente.setNome_completo(request.getNome_completo());
            pessoaExistente.setCpf(request.getCpf());
            pessoaExistente.setData_nascimento(request.getData_nascimento());
            pessoaExistente.setNum_registro_nasc(request.getNum_registro_nasc());
            pessoaExistente.setFls(request.getFls());
            pessoaExistente.setLivro(request.getLivro());
            pessoaExistente.setCartorio(request.getCartorio());
            pessoaExistente.setRg(request.getRg());
            pessoaExistente.setData_emissao_rg(request.getData_emissao_rg());
            pessoaExistente.setOrgao_emissor_rg(request.getOrgao_emissor_rg());
            pessoaExistente.setCns(request.getCns());
            pessoaExistente.setNis(request.getNis());
            pessoaExistente.setData_cadastramento(request.getData_cadastramento());

            Pessoa pessoaAtualizada = pessoaRepository.save(pessoaExistente);
            return pessoaMapper.toResponse(pessoaAtualizada);

        } else {
            throw new EntityNotFoundException("Pessoa não encontrada");
        }
    }

    public void delete(UUID id) {
        if(!pessoaRepository.existsById(id)){
            throw new EntityNotFoundException("Pessoa não encontrada.");
        }
        pessoaRepository.deleteById(id);
    }
}
