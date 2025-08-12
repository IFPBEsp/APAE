package br.org.apae.profissional_da_saude.api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class HistoricoConsultaResponseDTO {
    private Long id;
    private Long idAgendamento;
    private LocalDate dataConsulta;
    private boolean foiRealizada;
    private String justificativa;
    private LocalDateTime dataCriacao;
}