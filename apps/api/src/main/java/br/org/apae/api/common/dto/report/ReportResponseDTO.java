package br.org.apae.api.common.dto.report;

import java.time.LocalDateTime;
import java.util.UUID;

public record ReportResponseDTO(
    UUID id,
    UUID alunoId,
    String alunoNome,
    UUID professorId,
    String professorNome,
    UUID turmaId,
    String turmaNome,
    String atividades,
    String habilidades,
    String estrategias,
    String recursos,
    LocalDateTime createdAt
) {}
