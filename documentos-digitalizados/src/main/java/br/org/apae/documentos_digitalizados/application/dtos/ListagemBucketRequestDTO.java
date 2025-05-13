package br.org.apae.documentos_digitalizados.application.dtos;

import br.org.apae.documentos_digitalizados.domain.TipoDocumento;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record ListagemBucketRequestDTO(@NotBlank(message = "ID do paciente é obrigatório!") UUID idPaciente,
                                       @NotBlank(message = "Nome do paciente é obrigatório!") String nomePaciente,
                                       @NotNull(message = "Tipo do documento é obrigatório!") TipoDocumento tipoDocumento) {
}
