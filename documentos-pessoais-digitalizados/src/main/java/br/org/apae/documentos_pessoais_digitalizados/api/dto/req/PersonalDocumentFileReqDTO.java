package br.org.apae.documentos_pessoais_digitalizados.api.dto.req;

import br.org.apae.documentos_pessoais_digitalizados.domain.model.PersonalDocumentType;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.multipart.MultipartFile;

public record PersonalDocumentFileReqDTO(
        @NotNull(message = "O tipo de documento pessoal não pode ser nulo.")
        PersonalDocumentType personalDocumentType,

        @NotNull(message = "O arquivo não pode ser nulo.")
        MultipartFile file
    ) {
}
