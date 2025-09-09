package br.org.apae.profissional_da_saude.domain.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.UUID;


public class Agendamento {

    private UUID id;
    private UUID idPaciente;
    private UUID idAreaDaSaude;
    private Integer frequenciaDias;
    private LocalDate proximaConsulta;
    private LocalTime horaProximaConsulta;
    private Boolean confirmado;
    private String descricao;
    private String justificativa;
    private LocalDateTime dataCriacao;

    public Agendamento(UUID idPaciente, UUID idProfissional, Integer frequenciaDias, LocalDate proximaConsulta, LocalTime horaProximaConsulta) {
        this.idPaciente = idPaciente;
        this.idAreaDaSaude = idProfissional;
        this.frequenciaDias = frequenciaDias;
        this.proximaConsulta = proximaConsulta;
        this.horaProximaConsulta = horaProximaConsulta;
        this.confirmado = false;
    }

    public Agendamento(UUID id, UUID idPaciente, UUID idAreaDaSaude, Integer frequenciaDias, LocalDate proximaConsulta, LocalTime horaProximaConsulta, LocalDateTime dataCriacao) {
        this.id = id;
        this.idPaciente = idPaciente;
        this.idAreaDaSaude = idAreaDaSaude;
        this.frequenciaDias = frequenciaDias;
        this.proximaConsulta = proximaConsulta;
        this.horaProximaConsulta = horaProximaConsulta;
        this.dataCriacao = dataCriacao;
        this.confirmado = false;
    }

    public Agendamento(UUID idPaciente, UUID idAreaDaSaude, Integer frequenciaDias, LocalDate proximaConsulta, LocalTime horaProximaConsulta, Boolean confirmado, String descricao, String justificativa) {
        this.idPaciente = idPaciente;
        this.idAreaDaSaude = idAreaDaSaude;
        this.frequenciaDias = frequenciaDias;
        this.proximaConsulta = proximaConsulta;
        this.horaProximaConsulta = horaProximaConsulta;
        this.confirmado = confirmado;
        this.descricao = descricao;
        this.justificativa = justificativa;
    }

    public Agendamento(UUID id, UUID idPaciente, UUID idAreaDaSaude, Integer frequenciaDias, LocalDate proximaConsulta, LocalTime horaProximaConsulta, Boolean confirmado, String descricao, String justificativa, LocalDateTime dataCriacao) {
        this.id = id;
        this.idPaciente = idPaciente;
        this.idAreaDaSaude = idAreaDaSaude;
        this.frequenciaDias = frequenciaDias;
        this.proximaConsulta = proximaConsulta;
        this.horaProximaConsulta = horaProximaConsulta;
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

    public UUID getIdAreaDaSaude() {
        return idAreaDaSaude;
    }

    public void setIdAreaDaSaude(UUID idAreaDaSaude) {
        this.idAreaDaSaude = idAreaDaSaude;
    }

    public Integer getFrequenciaDias() {
        return frequenciaDias;
    }

    public void setFrequenciaDias(Integer frequenciaDias) {
        this.frequenciaDias = frequenciaDias;
    }

    public LocalDate getProximaConsulta() {
        return proximaConsulta;
    }

    public void setProximaConsulta(LocalDate proximaConsulta) {
        this.proximaConsulta = proximaConsulta;
    }

    public LocalTime getHoraProximaConsulta() {
        return horaProximaConsulta;
    }

    public void setHoraProximaConsulta(LocalTime horaProximaConsulta) {
        this.horaProximaConsulta = horaProximaConsulta;
    }

    public LocalDateTime getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(LocalDateTime dataCriacao) {
        this.dataCriacao = dataCriacao;
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

}
