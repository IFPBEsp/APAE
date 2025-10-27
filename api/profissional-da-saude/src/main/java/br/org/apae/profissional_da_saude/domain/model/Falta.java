package br.org.apae.profissional_da_saude.domain.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.UUID;

public class Falta {

    private UUID id;

    private LocalDate data;
    private LocalTime hora;
    private Boolean justificada;
    private String motivo;
    private LocalDateTime dataCriacao;

    private UUID fk_atendimento;
    private UUID fk_profissional;
    private UUID fk_cadastro_anual;

    public Falta(LocalDate data, LocalTime hora, Boolean justificada, String motivo, UUID fk_atendimento,
            UUID fk_profissional, UUID fk_cadastro_anual) {
        this.data = data;
        this.hora = hora;
        this.justificada = justificada;
        this.motivo = motivo;
        this.fk_atendimento = fk_atendimento;
        this.fk_profissional = fk_profissional;
        this.fk_cadastro_anual = fk_cadastro_anual;
    }

    public Falta(UUID id, LocalDate data, LocalTime hora, Boolean justificada, String motivo,
            UUID fk_atendimento, UUID fk_profissional, UUID fk_cadastro_anual, LocalDateTime dataCriacao) {
        this.id = id;
        this.data = data;
        this.hora = hora;
        this.justificada = justificada;
        this.motivo = motivo;
        this.fk_atendimento = fk_atendimento;
        this.fk_profissional = fk_profissional;
        this.fk_cadastro_anual = fk_cadastro_anual;
        this.dataCriacao = dataCriacao;

    }

    // Getters e Setters

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public LocalTime getHora() {
        return hora;
    }

    public void setHora(LocalTime hora) {
        this.hora = hora;
    }

    public Boolean getJustificada() {
        return justificada;
    }

    public void setJustificada(Boolean justificada) {
        this.justificada = justificada;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public LocalDateTime getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(LocalDateTime dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    // Chaves Estrangeiras

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

}
