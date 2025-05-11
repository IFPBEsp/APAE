package br.org.apae.documentos_pessoais_digitalizados.infrastructure.mapper;

import br.org.apae.documentos_pessoais_digitalizados.api.dto.res.PersonalDocumentResUrlDTO;
import br.org.apae.documentos_pessoais_digitalizados.domain.model.PersonalDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class PersonalDocumentMapper {

    private String domainUri;

    @Autowired
    public PersonalDocumentMapper(@Value("${app.web.domain}") String domainUri) {
        this.domainUri = domainUri;
    }

    public PersonalDocumentResUrlDTO toDTO(PersonalDocument personalDocument) {
        String url = domainUri + "/api/documents/" + personalDocument.getId() + "/file";
        return new PersonalDocumentResUrlDTO(url);
    }

}
