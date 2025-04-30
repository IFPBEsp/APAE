package br.org.apae.documentos_digitalizados.application.dtos;

import br.org.apae.documentos_digitalizados.domain.TipoDeDocumento;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record DocumentosDigitalizadosRequestDTO(@NotBlank(message = "ID do paciente é obrigatório!") Long pacienteId,
                                                @NotNull(message = "Tipo do documento(pessoal, medico, escolar) é obrigatório!") TipoDeDocumento tipoDeDocumento,
                                                @NotBlank(message = "Nome do documento é obrigatório!") String nomeDocumento) {
}
