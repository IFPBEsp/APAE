package br.org.apae.documentos_escolares.api.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record DocumentoEscolarUpdateRequestDTO(
        @NotNull(message = "O ID é obrigatório.") UUID pacienteId,
        @NotNull(message = "O ano é obrigatório") Integer ano,
        @NotBlank(message = "O nome atual do documento é obrigatório") String documentoNome,
        @NotBlank(message = "O novo nome do documento é obrigatório") String novoNome
) {}
