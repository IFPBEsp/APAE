package br.org.apae.api.common.dto.assessment;

import java.time.LocalDateTime;
import java.util.UUID;

public record AssessmentResponseDTO(
    UUID id,
    UUID alunoId,
    String alunoNome,
    UUID professorId,
    String professorNome,
    String descricao,
    LocalDateTime dataAvaliacao
){}