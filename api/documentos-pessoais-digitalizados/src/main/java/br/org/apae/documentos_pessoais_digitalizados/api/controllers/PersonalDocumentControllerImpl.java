package br.org.apae.documentos_pessoais_digitalizados.api.controllers;

import br.org.apae.documentos_pessoais_digitalizados.api.dtos.req.PersonalDocumentReqDTO;
import br.org.apae.documentos_pessoais_digitalizados.api.dtos.res.PersonalDocumentResDTO;
import br.org.apae.documentos_pessoais_digitalizados.application.services.PersonalDocumentService;
import br.org.apae.documentos_pessoais_digitalizados.domain.models.PersonalDocumentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping("api/v1/documentos-pessoais")
public class PersonalDocumentControllerImpl implements PersonalDocumentController {

    private final PersonalDocumentService personalDocumentService;

    @Autowired
    public PersonalDocumentControllerImpl(PersonalDocumentService personalDocumentService) {
        this.personalDocumentService = personalDocumentService;
    }

    @Override
    @Operation(
        summary = "Anexa um documento pessoal",
        description = "Permite anexar um documento pessoal ao paciente usando um arquivo Multipart e o tipo de documento."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Documento criado com sucesso"),
        @ApiResponse(responseCode = "400", description = "Requisição inválida"),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @PostMapping(value = "/{patientId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> attachPersonalDocument(
            @PathVariable("patientId") String patientId,
            @RequestPart("documentType") String documentType,
            @RequestPart("file") MultipartFile file
    ) {
        PersonalDocumentReqDTO dto = new PersonalDocumentReqDTO(patientId, documentType, file);
        this.personalDocumentService.saveFile(dto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Override
    @Operation(
        summary = "Lista todos os documentos pessoais do paciente",
        description = "Retorna todos os documentos associados a um paciente específico."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Documentos retornados com sucesso"),
        @ApiResponse(responseCode = "404", description = "Paciente não encontrado"),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @GetMapping("/{patientId}/documentos")
    public ResponseEntity<PersonalDocumentResDTO> listPersonalDocuments(@PathVariable("patientId") String patientId) {
        PersonalDocumentResDTO documentsDTO = this.personalDocumentService.listPersonalDocument(patientId);
        return ResponseEntity.ok(documentsDTO);
    }

    @Override
    @Operation(
        summary = "Lista documento pessoal por tipo",
        description = "Retorna o documento de um paciente filtrado pelo tipo de documento."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Documento retornado com sucesso"),
        @ApiResponse(responseCode = "404", description = "Documento ou paciente não encontrado"),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @GetMapping("/{patientId}")
    public ResponseEntity<PersonalDocumentResDTO> listPersonalDocumentByType(
            @PathVariable("patientId") String patientId,
            @RequestParam("type") String documentType
    ) {
        PersonalDocumentResDTO resDTO = this.personalDocumentService.listPersonalDocumentByType(patientId, PersonalDocumentType.valueOf(documentType));
        return ResponseEntity.ok(resDTO);
    }

    @Override
    @Operation(
        summary = "Visualiza um documento pessoal",
        description = "Retorna o arquivo de imagem do documento pessoal do paciente para visualização."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Documento retornado com sucesso"),
        @ApiResponse(responseCode = "404", description = "Documento não encontrado"),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @GetMapping(value = "/{patientId}/vizualizar", produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<byte[]> viewPatientPersonalDocument(
            @PathVariable("patientId") String patientId,
            @RequestParam("fileName") String fileName
    ) {
        byte[] file = this.personalDocumentService.viewPatientPersonalDocuments(patientId, fileName);
        return ResponseEntity.ok(file);
    }

    @Override
    @Operation(
        summary = "Deleta um documento pessoal",
        description = "Remove permanentemente um documento pessoal de um paciente com base no nome do arquivo."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Documento deletado com sucesso"),
        @ApiResponse(responseCode = "404", description = "Documento ou paciente não encontrado"),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @DeleteMapping("/{patientId}")
    public ResponseEntity<Void> deleteDocument(
            @PathVariable("patientId") String patientId,
            @RequestParam("fileName") String fileName
    ) {
        this.personalDocumentService.deleteDocument(patientId, fileName);
        return ResponseEntity.noContent().build();
    }
}

