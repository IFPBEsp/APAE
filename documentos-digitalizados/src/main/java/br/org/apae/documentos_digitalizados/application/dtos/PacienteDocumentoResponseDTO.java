package br.org.apae.documentos_digitalizados.application.dtos;

import br.org.apae.documentos_digitalizados.domain.TipoPaciente;

public record PacienteDocumentoResponseDTO(Long idPaciente,
                                           String nomePaciente,
                                           String nomeBucket,
                                           TipoPaciente tipoPaciente) {
}
