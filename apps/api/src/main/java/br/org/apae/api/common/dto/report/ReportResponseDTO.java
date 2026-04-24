package br.org.apae.api.common.dto.report;

import java.time.LocalDateTime;

public record ReportResponseDTO(
    Long id,
    LocalDateTime createdAt,
    String habilidades,
    String estrategias,
    String recursos,
    String professorNome,
    String turmaDescricao
) {}
