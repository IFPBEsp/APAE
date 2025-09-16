package br.org.apae.api_crud_pacientes.infrastructure.mapper.impl.pessoa;

import br.org.apae.api_crud_pacientes.api.dtos.request.PessoaRequest;
import br.org.apae.api_crud_pacientes.api.dtos.response.*;
import br.org.apae.api_crud_pacientes.domain.model.*;
import br.org.apae.api_crud_pacientes.domain.model.pessoa.Pessoa;
import br.org.apae.api_crud_pacientes.domain.model.pessoa.VO.DadosSociais;
import br.org.apae.api_crud_pacientes.domain.model.pessoa.VO.Identidade;
import br.org.apae.api_crud_pacientes.domain.model.pessoa.VO.RegistroCivil;
import br.org.apae.api_crud_pacientes.infrastructure.entity.*;
import br.org.apae.api_crud_pacientes.infrastructure.mapper.PessoaMapperInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class PessoaMapper implements PessoaMapperInterface<PessoaRequest, PessoaResponse, PessoaEntity, Pessoa> {

    private PessoaEntityMapperHelper mapperHelper;

    @Autowired
    public PessoaMapper(PessoaEntityMapperHelper mapperHelper) {
        this.mapperHelper = mapperHelper;
    }

    @Override
    public PessoaEntity toEntity(PessoaRequest request) {
        PessoaEntity pessoaEntity = new PessoaEntity();

        pessoaEntity.setNomeCompleto(request.getNomeCompleto());
        pessoaEntity.setDataNascimento(request.getDataNascimento());
        pessoaEntity.setNumRegistroNasc(request.getNumRegistroNasc());
        pessoaEntity.setFls(request.getFls());
        pessoaEntity.setLivro(request.getLivro());
        pessoaEntity.setCartorio(request.getCartorio());
        pessoaEntity.setCpf(request.getCpf());
        pessoaEntity.setRg(request.getRg());
        pessoaEntity.setDataEmissaoRg(request.getDataEmissaoRg());
        pessoaEntity.setOrgaoEmissorRg(request.getOrgaoEmissorRg());
        pessoaEntity.setCns(request.getCns());
        pessoaEntity.setNis(request.getNis());

        pessoaEntity.setDataCadastramento(request.getDataCadastramento());
        pessoaEntity.setContatos(mapperHelper.mapContatos(request, pessoaEntity));
        pessoaEntity.setCadastrosAnuais(mapperHelper.mapCadastrosAnuais(request, pessoaEntity));
        pessoaEntity.setVacinacoes(mapperHelper.mapVacinas(request, pessoaEntity));
        pessoaEntity.setTiposAtendimentos(mapperHelper.mapAtendimentos(request, pessoaEntity));
        pessoaEntity.setDeficiencias(mapperHelper.mapDeficiencias(request, pessoaEntity));
        pessoaEntity.setResponsaveis(mapperHelper.mapResponsaveis(request, pessoaEntity));

        return pessoaEntity;
    }

    @Override
    public PessoaResponse toResponse(PessoaEntity pessoa) {
        PessoaResponse response = new PessoaResponse();

        response.setId(pessoa.getId());
        response.setNomeCompleto(pessoa.getNomeCompleto());
        response.setDataNascimento(pessoa.getDataNascimento());
        response.setNumRegistroNasc(pessoa.getNumRegistroNasc());
        response.setFls(pessoa.getFls());
        response.setLivro(pessoa.getLivro());
        response.setCartorio(pessoa.getCartorio());
        response.setCpf(pessoa.getCpf());
        response.setRg(pessoa.getRg());
        response.setDataEmissaoRg(pessoa.getDataEmissaoRg());
        response.setOrgaoEmissorRg(pessoa.getOrgaoEmissorRg());
        response.setCns(pessoa.getCns());
        response.setNis(pessoa.getNis());
        response.setDataCadastramento(pessoa.getDataCadastramento());

        response.setContatoResponse(mapperHelper.mapContatoResponses(pessoa.getContatos()));
        response.setResponsaveisResponses(mapperHelper.mapResponsavelResponses(pessoa.getResponsaveis()));
        response.setCadastrosAnuaisResponses(mapperHelper.mapCadastroAnualResponses(pessoa.getCadastrosAnuais()));
        response.setVacinasResponses(mapperHelper.mapVacinaResponses(pessoa.getVacinacoes()));
        response.setDeficienciasResponses(mapperHelper.mapDeficienciaResponses(pessoa.getDeficiencias()));
        response.setAtendimentosResponses(mapperHelper.mapAtendimentoResponses(pessoa.getTiposAtendimentos()));

        return response;
    }

    @Override
    public Pessoa toDomain(PessoaEntity entity) {
        Identidade identidade = new Identidade(
                entity.getNomeCompleto(),
                entity.getDataNascimento(),
                entity.getCpf(),
                entity.getRg(),
                entity.getDataEmissaoRg(),
                entity.getOrgaoEmissorRg()
        );

        RegistroCivil registroCivil = new RegistroCivil(
                entity.getNumRegistroNasc(),
                entity.getFls(),
                entity.getLivro(),
                entity.getCartorio()
        );

        DadosSociais dadosSociais = new DadosSociais(
                entity.getCns(),
                entity.getNis(),
                entity.getDataCadastramento()
        );

        List<PessoaResponsavel> responsavels = extrairResponsaveis(entity);
        List<CadastroAnual> cadastroAnuals = extrairCadastrosAnuais(entity);
        List<Vacina> vacinacoes = extrairVacinacoes(entity);
        List<TipoDeficiencia> deficiencias = extrairDeficiencias(entity);
        List<TipoAtendimento> tiposAtendimentos = extrairTiposAtendimentos(entity);
        List<Contato> contatos = extrairContatos(entity);

        return new Pessoa.Builder()
                .id(entity.getId())
                .identidade(identidade)
                .registroCivil(registroCivil)
                .dadosSociais(dadosSociais)
                .responsaveis(responsavels)
                .cadastrosAnuais(cadastroAnuals)
                .vacinacoes(vacinacoes)
                .deficiencias(deficiencias)
                .contatos(contatos)
                .tiposAtendimentos(tiposAtendimentos)
                .build();
    }

    @Override
    public PessoaEntity toEntityFromDomain(Pessoa domain) {
        PessoaEntity pessoaEntity = new PessoaEntity();

        pessoaEntity.setId(domain.getId());
        pessoaEntity.setNomeCompleto(domain.getIdentidade().getNomeCompleto());
        pessoaEntity.setDataNascimento(domain.getIdentidade().getDataNascimento());
        pessoaEntity.setNumRegistroNasc(domain.getRegistroCivil().getNumRegistroNasc());
        pessoaEntity.setFls(domain.getRegistroCivil().getFls());
        pessoaEntity.setLivro(domain.getRegistroCivil().getLivro());
        pessoaEntity.setCartorio(domain.getRegistroCivil().getCartorio());
        pessoaEntity.setCpf(domain.getIdentidade().getCpf());
        pessoaEntity.setRg(domain.getIdentidade().getRg());
        pessoaEntity.setDataEmissaoRg(domain.getIdentidade().getDataEmissaoRg());
        pessoaEntity.setOrgaoEmissorRg(domain.getIdentidade().getOrgaoEmissorRg());
        pessoaEntity.setCns(domain.getDadosSociais().getCns());
        pessoaEntity.setNis(domain.getDadosSociais().getNis());
        pessoaEntity.setDataCadastramento(domain.getDadosSociais().getDataCadastramento());

        pessoaEntity.setContatos(mapperHelper.mapContatosFromDomain(domain.getContatos()));
        pessoaEntity.setResponsaveis(mapperHelper.mapResponsaveisFromDomain(domain.getResponsaveis()));
        pessoaEntity.setVacinacoes(mapperHelper.mapVacinacoesFromDomain(domain.getVacinacoes()));
        pessoaEntity.setDeficiencias(mapperHelper.mapDeficienciasFromDomain(domain.getDeficiencias()));
        pessoaEntity.setCadastrosAnuais(mapperHelper.mapCadastrosAnuaisFromDomain(domain.getCadastrosAnuais()));
        pessoaEntity.setTiposAtendimentos(mapperHelper.mapTiposAtendimentosFromDomain(domain.getTiposAtendimentos()));

        return pessoaEntity;
    }

    private  List<PessoaResponsavel> extrairResponsaveis(PessoaEntity entity) {
        return entity.getResponsaveis()
                .stream()
                .map(r -> new PessoaResponsavel(
                        r.getId(),
                        r.getNome(),
                        r.getOndeProcurar(),
                        r.isVivo(),
                        r.getProfissao(),
                        r.getRg(),
                        r.getCpf(),
                        r.getEmergencia(),
                        PessoaResponsavel.TipoResponsavel.valueOf(r.getTipoResponsavel().name())
                )).collect(Collectors.toList());

    }

    private  List<CadastroAnual> extrairCadastrosAnuais(PessoaEntity entity) {
        return entity.getCadastrosAnuais()
                .stream()
                .map(c -> new CadastroAnual(
                        c.getId(),
                        c.getBeneficioDePrestacaoContinuada(),
                        c.getHistoricosAlergias(),
                        c.getMedicacoesContinuas(),
                        c.getHistoricoDoencas(),
                        c.getRendaFamiliar()
                )).collect(Collectors.toList());

    }

    private  List<Vacina> extrairVacinacoes(PessoaEntity entity) {
        return entity.getVacinacoes()
                .stream()
                .map(v -> new Vacina(
                        v.getId(),
                        v.getNome(),
                        v.getDataAplicacao()
                ))
                .collect(Collectors.toList());

    }

    private  List<TipoDeficiencia> extrairDeficiencias(PessoaEntity entity) {
        return entity.getDeficiencias()
                .stream()
                .map(d -> new TipoDeficiencia(
                        d.getId(),
                        d.getDescricao()
                ))
                .collect(Collectors.toList());

    }

    private  List<TipoAtendimento> extrairTiposAtendimentos(PessoaEntity entity) {
        return entity.getTiposAtendimentos()
                .stream()
                .map(t -> new TipoAtendimento(
                        t.getId(),
                        t.getDescricao()
                )).collect(Collectors.toList());

    }

    private  List<Contato> extrairContatos(PessoaEntity entity) {
        return entity.getContatos()
                .stream()
                .map(c -> new Contato(
                        c.getId(),
                        c.getEnderecoAtivo(),
                        c.getComprovanteResidencia(),
                        c.getEndereco(),
                        c.getBairro(),
                        c.getCidade(),
                        c.getEstado(),
                        c.getCep(),
                        c.getNaturalidade(),
                        c.getTelefone()
                )).collect(Collectors.toList());

    }

}
