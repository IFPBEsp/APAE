package br.org.apae.profissional_da_saude.api.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@Getter
public class AgendamentoCreateDTO {

    @NotBlank
    private UUID idPaciente;
    @NotBlank
    private UUID idProfissional;
    @NotBlank
    private Integer frequenciaDias;
    @NotBlank
    private LocalDate proximaConsulta;
    @NotBlank
    private LocalTime horaProximaConsulta;
}
