package br.org.apae.profissional_da_saude.api.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.UUID;

@Getter
@Setter
@Builder
public class AgendamentoResponseDTO {
    private UUID id;
    private UUID idPaciente;
    private UUID idProfissional;
    private Integer frequenciaDias;
    private LocalDate proximaConsulta;
    private LocalTime horaProximaConsulta;
    private Boolean confirmado;
    private LocalDateTime dataCriacao;
}
