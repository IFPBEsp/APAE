package br.org.apae.documentos_escolares.api.dto.response;

import java.util.List;
import java.util.UUID;

public record DocumentoEscolarResponseDTO(UUID pacienteId,
                                          List<UrlPreAssinadaResponseDTO> urls) {
}
