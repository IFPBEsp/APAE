package br.org.apae.documentos_escolares.api.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;

import java.util.UUID;

public record DocumentoEscolarUploadRequestDTO(@NotNull(message = "O ID é obrigatório.") UUID pacienteId,
                                               @NotBlank(message = "O ano é obrigatório") Integer ano) {
}
