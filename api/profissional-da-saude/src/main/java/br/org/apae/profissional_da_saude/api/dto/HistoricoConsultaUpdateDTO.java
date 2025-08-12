package br.org.apae.profissional_da_saude.api.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class HistoricoConsultaUpdateDTO {

    @NotNull(message = "Indicador de realização é obrigatório")
    private Boolean foiRealizada;

    private String justificativa;
}