package br.org.apae.profissional_da_saude.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.UUID;


@Getter
@AllArgsConstructor
public class Agendamento {

    private UUID id;
    private UUID idPaciente;
    private UUID idProfissional;
    private Integer frequenciaDias;
    private LocalDate proximaConsulta;
    private LocalTime horaProximaConsulta;
    private LocalDateTime dataCriacao;

    public Agendamento(UUID idPaciente, UUID idProfissional, Integer frequenciaDias, LocalDate proximaConsulta, LocalTime horaProximaConsulta, LocalDateTime dataCriacao) {
        this.idPaciente = idPaciente;
        this.idProfissional = idProfissional;
        this.frequenciaDias = frequenciaDias;
        this.proximaConsulta = proximaConsulta;
        this.horaProximaConsulta = horaProximaConsulta;
        this.dataCriacao = dataCriacao;
    }
}
