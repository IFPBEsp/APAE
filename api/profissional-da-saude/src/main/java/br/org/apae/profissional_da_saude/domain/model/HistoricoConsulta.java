package br.org.apae.profissional_da_saude.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class HistoricoConsulta {
    private UUID id;
    private UUID idAgendamento;
    private LocalDate dataConsulta;
    private LocalTime horaConsulta;
    private boolean foiRealizada;
    private String justificativa;
    private LocalDateTime dataCriacao;
}