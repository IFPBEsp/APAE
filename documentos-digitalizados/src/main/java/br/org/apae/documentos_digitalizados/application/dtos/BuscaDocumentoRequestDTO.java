package br.org.apae.documentos_digitalizados.application.dtos;

import br.org.apae.documentos_digitalizados.domain.TipoDocumento;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record BuscaDocumentoRequestDTO(@NotBlank(message = "ID do paciente é obrigatório!") Long idPaciente,
                                       @NotBlank(message = "Nome do paciente é obrigatório!") String nomePaciente,
                                       @NotNull(message = "Tipo do documento é obrigatório!") TipoDocumento tipoDocumento,
                                       @NotBlank(message = "Nome do documento é obrigatório!") String nomeDocumento) {
}
