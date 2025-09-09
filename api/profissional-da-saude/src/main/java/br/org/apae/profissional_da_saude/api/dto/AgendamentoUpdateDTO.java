package br.org.apae.profissional_da_saude.api.dto;

import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@Getter
public class AgendamentoUpdateDTO {
    private UUID idPaciente;
    private UUID idAreaDaSaude;
    private Integer frequenciaDias;
    private LocalDate proximaConsulta;
    private LocalTime horaProximaConsulta;
    private Boolean confirmado;
    private String descricao;
    private String justificativa;
}
