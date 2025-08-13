package br.org.apae.profissional_da_saude.domain.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.UUID;


public class Agendamento {

    private UUID id;
    private UUID idPaciente;
    private UUID idProfissional;
    private Integer frequenciaDias;
    private LocalDate proximaConsulta;
    private LocalTime horaProximaConsulta;
    private Boolean confirmado;
    private LocalDateTime dataCriacao;

    public Agendamento(UUID idPaciente, UUID idProfissional, Integer frequenciaDias, LocalDate proximaConsulta, LocalTime horaProximaConsulta) {
        this.idPaciente = idPaciente;
        this.idProfissional = idProfissional;
        this.frequenciaDias = frequenciaDias;
        this.proximaConsulta = proximaConsulta;
        this.horaProximaConsulta = horaProximaConsulta;
        this.confirmado = false;
    }

    public Agendamento(UUID id, UUID idPaciente, UUID idProfissional, Integer frequenciaDias, LocalDate proximaConsulta, LocalTime horaProximaConsulta, LocalDateTime dataCriacao) {
        this.id = id;
        this.idPaciente = idPaciente;
        this.idProfissional = idProfissional;
        this.frequenciaDias = frequenciaDias;
        this.proximaConsulta = proximaConsulta;
        this.horaProximaConsulta = horaProximaConsulta;
        this.dataCriacao = dataCriacao;
        this.confirmado = false;
    }

    public Agendamento(UUID idPaciente, UUID idProfissional, Integer frequenciaDias, LocalDate proximaConsulta, LocalTime horaProximaConsulta, Boolean confirmado) {
        this.idPaciente = idPaciente;
        this.idProfissional = idProfissional;
        this.frequenciaDias = frequenciaDias;
        this.proximaConsulta = proximaConsulta;
        this.horaProximaConsulta = horaProximaConsulta;
        this.confirmado = confirmado;
    }

    public Agendamento(UUID id, UUID idPaciente, UUID idProfissional, Integer frequenciaDias, LocalDate proximaConsulta, LocalTime horaProximaConsulta, LocalDateTime dataCriacao, Boolean confirmado) {
        this.id = id;
        this.idPaciente = idPaciente;
        this.idProfissional = idProfissional;
        this.frequenciaDias = frequenciaDias;
        this.proximaConsulta = proximaConsulta;
        this.horaProximaConsulta = horaProximaConsulta;
        this.dataCriacao = dataCriacao;
        this.confirmado = confirmado;
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

}
