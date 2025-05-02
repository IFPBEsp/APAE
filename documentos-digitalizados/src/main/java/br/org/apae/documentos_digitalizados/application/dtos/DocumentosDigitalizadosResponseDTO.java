package br.org.apae.documentos_digitalizados.application.dtos;

import org.springframework.core.io.InputStreamResource;

public record DocumentosDigitalizadosResponseDTO(InputStreamResource documento) {
}
