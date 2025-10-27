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

    public AgendamentoGeradoResponseDTO(UUID id, Integer frequencia_dias, LocalDate data_inicial, LocalTime hora,
                                        LocalDate data_fim, Boolean ativo, UUID fk_atendimento,
                                        UUID fk_profissional, UUID fk_cadastro_anual, List<FaltaDTO> faltas) {
        this.id = id;
        this.frequencia_dias = frequencia_dias;
        this.data_inicial = data_inicial;
        this.hora = hora;
        this.data_fim = data_fim;
        this.ativo = ativo;
        this.fk_atendimento = fk_atendimento;
        this.fk_profissional = fk_profissional;
        this.fk_cadastro_anual = fk_cadastro_anual;
        this.faltas = faltas;
    }
    
}