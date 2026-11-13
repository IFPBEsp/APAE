package br.org.apae.api.common.dto.patient.request.documents;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import br.org.apae.api.documents.interfaces.validations.NotEmptyFiles;
import br.org.apae.api.documents.interfaces.validations.ValidFileFormat;
import br.org.apae.api.documents.interfaces.validations.ValidFileSize;
import br.org.apae.api.documents.interfaces.validations.ValidImageFormat;

public record CreateDocumentsDTO(
                @ValidFileSize @ValidFileFormat MultipartFile rg,
                @ValidFileSize @ValidFileFormat MultipartFile cpf,
                @ValidFileSize @ValidFileFormat MultipartFile proof_of_address,
                @ValidFileSize @ValidFileFormat MultipartFile birth_certificate,
                @ValidFileSize @ValidImageFormat MultipartFile photo,
                @NotEmptyFiles @ValidFileSize @ValidFileFormat List<MultipartFile> reports,
                @NotEmptyFiles @ValidFileSize @ValidFileFormat List<MultipartFile> referrals) {
}
