package br.org.apae.profissional_da_saude.domain.model;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

public class AgendamentoGerado {

    private UUID id;
    private Integer frequencia_dias;
    private LocalDate data_inicial;
    private LocalTime hora;
    private LocalDate data_fim;
    private Boolean ativo;

    private UUID fk_atendimento;
    private UUID fk_profissional;
    private UUID fk_cadastro_anual;

    private List<Falta> faltas;


    public AgendamentoGerado(Integer frequencia_dias, LocalDate data_inicial, LocalTime hora, LocalDate data_fim, Boolean ativo, UUID fk_atendimento, UUID fk_profissional, UUID fk_cadastro_anual) {
        this.frequencia_dias = frequencia_dias;
        this.data_inicial = data_inicial;
        this.hora = hora;
        this.data_fim = data_fim;
        this.ativo = ativo;
        this.fk_atendimento = fk_atendimento;
        this.fk_profissional = fk_profissional;
        this.fk_cadastro_anual = fk_cadastro_anual;
    }

    public AgendamentoGerado(UUID id, Integer frequencia_dias, LocalDate data_inicial, LocalTime hora, LocalDate data_fim, Boolean ativo, UUID fk_atendimento, UUID fk_profissional, UUID fk_cadastro_anual, List<Falta> faltas) {
        this.id = id;
        this.frequencia_dias = frequencia_dias;
        this.data_inicial = data_inicial;
        this.hora = hora;
        this.data_fim = data_fim;
        this.ativo = ativo;
        this.fk_atendimento = fk_atendimento;
        this.fk_profissional = fk_profissional;
        this.fk_cadastro_anual = fk_cadastro_anual;
        this.faltas = faltas;
    }

    public UUID getId() {
        return id;
    }
    public void setId(UUID id) {
        this.id = id;
    }

    //Chaves estrangeiras

    public UUID getFkAtendimento() {
        return fk_atendimento;
    }
    public void setFkAtendimento(UUID fk_atendimento) {
        this.fk_atendimento = fk_atendimento;
    }

    public UUID getFkProfissional() {
        return fk_profissional;
    }
    public void setFkProfissional(UUID fk_profissional) {
        this.fk_profissional = fk_profissional;
    }

    public UUID getFkCadastroAnual() {
        return fk_cadastro_anual;
    }
    public void setFkCadastroAnual(UUID fk_cadastro_anual) {
        this.fk_cadastro_anual = fk_cadastro_anual;
    }
    //
    
    public Integer getFrequenciaDias() {
        return frequencia_dias;
    }
    public void setFrequenciaDias(Integer frequencia_dias) {
        this.frequencia_dias = frequencia_dias;
    }

    public LocalDate getDataInicial() {
        return data_inicial;
    }
    public void setDataInicial(LocalDate data_inicial) {
        this.data_inicial = data_inicial;
    }

    public LocalTime getHora() {
        return hora;
    }
    public void setHora(LocalTime hora) {
        this.hora = hora;
    }

    public LocalDate getDataFim() {
        return data_fim;
    }
    public void setDataFim(LocalDate data_fim) {
        this.data_fim = data_fim;
    }

    public Boolean getAtivo() {
        return ativo;
    }
    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }

    public List<Falta> getFaltas(){ 
        return faltas; 
    }
    public void setFaltas(List<Falta> faltas){ 
        this.faltas = faltas; 
    }

}
