package br.org.apae.documentos_pessoais_digitalizados.api.controller;

import br.org.apae.documentos_pessoais_digitalizados.api.dto.req.PersonalDocumentFileReqDTO;
import br.org.apae.documentos_pessoais_digitalizados.api.dto.req.PersonalDocumentReqDTO;
import br.org.apae.documentos_pessoais_digitalizados.api.dto.res.PersonalDocumentResUrlDTO;
import br.org.apae.documentos_pessoais_digitalizados.application.service.PersonalDocumentService;
import br.org.apae.documentos_pessoais_digitalizados.application.service.StorageService;
import br.org.apae.documentos_pessoais_digitalizados.domain.model.PersonalDocument;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Tag(name = "Documentos Pessoais", description = "Gerencia os documentos pessoais digitalizados dos pacientes")
@RestController
@RequestMapping("/api/documents")
public class PersonalDocumentControllerImpl implements PersonalDocumentController {

    private final PersonalDocumentService personalDocumentService;
    private final StorageService storageService;

    @Autowired
    public PersonalDocumentControllerImpl(PersonalDocumentService personalDocumentService, StorageService storageService) {
        this.personalDocumentService = personalDocumentService;
        this.storageService = storageService;
    }

    @Operation(
        summary = "Fazer upload de documentos de um paciente",
        description = "Recebe arquivos e dados do documento, associa a um paciente existente e armazena os documentos digitalizados no sistema."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Documentos enviados com sucesso"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos")
    })
    @Override
    @PostMapping("/{patientId}/upload")
    public ResponseEntity<List<PersonalDocumentResUrlDTO>> create(
            @Valid @PathVariable UUID patientId,
            @Valid @ModelAttribute PersonalDocumentReqDTO personalDocumentReqDTO
    ) {
        List<PersonalDocumentResUrlDTO> personalDocumentResUrlDTOS = this.personalDocumentService.create(patientId, personalDocumentReqDTO);
        return new ResponseEntity<>(personalDocumentResUrlDTOS, HttpStatus.CREATED);
    }

    @Operation(
        summary = "Atualizar o arquivo de um documento",
        description = "Substitui o arquivo de um documento existente pelo novo conteúdo enviado, mantendo os metadados e o vínculo com o paciente."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Documento atualizado com sucesso"),
        @ApiResponse(responseCode = "404", description = "Documento não encontrado")
    })
    @Override
    @PutMapping("/{id}")
    public ResponseEntity<PersonalDocumentResUrlDTO> update(
            @Valid @PathVariable UUID id,
            @Valid @ModelAttribute PersonalDocumentFileReqDTO personalDocumentFileReqDTO
    ) {
        PersonalDocumentResUrlDTO personalDocumentResUrlDTO = this.personalDocumentService.update(id, personalDocumentFileReqDTO);
        return ResponseEntity.ok(personalDocumentResUrlDTO);
    }

    @Operation(
        summary = "Buscar documento por ID",
        description = "Retorna as informações do documento identificado pelo UUID informado."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Documento encontrado"),
        @ApiResponse(responseCode = "404", description = "Documento não encontrado")
    })
    @Override
    @GetMapping("/{id}")
    public ResponseEntity<PersonalDocumentResUrlDTO> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(this.personalDocumentService.findById(id));
    }

    @Operation(
        summary = "Listar documentos de um paciente",
        description = "Retorna todos os documentos associados a um paciente identificado por UUID."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Documentos listados com sucesso"),
        @ApiResponse(responseCode = "404", description = "Paciente não encontrado")
    })
    @Override
    @GetMapping("/paciente/{id}")
    public ResponseEntity<List<PersonalDocumentResUrlDTO>> findAll(@PathVariable UUID id) {
        return ResponseEntity.ok(this.personalDocumentService.findAll(id));
    }

    @Operation(
        summary = "Excluir um documento",
        description = "Remove permanentemente o documento do sistema com base no seu ID."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Documento excluído com sucesso"),
        @ApiResponse(responseCode = "404", description = "Documento não encontrado")
    })
    @Override
    @DeleteMapping("/{id}")
    public ResponseEntity<PersonalDocumentResUrlDTO> delete(@PathVariable UUID id) {
        this.personalDocumentService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(
        summary = "Obter conteúdo do arquivo",
        description = "Retorna o conteúdo binário do arquivo associado ao documento identificado pelo UUID."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Arquivo retornado com sucesso"),
        @ApiResponse(responseCode = "404", description = "Documento ou arquivo não encontrado")
    })
    @Override
    @GetMapping("/{id}/file")
    public ResponseEntity<byte[]> findFileByDocumentId(@PathVariable UUID id) {
        PersonalDocument personalDocument = this.personalDocumentService.findFileByDocumentId(id);
        byte[] file = this.storageService.findDocumentByFileName(personalDocument.getPathDocumentStorage(), personalDocument.getPatient().toString());
        String contentType = personalDocument.getContentType();
        return ResponseEntity.ok()
                .contentType(MediaType.valueOf(contentType))
                .body(file);
    }
}
