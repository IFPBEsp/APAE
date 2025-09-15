package br.org.apae.profissional_da_saude.api.dto;

import br.org.apae.profissional_da_saude.domain.model.enums.DiaSemana;
import br.org.apae.profissional_da_saude.domain.model.enums.Turno;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class DisponibilidadeDTO {

    @NotNull(message = "Dia da semana é obrigatório")
    private DiaSemana dia;

    @NotNull(message = "Turno é obrigatório")
    private Turno turno;

    public DisponibilidadeDTO() {
    }

    public DisponibilidadeDTO(DiaSemana dia, Turno turno) {
        this.dia = dia;
        this.turno = turno;
    }
}
