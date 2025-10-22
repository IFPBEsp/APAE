package br.org.apae.profissional_da_saude.domain.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.UUID;


public class Agendamento {

    private UUID id;
    private UUID idPaciente;
    private UUID idProfissional;
    private UUID idAtendimento;
    private UUID idCadastroAnual;
    private Integer frequenciaDias;
    private LocalDate dataInicial;
    private LocalDate dataFim;
    private LocalTime hora;
    private Boolean ativo;
    private Boolean confirmado;
    private String descricao;
    private String justificativa;
    private LocalDateTime dataCriacao;

    public Agendamento(UUID idPaciente, UUID idProfissional, UUID idAtendimento, UUID idCadastroAnual, Integer frequenciaDias, LocalDate dataInicial, LocalDate dataFim, LocalTime hora, Boolean ativo, Boolean confirmado, String descricao, String justificativa) {
        this.idPaciente = idPaciente;
        this.idProfissional = idProfissional;
        this.idAtendimento = idAtendimento;
        this.idCadastroAnual = idCadastroAnual;
        this.frequenciaDias = frequenciaDias;
        this.dataInicial = dataInicial;
        this.dataFim = dataFim;
        this.hora = hora;
        this.ativo = ativo;
        this.confirmado = confirmado;
        this.descricao = descricao;
        this.justificativa = justificativa;
    }

    public Agendamento(UUID id, UUID idPaciente, UUID idProfissional, UUID idAtendimento, UUID idCadastroAnual, Integer frequenciaDias, LocalDate dataInicial, LocalDate dataFim, LocalTime hora, Boolean ativo, Boolean confirmado, String descricao, String justificativa, LocalDateTime dataCriacao) {
        this.id = id;
        this.idPaciente = idPaciente;
        this.idProfissional = idProfissional;
        this.idAtendimento = idAtendimento;
        this.idCadastroAnual = idCadastroAnual;
        this.frequenciaDias = frequenciaDias;
        this.dataInicial = dataInicial;
        this.dataFim = dataFim;
        this.hora = hora;
        this.ativo = ativo;
        this.confirmado = confirmado;
        this.descricao = descricao;
        this.justificativa = justificativa;
        this.dataCriacao = dataCriacao;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getIdPaciente() {
        return idPaciente;
    }

    public void setIdPaciente(UUID idPaciente) {
        this.idPaciente = idPaciente;
    }

    public UUID getIdProfissional() {
        return idProfissional;
    }

    public void setIdProfissional(UUID idProfissional) {
        this.idProfissional = idProfissional;
    }

    public UUID getIdAtendimento() {
        return idAtendimento;
    }

    public void setIdAtendimento(UUID idAtendimento) {
        this.idAtendimento = idAtendimento;
    }

    public UUID getIdCadastroAnual() {
        return idCadastroAnual;
    }

    public void setIdCadastroAnual(UUID idCadastroAnual) {
        this.idCadastroAnual = idCadastroAnual;
    }

    public Integer getFrequenciaDias() {
        return frequenciaDias;
    }

    public void setFrequenciaDias(Integer frequenciaDias) {
        this.frequenciaDias = frequenciaDias;
    }

    public LocalDate getDataInicial() {
        return dataInicial;
    }

    public void setDataInicial(LocalDate dataInicial) {
        this.dataInicial = dataInicial;
    }

    public LocalDate getDataFim() {
        return dataFim;
    }

    public void setDataFim(LocalDate dataFim) {
        this.dataFim = dataFim;
    }

    public LocalTime getHora() {
        return hora;
    }

    public void setHora(LocalTime hora) {
        this.hora = hora;
    }

    public Boolean getAtivo() {
        return ativo;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }

    public Boolean getConfirmado() {
        return confirmado;
    }

    public void setConfirmado(Boolean confirmado) {
        this.confirmado = confirmado;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getJustificativa() {
        return justificativa;
    }

    public void setJustificativa(String justificativa) {
        this.justificativa = justificativa;
    }

    public LocalDateTime getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(LocalDateTime dataCriacao) {
        this.dataCriacao = dataCriacao;
    }
}
