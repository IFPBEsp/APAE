package br.org.apae.api_crud_pacientes.domain.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import br.org.apae.api_crud_pacientes.api.dtos.request.CadastroAnualRequest;
import br.org.apae.api_crud_pacientes.api.dtos.response.CadastroAnualResponse;
import br.org.apae.api_crud_pacientes.infrastructure.entity.CadastroAnualEntity;
import br.org.apae.api_crud_pacientes.infrastructure.entity.PessoaEntity;
import br.org.apae.api_crud_pacientes.infrastructure.mapper.impl.CadastroAnualMapper;
import br.org.apae.api_crud_pacientes.infrastructure.percistency.jpa.CadastroAnualRepositoryJpa;
import br.org.apae.api_crud_pacientes.infrastructure.percistency.jpa.PessoaRepositoryJpa;
import jakarta.persistence.EntityNotFoundException;

@Service
public class CadastroAnualService {

    private final CadastroAnualRepositoryJpa cadastroRepository;
    private final PessoaRepositoryJpa pessoaRepository;
    private final CadastroAnualMapper cadastroMapper;
    private final PessoaService pessoaService;

    public CadastroAnualService(CadastroAnualRepositoryJpa repository,
                                PessoaRepositoryJpa pessoaRepository,
                                CadastroAnualMapper mapper,
                                PessoaService pessoaService) {
        this.cadastroRepository = repository;
        this.pessoaRepository = pessoaRepository;
        this.cadastroMapper = mapper;
        this.pessoaService = pessoaService;
    }

    public CadastroAnualResponse create(CadastroAnualRequest request) {
        PessoaEntity pessoa = pessoaService.getById(request.getPessoaId());
        CadastroAnualEntity cadastro = cadastroMapper.toEntity(request, pessoa);
        return cadastroMapper.toResponse(cadastroRepository.save(cadastro));
    }

    public CadastroAnualResponse getById(UUID id) {
        Optional<CadastroAnualEntity> cadastroOptional = cadastroRepository.findById(id);
        if (cadastroOptional.isEmpty()) {
            throw new EntityNotFoundException("Cadastro Anual não encontrado.");
        }
        return cadastroMapper.toResponse(cadastroOptional.get());
    }

    public List<CadastroAnualResponse> getAll() {
        return cadastroRepository.findAll()
                .stream()
                .map(cadastroMapper::toResponse)
                .collect(Collectors.toList());
    }

    public CadastroAnualResponse update(UUID id, CadastroAnualRequest dto) {
        CadastroAnualEntity cadastro = cadastroRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Cadastro não encontrado"));

        PessoaEntity pessoa = pessoaRepository.findById(dto.getPessoaId())
                .orElseThrow(() -> new EntityNotFoundException("Pessoa não encontrada"));

        cadastro.setBeneficioDePrestacaoContinuada(dto.getBeneficioDePrestacaoContinuada());
        cadastro.setHistoricosAlergias(dto.getHistoricosAlergias());
        cadastro.setMedicacoesContinuas(dto.getMedicacoesContinuas());
        cadastro.setHistoricoDoencas(dto.getHistoricoDoencas());
        cadastro.setRendaFamiliar(dto.getRendaFamiliar());
        cadastro.setPessoa(pessoa);

        cadastroRepository.save(cadastro);
        return cadastroMapper.toResponse(cadastro);
    }

    public void delete(UUID id) {
        CadastroAnualEntity cadastro = cadastroRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Cadastro não encontrado"));
        cadastroRepository.delete(cadastro);
    }
}
