package br.org.apae.documentos_digitalizados.api.dto;

import br.org.apae.documentos_digitalizados.domain.model.TipoPaciente;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record CriarBucketRequestDTO(@NotNull(message = "ID do paciênte é obrigatório!") UUID idPaciente,
                                    @NotBlank(message = "Tipo do paciênte é obrigatório!") TipoPaciente tipoPaciente) {
}
