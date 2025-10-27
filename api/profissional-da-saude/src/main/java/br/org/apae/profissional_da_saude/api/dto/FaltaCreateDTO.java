package br.org.apae.profissional_da_saude.api.dto;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FaltaCreateDTO {

    @NotNull(message = "A data da falta é obrigatória.")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate data;

    @NotNull(message = "A hora da falta é obrigatória.")
    @JsonFormat(pattern = "HH:mm")
    private LocalTime hora;

    @NotNull(message = "A informação de justificativa (true/false) é obrigatória.")
    private Boolean justificada;

    @NotNull(message = "O motivo da falta é obrigatório.")
    private String motivo;

    @NotNull(message = "O ID do atendimento é obrigatório.")
    private UUID fkAtendimento;

    @NotNull(message = "O ID do profissional é obrigatório.")
    private UUID fkProfissional;

    @NotNull(message = "O ID do cadastro anual é obrigatório.")
    private UUID fkCadastroAnual;

}
