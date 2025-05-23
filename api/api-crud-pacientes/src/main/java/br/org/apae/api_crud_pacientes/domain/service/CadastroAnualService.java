package br.org.apae.api_crud_pacientes.domain.service;

import br.org.apae.api_crud_pacientes.api.dtos.cadastro_anual.CadastroAnualRequest;
import br.org.apae.api_crud_pacientes.api.dtos.cadastro_anual.CadastroAnualResponse;
import br.org.apae.api_crud_pacientes.application.cadastro_anual.CadastroAnualMapper;
import br.org.apae.api_crud_pacientes.domain.model.CadastroAnual;
import br.org.apae.api_crud_pacientes.domain.model.Pessoa;
import br.org.apae.api_crud_pacientes.domain.repository.CadastroAnualRepository;
import br.org.apae.api_crud_pacientes.domain.repository.PessoaRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class CadastroAnualService {

    private final CadastroAnualRepository cadastroRepository;
    private final PessoaRepository pessoaRepository;
    private final CadastroAnualMapper cadastroMapper;
    private final PessoaService pessoaService;

    public CadastroAnualService(CadastroAnualRepository repository,
                                PessoaRepository pessoaRepository,
                                CadastroAnualMapper mapper,
                                PessoaService pessoaService) {
        this.cadastroRepository = repository;
        this.pessoaRepository = pessoaRepository;
        this.cadastroMapper = mapper;
        this.pessoaService = pessoaService;
    }

    public CadastroAnualResponse create(CadastroAnualRequest request) {
        Pessoa pessoa = pessoaService.getById(request.getPessoaId());
        CadastroAnual cadastro = cadastroMapper.toEntity(request, pessoa);
        return cadastroMapper.toResponse(cadastroRepository.save(cadastro));
    }

    public CadastroAnualResponse getById(UUID id) {
        Optional<CadastroAnual> cadastroOptional = cadastroRepository.findById(id);
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
        CadastroAnual cadastro = cadastroRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Cadastro não encontrado"));

        Pessoa pessoa = pessoaRepository.findById(dto.getPessoaId())
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
        CadastroAnual cadastro = cadastroRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Cadastro não encontrado"));
        cadastroRepository.delete(cadastro);
    }
}
