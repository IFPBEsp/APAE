package br.org.apae.laudos_medicos.application.dtos.requests;

import java.time.LocalDate;

public record LaudoRequestDTO(Long idMedico, Long idPaciente, LocalDate dataEmissao, String descricao){}
