package br.org.apae.documentos_digitalizados.application.dtos;

import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

public record DocumentosDigitalizadosResponseDTO(MultipartFile documento) {
}
