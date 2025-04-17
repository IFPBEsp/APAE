package br.org.apae.laudos_medicos.application.dtos.responses;

import java.time.LocalDate;

public record LaudoResponseDTO(Long id, Long idMedico, Long idPaciente, LocalDate dataEmissao, String descricao){}
