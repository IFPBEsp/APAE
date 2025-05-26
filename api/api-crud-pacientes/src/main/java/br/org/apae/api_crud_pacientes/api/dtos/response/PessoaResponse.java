package br.org.apae.api_crud_pacientes.api.dtos.response;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public class PessoaResponse {

    private UUID id;
    private String nome_completo;
    private LocalDate data_nascimento;
    private String num_registro_nasc;
    private String fls;
    private String livro;
    private String cartorio;
    private String cpf;
    private String rg;
    private LocalDate data_emissao_rg;
    private String orgao_emissor_rg;
    private String cns;
    private String nis;
    private LocalDate data_cadastramento;

    private ContatoResponse contatoResponse;
    private List<VacinaResponse> vacinasResponses;
    private List<TipoDeficienciaResponse> deficienciasResponses;
    private List<TipoAtendimentoResponse> atendimentosResponses;
    private List<PessoaResponsavelResponse> responsaveisResponses;
    private List<CadastroAnualResponse> cadastrosAnuaisResponses;

    public PessoaResponse() {}


    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }


    public String getNome_completo() {
        return nome_completo;
    }

    public void setNome_completo(String nome_completo) {
        this.nome_completo = nome_completo;
    }

    public LocalDate getData_nascimento() {
        return data_nascimento;
    }

    public void setData_nascimento(LocalDate data_nascimento) {
        this.data_nascimento = data_nascimento;
    }

    public String getNum_registro_nasc() {
        return num_registro_nasc;
    }

    public void setNum_registro_nasc(String num_registro_nasc) {
        this.num_registro_nasc = num_registro_nasc;
    }

    public String getFls() {
        return fls;
    }

    public void setFls(String fls) {
        this.fls = fls;
    }

    public String getLivro() {
        return livro;
    }

    public void setLivro(String livro) {
        this.livro = livro;
    }

    public String getCartorio() {
        return cartorio;
    }

    public void setCartorio(String cartorio) {
        this.cartorio = cartorio;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getRg() {
        return rg;
    }

    public void setRg(String rg) {
        this.rg = rg;
    }

    public LocalDate getData_emissao_rg() {
        return data_emissao_rg;
    }

    public void setData_emissao_rg(LocalDate data_emissao_rg) {
        this.data_emissao_rg = data_emissao_rg;
    }

    public String getOrgao_emissor_rg() {
        return orgao_emissor_rg;
    }

    public void setOrgao_emissor_rg(String orgao_emissor_rg) {
        this.orgao_emissor_rg = orgao_emissor_rg;
    }

    public String getCns() {
        return cns;
    }

    public void setCns(String cns) {
        this.cns = cns;
    }

    public String getNis() {
        return nis;
    }

    public void setNis(String nis) {
        this.nis = nis;
    }

    public LocalDate getData_cadastramento() {
        return data_cadastramento;
    }

    public void setData_cadastramento(LocalDate data_cadastramento) {
        this.data_cadastramento = data_cadastramento;
    }

    public ContatoResponse getContatoResponse() {
        return contatoResponse;
    }

    public void setContatoResponse(ContatoResponse contatoResponse) {
        this.contatoResponse = contatoResponse;
    }

    public List<VacinaResponse> getVacinasResponses() {
        return vacinasResponses;
    }

    public void setVacinasResponses(List<VacinaResponse> vacinasResponses) {
        this.vacinasResponses = vacinasResponses;
    }

    public List<TipoDeficienciaResponse> getDeficienciasResponses() {
        return deficienciasResponses;
    }

    public void setDeficienciasResponses(List<TipoDeficienciaResponse> deficienciasResponses) {
        this.deficienciasResponses = deficienciasResponses;
    }

    public List<TipoAtendimentoResponse> getAtendimentosResponses() {
        return atendimentosResponses;
    }

    public void setAtendimentosResponses(List<TipoAtendimentoResponse> atendimentosResponses) {
        this.atendimentosResponses = atendimentosResponses;
    }

    public List<PessoaResponsavelResponse> getResponsaveisResponses() {
        return responsaveisResponses;
    }

    public void setResponsaveisResponses(List<PessoaResponsavelResponse> responsaveisResponses) {
        this.responsaveisResponses = responsaveisResponses;
    }

    public List<CadastroAnualResponse> getCadastrosAnuaisResponses() {
        return cadastrosAnuaisResponses;
    }

    public void setCadastrosAnuaisResponses(List<CadastroAnualResponse> cadastrosAnuaisResponses) {
        this.cadastrosAnuaisResponses = cadastrosAnuaisResponses;
    }
}
