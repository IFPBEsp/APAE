package br.org.apae.profissional_da_saude.api.dto;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

import br.org.apae.profissional_da_saude.domain.model.Falta;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AgendamentoGeradoResponseDTO {

    private UUID id;
    private Integer frequencia_dias;
    private LocalDate data_inicial;
    private LocalTime hora;
    private LocalDate data_fim;
    private Boolean ativo;

    private UUID fk_atendimento;
    private UUID fk_profissional;
    private UUID fk_cadastro_anual;

    private List<FaltaDTO> faltas;
    
}