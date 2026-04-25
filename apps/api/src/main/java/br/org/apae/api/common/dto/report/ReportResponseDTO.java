package br.org.apae.api.common.dto.report;

import java.time.LocalDateTime;
import java.util.UUID;

public record ReportResponseDTO(
    UUID id,
    LocalDateTime createdAt,
    String habilidades,
    String estrategias,
    String recursos,
    String professorNome,
    String turmaDescricao
) {}
