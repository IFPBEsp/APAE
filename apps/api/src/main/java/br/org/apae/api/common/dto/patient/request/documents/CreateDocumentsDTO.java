package br.org.apae.api.common.dto.patient.request.documents;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import br.org.apae.api.documents.interfaces.validations.NotEmptyFiles;
import jakarta.validation.constraints.NotNull;

public record CreateDocumentsDTO(
                MultipartFile rg,
                MultipartFile cpf,
                MultipartFile proof_of_address,
                MultipartFile birth_certificate,
                MultipartFile photo,
                @NotEmptyFiles List<MultipartFile> reports,
                @NotEmptyFiles List<MultipartFile> referrals) {
}
