package br.org.apae.documentos_digitalizados.application.dtos;

import java.util.UUID;

public record DocumentosDigitalizadosResponseDTO(Long id,
                                                 Long pacienteId,
                                                 UUID encaminhamento,
                                                 UUID laudoMedico) {
}
