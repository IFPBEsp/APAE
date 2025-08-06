package br.org.apae.profissional_da_saude.domain.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public class Agendamento {

    private UUID id;
    private UUID idPaciente;
    private UUID idProfissional;
    private Integer frequenciaDias;
    private LocalDateTime proximaConsulta;
    private LocalDate createAt;

    public Agendamento(UUID id, UUID idPaciente, UUID idProfissional, Integer frequenciaDias, LocalDateTime proximaConsulta, LocalDate createAt) {
        this.id = id;
        this.idPaciente = idPaciente;
        this.idProfissional = idProfissional;
        this.frequenciaDias = frequenciaDias;
        this.proximaConsulta = proximaConsulta;
        this.createAt = createAt;
    }

    public Agendamento(UUID idPaciente, UUID idProfissional, Integer frequenciaDias, LocalDateTime proximaConsulta, LocalDate createAt) {
        this.idPaciente = idPaciente;
        this.idProfissional = idProfissional;
        this.frequenciaDias = frequenciaDias;
        this.proximaConsulta = proximaConsulta;
        this.createAt = createAt;
    }

    public UUID getId() {
        return id;
    }

    public UUID getIdPaciente() {
        return idPaciente;
    }

    public UUID getIdProfissional() {
        return idProfissional;
    }

    public Integer getFrequenciaDias() {
        return frequenciaDias;
    }

    public LocalDateTime getProximaConsulta() {
        return proximaConsulta;
    }

    public LocalDate getCreateAt() {
        return createAt;
    }
}
