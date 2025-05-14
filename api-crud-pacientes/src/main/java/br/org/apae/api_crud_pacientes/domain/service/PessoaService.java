package br.org.apae.api_crud_pacientes.domain.service;

import br.org.apae.api_crud_pacientes.api.dtos.cadastro_anual.CadastroAnualRequest;
import br.org.apae.api_crud_pacientes.api.dtos.pessoa.PessoaRequest;
import br.org.apae.api_crud_pacientes.api.dtos.pessoa.PessoaResponse;
import br.org.apae.api_crud_pacientes.api.dtos.pessoa_responsavel.PessoaResponsavelRequest;
import br.org.apae.api_crud_pacientes.api.dtos.tipo_atendimento.TipoAtendimentoRequest;
import br.org.apae.api_crud_pacientes.api.dtos.tipo_deficiencia.TipoDeficienciaRequest;
import br.org.apae.api_crud_pacientes.api.dtos.vacina.VacinaRequest;
import br.org.apae.api_crud_pacientes.application.pessoa.PessoaMapper;
import br.org.apae.api_crud_pacientes.domain.model.*;
import br.org.apae.api_crud_pacientes.domain.repository.PessoaRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class PessoaService {
    private final PessoaRepository pessoaRepository;
    private final ContatoService contatoService;
    private final VacinaService vacinaService;
    private final TipoDeficienciaService tipoDeficienciaService;
    private final TipoAtendimentoService tipoAtendimentoService;
    private final PessoaResponsavelService pessoaResponsavelService;
    private final CadastroAnualService cadastroAnualService;

    public PessoaService(PessoaRepository pessoaRepository, ContatoService contatoService, VacinaService vacinaService, TipoDeficienciaService tipoDeficienciaService, TipoAtendimentoService tipoAtendimentoService, PessoaResponsavelService pessoaResponsavelService, CadastroAnualService cadastroAnualService) {
        this.pessoaRepository = pessoaRepository;
        this.contatoService = contatoService;
        this.vacinaService = vacinaService;
        this.tipoDeficienciaService = tipoDeficienciaService;
        this.tipoAtendimentoService = tipoAtendimentoService;
        this.pessoaResponsavelService = pessoaResponsavelService;
        this.cadastroAnualService = cadastroAnualService;
    }

    public PessoaResponse getById(UUID id) {
        Optional<Pessoa> optionalPaciente = pessoaRepository.findById(id);
        if(optionalPaciente.isEmpty()){
            throw new EntityNotFoundException("Pessoa não encontrada");
        }

        Pessoa pessoa = optionalPaciente.get();
        return new PessoaMapper().toResponse(pessoa);
    }

    public PessoaResponse create(PessoaRequest request) {
        PessoaMapper mapper = new PessoaMapper();
        Pessoa pessoa = mapper.toEntity(request);
        Pessoa pessoaSalva = pessoaRepository.save(pessoa);

        // 1. Contato
        if (request.getContatoRequest() != null) {
            Contato contato = contatoService.create(request.getContatoRequest(), pessoaSalva);
            pessoaSalva.setContato(contato);
        }

        // 2. Vacinas
        if (request.getVacinacoesRequests() != null && !request.getVacinacoesRequests().isEmpty()) {
            List<Vacina> vacinas = new ArrayList<>();
            for (VacinaRequest v : request.getVacinacoesRequests()) {
                vacinas.add(vacinaService.create(v, pessoaSalva));
            }
            pessoaSalva.setVacinacoes(vacinas);
        }

        // 3. Deficiências
        if (request.getDeficienciasRequests() != null && !request.getDeficienciasRequests().isEmpty()) {
            List<TipoDeficiencia> deficiencias = new ArrayList<>();
            for (TipoDeficienciaRequest d : request.getDeficienciasRequests()) {
                deficiencias.add(tipoDeficienciaService.create(d, pessoaSalva));
            }
            pessoaSalva.setDeficiencias(deficiencias);
        }

        // 4. Atendimentos
        if (request.getAtendimentosRequests() != null && !request.getAtendimentosRequests().isEmpty()) {
            List<TipoAtendimento> atendimentos = new ArrayList<>();
            for (TipoAtendimentoRequest a : request.getAtendimentosRequests()) {
                atendimentos.add(tipoAtendimentoService.create(a, pessoaSalva));
            }
            pessoaSalva.setTiposAtendimentos(atendimentos);
        }

        // 5. Responsáveis
        if (request.getResponsaveisRequests() != null && !request.getResponsaveisRequests().isEmpty()) {
            List<PessoaResponsavel> responsaveis = new ArrayList<>();
            for (PessoaResponsavelRequest r : request.getResponsaveisRequests()) {
                responsaveis.add(pessoaResponsavelService.create(r, pessoaSalva));
            }
            pessoaSalva.setResponsaveis(responsaveis);
        }

        // 6. Cadastros Anuais
        if (request.getCadastrosAnuaisRequests() != null && !request.getCadastrosAnuaisRequests().isEmpty()) {
            List<CadastroAnual> cadastros = new ArrayList<>();
            for (CadastroAnualRequest c : request.getCadastrosAnuaisRequests()) {
                cadastros.add(cadastroAnualService.create(c, pessoaSalva));
            }
            pessoaSalva.setCadastrosAnuais(cadastros);
        }

        // Salva tudo de uma vez após vincular as entidades
        pessoaSalva = pessoaRepository.save(pessoaSalva);

        return mapper.toResponse(pessoaSalva);
    }


    public Page<PessoaResponse> getALl(Pageable pageable, String cpf, String nome) {
        PessoaMapper mapper = new PessoaMapper();

        if (cpf != null && nome != null) {
            return pessoaRepository.findByCpfContainingAndNomeCompletoContainingIgnoreCase(cpf, nome, pageable)
                    .map(mapper::toResponse);
        } else if (cpf != null) {
            return pessoaRepository.findByCpfContaining(cpf, pageable)
                    .map(mapper::toResponse);
        } else if (nome != null) {
            return pessoaRepository.findByNomeCompletoContainingIgnoreCase(nome, pageable)
                    .map(mapper::toResponse);
        } else {
            return pessoaRepository.findAll(pageable)
                    .map(mapper::toResponse);
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
            return new PessoaMapper().toResponse(pessoaAtualizada);

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
