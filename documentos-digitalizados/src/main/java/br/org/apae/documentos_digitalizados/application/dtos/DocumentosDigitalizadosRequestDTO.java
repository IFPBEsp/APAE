package br.org.apae.documentos_digitalizados.application.dtos;

import br.org.apae.documentos_digitalizados.domain.TipoDocumento;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record DocumentosDigitalizadosRequestDTO(@NotBlank(message = "ID do paciente é obrigatório!") Long pacienteId,
                                                @NotBlank(message = "Nome do paciente é obrigatório!") String nomePaciente,
                                                @NotNull(message = "Tipo do documento(ex: pessoal, medico, escolar) é obrigatório!") TipoDocumento tipoDocumento,
                                                @NotBlank(message = "Nome do documento(ex: CPF, RG, laudo, ...) é obrigatório!") String nomeDocumento) {
}
