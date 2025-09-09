package br.org.apae.api_crud_pacientes.domain.model.pessoa;

import br.org.apae.api_crud_pacientes.domain.model.*;
import br.org.apae.api_crud_pacientes.domain.model.pessoa.VO.*;

import java.util.List;
import java.util.UUID;

public class Pessoa {

    private final UUID id;
    private final Identidade identidade;
    private final RegistroCivil registroCivil;
    private final DadosSociais dadosSociais;
    private final List<PessoaResponsavel> responsaveis;
    private final List<CadastroAnual> cadastrosAnuais;
    private final List<Vacina> vacinacoes;
    private final List<TipoDeficiencia> deficiencias;
    private final List<TipoAtendimento> tiposAtendimentos;
    private final List<Contato> contatos;

    private Pessoa(Builder builder) {
        this.id = builder.id != null ? builder.id : UUID.randomUUID();
        this.identidade = builder.identidade;
        this.registroCivil = builder.registroCivil;
        this.dadosSociais = builder.dadosSociais;
        this.responsaveis = builder.responsaveis;
        this.cadastrosAnuais = builder.cadastrosAnuais;
        this.vacinacoes = builder.vacinacoes;
        this.deficiencias = builder.deficiencias;
        this.tiposAtendimentos = builder.tiposAtendimentos;
        this.contatos = builder.contatos;
    }

    public UUID getId() {
        return id;
    }

    public Identidade getIdentidade() {
        return identidade;
    }

    public RegistroCivil getRegistroCivil() {
        return registroCivil;
    }

    public DadosSociais getDadosSociais() {
        return dadosSociais;
    }

    public List<PessoaResponsavel> getResponsaveis() {
        return responsaveis;
    }

    public List<CadastroAnual> getCadastrosAnuais() {
        return cadastrosAnuais;
    }

    public List<Vacina> getVacinacoes() {
        return vacinacoes;
    }

    public List<TipoDeficiencia> getDeficiencias() {
        return deficiencias;
    }

    public List<TipoAtendimento> getTiposAtendimentos() {
        return tiposAtendimentos;
    }

    public List<Contato> getContatos() {
        return contatos;
    }

    public static class Builder {
        private UUID id;
        private Identidade identidade;
        private RegistroCivil registroCivil;
        private DadosSociais dadosSociais;
        private List<PessoaResponsavel> responsaveis;
        private List<CadastroAnual> cadastrosAnuais;
        private List<Vacina> vacinacoes;
        private List<TipoDeficiencia> deficiencias;
        private List<TipoAtendimento> tiposAtendimentos;
        private List<Contato> contatos;

        public Builder id(UUID id) {
            this.id = id;
            return this;
        }

        public Builder identidade(Identidade identidade) {
            this.identidade = identidade;
            return this;
        }

        public Builder registroCivil(RegistroCivil registroCivil) {
            this.registroCivil = registroCivil;
            return this;
        }

        public Builder dadosSociais(DadosSociais dadosSociais) {
            this.dadosSociais = dadosSociais;
            return this;
        }

        public Builder responsaveis(List<PessoaResponsavel> responsaveis) {
            this.responsaveis = responsaveis;
            return this;
        }

        public Builder cadastrosAnuais(List<CadastroAnual> cadastrosAnuais) {
            this.cadastrosAnuais = cadastrosAnuais;
            return this;
        }

        public Builder vacinacoes(List<Vacina> vacinacoes) {
            this.vacinacoes = vacinacoes;
            return this;
        }

        public Builder deficiencias(List<TipoDeficiencia> deficiencias) {
            this.deficiencias = deficiencias;
            return this;
        }

        public Builder tiposAtendimentos(List<TipoAtendimento> tiposAtendimentos) {
            this.tiposAtendimentos = tiposAtendimentos;
            return this;
        }

        public Builder contatos(List<Contato> contatos) {
            this.contatos = contatos;
            return this;
        }

        public Pessoa build() {
            if (identidade == null || dadosSociais == null || registroCivil == null || contatos == null) {
                throw new IllegalArgumentException("Campos inválidos.");
            }
            return new Pessoa(this);
        }
    }
}
