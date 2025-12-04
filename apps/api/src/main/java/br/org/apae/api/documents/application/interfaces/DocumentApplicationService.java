package br.org.apae.api.documents.application.interfaces;

import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import br.org.apae.api.documents.interfaces.dto.DocumentDTO;
import br.org.apae.api.documents.interfaces.dto.GetDocumentArgsDTO;
import br.org.apae.api.documents.interfaces.dto.GetPresignedDocumentUrlArgsDTO;
import br.org.apae.api.documents.interfaces.dto.ListDocumentsArgsDTO;
import br.org.apae.api.documents.interfaces.dto.PutDocumentArgsDTO;
import br.org.apae.api.documents.interfaces.dto.RemoveDocumentArgsDTO;
import br.org.apae.api.documents.interfaces.exceptions.InsufficientDataException;
import br.org.apae.api.documents.interfaces.exceptions.InvalidResponseException;

public interface DocumentApplicationService {
    DocumentDTO putDocument(PutDocumentArgsDTO dto)
            throws InsufficientDataException, IOException, InvalidKeyException,
            InvalidResponseException, NoSuchAlgorithmException;

    InputStream getDocument(GetDocumentArgsDTO dto)
            throws InsufficientDataException, IOException, InvalidKeyException,
            InvalidResponseException, NoSuchAlgorithmException;

    String getPresignedDocumentUrl(GetPresignedDocumentUrlArgsDTO dto)
            throws InsufficientDataException, IOException, InvalidKeyException,
            InvalidResponseException, NoSuchAlgorithmException;

    void removeDocument(RemoveDocumentArgsDTO dto)
            throws InsufficientDataException, IOException, InvalidKeyException,
            InvalidResponseException, NoSuchAlgorithmException;

    Iterable<DocumentDTO> listDocuments(ListDocumentsArgsDTO dto)
            throws InsufficientDataException, IOException, InvalidKeyException,
            InvalidResponseException, NoSuchAlgorithmException;
}
