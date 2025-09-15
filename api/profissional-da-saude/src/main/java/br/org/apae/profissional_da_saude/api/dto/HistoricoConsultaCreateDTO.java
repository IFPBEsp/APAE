package br.org.apae.profissional_da_saude.api.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class HistoricoConsultaCreateDTO {

    @NotNull(message = "ID do agendamento é obrigatório")
    private UUID idAgendamento;

    @NotNull(message = "Data da consulta é obrigatória")
    @PastOrPresent(message = "Data da consulta não pode ser futura")
    private LocalDate dataConsulta;

    @NotNull(message = "Hora da consulta é obrigatória")
    private LocalTime horaConsulta;

    @NotNull(message = "Indicador de realização é obrigatório")
    private Boolean foiRealizada;

    private String justificativa;
}