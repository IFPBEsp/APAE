package br.org.apae.documentos_digitalizados.application.dtos;

import br.org.apae.documentos_digitalizados.domain.TipoPaciente;

import java.time.LocalDateTime;

public record PacienteDocumentoResponseDTO(Long idPaciente,
                                           String nomePaciente,
                                           String nomeBucket,
                                           TipoPaciente tipoPaciente,
                                           LocalDateTime dataAtualizacao) {
}
