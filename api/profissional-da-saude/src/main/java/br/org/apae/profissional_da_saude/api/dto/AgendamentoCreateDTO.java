package br.org.apae.profissional_da_saude.api.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@Getter
public class AgendamentoCreateDTO {

    @NotNull
    private UUID idPaciente;

    @NotNull
    private UUID idAreaDaSaude;

    @NotNull
    private Integer frequenciaDias;

    @NotNull
    private LocalDate proximaConsulta;

    @NotNull
    private Boolean confirmado;

    @NotNull
    private String descricao;

    @NotNull
    @JsonFormat(pattern = "HH:mm:ss")
    private LocalTime horaProximaConsulta;
}
