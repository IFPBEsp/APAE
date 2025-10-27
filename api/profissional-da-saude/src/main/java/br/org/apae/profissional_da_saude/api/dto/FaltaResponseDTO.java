package br.org.apae.profissional_da_saude.api.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import java.util.UUID;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class FaltaResponseDTO {
    private UUID id;
    private LocalDate data;
    private LocalTime hora;
    private Boolean justificada;
    private String motivo;
    private UUID fkAtendimento;
    private UUID fkProfissional;
    private UUID fkCadastroAnual;
    private LocalDateTime dataCriacao;

}
