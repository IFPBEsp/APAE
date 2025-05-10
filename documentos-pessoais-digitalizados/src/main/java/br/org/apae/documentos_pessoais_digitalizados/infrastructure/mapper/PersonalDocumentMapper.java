package br.org.apae.documentos_pessoais_digitalizados.infrastructure.mapper;

import br.org.apae.documentos_pessoais_digitalizados.api.dto.res.PersonalDocumentResUrlDTO;
import br.org.apae.documentos_pessoais_digitalizados.domain.model.PersonalDocument;
import org.springframework.stereotype.Component;

@Component
public class PersonalDocumentMapper {
    public PersonalDocumentResUrlDTO toDTO(PersonalDocument personalDocument) {
        String url = "/api/documents/" + personalDocument.getPatient() + "/file";
        return new PersonalDocumentResUrlDTO(url);
    }

}
