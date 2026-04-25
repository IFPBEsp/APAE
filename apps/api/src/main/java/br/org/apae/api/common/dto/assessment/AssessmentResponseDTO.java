package br.org.apae.api.common.dto.assessment;

import java.time.LocalDateTime;
import java.util.UUID;

public record AssessmentResponseDTO(
    UUID id,
    String descricaoAvaliacao,
    LocalDateTime dataAvaliacao,
    UUID pacienteId,
    String professorNome
){}