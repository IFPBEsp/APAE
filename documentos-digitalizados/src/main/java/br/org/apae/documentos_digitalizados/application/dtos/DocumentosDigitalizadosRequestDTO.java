package br.org.apae.documentos_digitalizados.application.dtos;

import br.org.apae.documentos_digitalizados.domain.TipoDocumento;
import br.org.apae.documentos_digitalizados.domain.TipoPaciente;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record DocumentosDigitalizadosRequestDTO(@NotNull(message = "ID do paciênte é obrigatório!") UUID pacienteId,
                                                @NotBlank(message = "Nome do paciênte é obrigatório!") String nomePaciente,
                                                @NotNull(message = "Tipo do paciênte(ex: aluno, paciente, ambos) é obrigatório!") TipoPaciente tipoPaciente,
                                                @NotNull(message = "Tipo do documento(ex: pessoal, medico, escolar) é obrigatório!") TipoDocumento tipoDocumento,
                                                @NotBlank(message = "Nome do documento(ex: CPF, RG, laudo, ...) é obrigatório!") String nomeDocumento) {
}
