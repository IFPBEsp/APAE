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
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class CadastroAnualService {

    private final CadastroAnualRepository repository;
    private final PessoaRepository pessoaRepository;
    private final CadastroAnualMapper mapper;

    public CadastroAnualService(CadastroAnualRepository repository,
                                PessoaRepository pessoaRepository,
                                CadastroAnualMapper mapper) {
        this.repository = repository;
        this.pessoaRepository = pessoaRepository;
        this.mapper = mapper;
    }

    public CadastroAnual create(CadastroAnualRequest dto) {
        CadastroAnual cadastro = mapper.toEntity(dto);
        return repository.save(cadastro);
    }

    public CadastroAnualResponse findById(UUID id) {
        CadastroAnual cadastro = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Cadastro não encontrado"));
        return mapper.toDTO(cadastro);
    }

    public List<CadastroAnualResponse> findAll() {
        return repository.findAll()
                .stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());
    }

    public CadastroAnualResponse update(UUID id, CadastroAnualRequest dto) {
        CadastroAnual cadastro = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Cadastro não encontrado"));

        Pessoa pessoa = pessoaRepository.findById(dto.getPessoaId())
                .orElseThrow(() -> new EntityNotFoundException("Pessoa não encontrada"));

        cadastro.setBeneficioDePrestacaoContinuada(dto.getBeneficioDePrestacaoContinuada());
        cadastro.setHistoricosAlergias(dto.getHistoricosAlergias());
        cadastro.setMedicacoesContinuas(dto.getMedicacoesContinuas());
        cadastro.setHistoricoDoencas(dto.getHistoricoDoencas());
        cadastro.setRendaFamiliar(dto.getRendaFamiliar());
        cadastro.setPessoa(pessoa);

        repository.save(cadastro);
        return mapper.toDTO(cadastro);
    }

    public void delete(UUID id) {
        CadastroAnual cadastro = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Cadastro não encontrado"));
        repository.delete(cadastro);
    }
}
