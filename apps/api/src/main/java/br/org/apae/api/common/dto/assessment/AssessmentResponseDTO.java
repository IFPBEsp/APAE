package br.org.apae.api.common.dto.assessment;

import java.time.LocalDateTime;

public record AssessmentResponseDTO(
    Long id,
    String descricaoAvaliacao,
    LocalDateTime dataAvaliacao,
    Long pacienteId,
    String nomeProfessor
){}