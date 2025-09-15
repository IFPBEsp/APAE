package br.org.apae.api_crud_pacientes.infrastructure.mapper.impl.pessoa;

import br.org.apae.api_crud_pacientes.api.dtos.request.PessoaRequest;
import br.org.apae.api_crud_pacientes.api.dtos.response.*;
import br.org.apae.api_crud_pacientes.domain.model.*;
import br.org.apae.api_crud_pacientes.infrastructure.entity.*;
import br.org.apae.api_crud_pacientes.infrastructure.mapper.impl.*;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class PessoaEntityMapperHelper {

    public ContatoMapper contatoMapper;
    public CadastroAnualMapper cadastroAnualMapper;
    public VacinaMapper vacinaMapper;
    public TipoDeficienciaMapper tipoDeficienciaMapper;
    public TipoAtendimentoMapper tipoAtendimentoMapper;
    public PessoaResponsavelMapper pessoaResponsavelMapper;

    public PessoaEntityMapperHelper(
            ContatoMapper contatoMapper,
            CadastroAnualMapper cadastroAnualMapper,
            VacinaMapper vacinaMapper,
            TipoDeficienciaMapper tipoDeficienciaMapper,
            TipoAtendimentoMapper tipoAtendimentoMapper,
            PessoaResponsavelMapper pessoaResponsavelMapper
    ) {
        this.contatoMapper = contatoMapper;
        this.cadastroAnualMapper = cadastroAnualMapper;
        this.vacinaMapper = vacinaMapper;
        this.tipoDeficienciaMapper = tipoDeficienciaMapper;
        this.tipoAtendimentoMapper = tipoAtendimentoMapper;
        this.pessoaResponsavelMapper = pessoaResponsavelMapper;
    }

    public  <T, R> List<R> mapList(List<T> source, Function<T, R> mapper) {
        if (source == null) return List.of();
        return source.stream().map(mapper).collect(Collectors.toList());

    }

    public List<ContatoEntity> mapContatos(PessoaRequest request, PessoaEntity pessoaEntity) {
        return mapList(request.getContatoRequest(),
                contato -> contatoMapper.toEntity(contato, pessoaEntity));
    }

    public List<PessoaResponsavelEntity> mapResponsaveis(PessoaRequest request, PessoaEntity pessoaEntity) {
        return mapList(request.getResponsaveisRequests(),
                responsavel -> {
                    PessoaResponsavelEntity entity = pessoaResponsavelMapper.toEntity(responsavel, pessoaEntity);
                    entity.setPessoa(pessoaEntity);
                    return entity;
                });
    }

    public List<CadastroAnualEntity> mapCadastrosAnuais(PessoaRequest request, PessoaEntity pessoaEntity) {
        return mapList(request.getCadastrosAnuaisRequests(),
                cadastro -> {
                    CadastroAnualEntity entity = cadastroAnualMapper.toEntity(cadastro, pessoaEntity);
                    entity.setPessoa(pessoaEntity);
                    return entity;
                });
    }

    public List<VacinaEntity> mapVacinas(PessoaRequest request, PessoaEntity pessoaEntity) {
        return mapList(request.getVacinacoesRequests(),
                vacina -> {
                    VacinaEntity entity = vacinaMapper.toEntity(vacina, pessoaEntity);
                    entity.setPessoa(pessoaEntity);
                    return entity;
                });
    }

    public List<TipoDeficienciaEntity> mapDeficiencias(PessoaRequest request, PessoaEntity pessoaEntity) {
        return mapList(request.getDeficienciasRequests(),
                deficiencia -> {
                    TipoDeficienciaEntity entity = tipoDeficienciaMapper.toEntity(deficiencia, pessoaEntity);
                    entity.setPessoa(pessoaEntity);
                    return entity;
                });
    }

    public List<TipoAtendimentoEntity> mapAtendimentos(PessoaRequest request, PessoaEntity pessoaEntity) {
        return mapList(request.getAtendimentosRequests(),
                atendimento -> {
                    TipoAtendimentoEntity entity = tipoAtendimentoMapper.toEntity(atendimento, pessoaEntity);
                    entity.setPessoa(pessoaEntity);
                    return entity;
                });
    }

    public List<ContatoResponse> mapContatoResponses(List<ContatoEntity> contatos) {
        return mapList(contatos, contatoMapper::toResponse);
    }

    public List<PessoaResponsavelResponse> mapResponsavelResponses(List<PessoaResponsavelEntity> responsaveis) {
        return mapList(responsaveis, pessoaResponsavelMapper::toResponse);
    }

    public List<CadastroAnualResponse> mapCadastroAnualResponses(List<CadastroAnualEntity> cadastros) {
        return mapList(cadastros, cadastroAnualMapper::toResponse);
    }

    public List<VacinaResponse> mapVacinaResponses(List<VacinaEntity> vacinas) {
        return mapList(vacinas, vacinaMapper::toResponse);
    }

    public List<TipoDeficienciaResponse> mapDeficienciaResponses(List<TipoDeficienciaEntity> deficiencias) {
        return mapList(deficiencias, tipoDeficienciaMapper::toResponse);
    }

    public List<TipoAtendimentoResponse> mapAtendimentoResponses(List<TipoAtendimentoEntity> atendimentos) {
        return mapList(atendimentos, tipoAtendimentoMapper::toResponse);
    }

    public List<ContatoEntity> mapContatosFromDomain(List<Contato> contatos) {
        return mapList(contatos, contato ->  contatoMapper.toEntityFromDomain(contato));
    }

    public List<PessoaResponsavelEntity> mapResponsaveisFromDomain(List<PessoaResponsavel> responsaveis) {
        return mapList(responsaveis, r ->  pessoaResponsavelMapper.toEntityFromDomain(r));
    }

    public List<VacinaEntity> mapVacinacoesFromDomain(List<Vacina> vacinas) {
        return mapList(vacinas, v ->  vacinaMapper.toEntityFromDomain(v));
    }

    public List<TipoDeficienciaEntity> mapDeficienciasFromDomain(List<TipoDeficiencia> deficiencias) {
        return mapList(deficiencias, d ->  tipoDeficienciaMapper.toEntityFromDomain(d));
    }

    public List<CadastroAnualEntity> mapCadastrosAnuaisFromDomain(List<CadastroAnual> cadastroAnuais) {
        return mapList(cadastroAnuais, c ->  cadastroAnualMapper.toEntityFromDomain(c));
    }

    public List<TipoAtendimentoEntity> mapTiposAtendimentosFromDomain(List<TipoAtendimento> atendimentos) {
        return mapList(atendimentos, a ->  tipoAtendimentoMapper.toEntityFromDomain(a));
    }
}
