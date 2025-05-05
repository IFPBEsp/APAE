package br.org.apae.documentos_digitalizados.application.dtos;

import br.org.apae.documentos_digitalizados.domain.TipoPaciente;

import java.time.LocalDateTime;
import java.util.UUID;

public record PacienteDocumentoResponseDTO(UUID idPaciente,
                                           String nomePaciente,
                                           String nomeBucket,
                                           TipoPaciente tipoPaciente,
                                           LocalDateTime dataAtualizacao) {
}
