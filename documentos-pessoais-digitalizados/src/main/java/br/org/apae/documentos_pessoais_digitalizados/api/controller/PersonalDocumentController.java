package br.org.apae.documentos_pessoais_digitalizados.api.controller;

import br.org.apae.documentos_pessoais_digitalizados.api.dto.req.PersonalDocumentFileReqDTO;
import br.org.apae.documentos_pessoais_digitalizados.api.dto.req.PersonalDocumentReqDTO;
import br.org.apae.documentos_pessoais_digitalizados.api.dto.res.PersonalDocumentResUrlDTO;
import br.org.apae.documentos_pessoais_digitalizados.domain.model.PersonalDocumentType;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
public interface PersonalDocumentController {

    @Operation(
            summary = "Fazer upload de documentos de um paciente",
            description = "Recebe arquivos e dados do documento, associa a um paciente existente e armazena os documentos digitalizados no sistema."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Documentos enviados com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos")
    })
    ResponseEntity<List<PersonalDocumentResUrlDTO>> create(UUID id, PersonalDocumentReqDTO personalDocumentReqDTO);

    @Operation(
            summary = "Atualizar o arquivo de um documento",
            description = "Substitui o arquivo de um documento existente pelo novo conteúdo enviado, mantendo os metadados e o vínculo com o paciente."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Documento atualizado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Documento não encontrado")
    })
    ResponseEntity<PersonalDocumentResUrlDTO> update(UUID id, PersonalDocumentFileReqDTO personalDocumentFileReqDTO);
    @Operation(
            summary = "Buscar documento por ID",
            description = "Retorna as informações do documento identificado pelo UUID informado."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Documento encontrado"),
            @ApiResponse(responseCode = "404", description = "Documento não encontrado")
    })
    ResponseEntity<PersonalDocumentResUrlDTO> findById(UUID id);

    @Operation(
            summary = "Listar documentos de um paciente",
            description = "Retorna todos os documentos associados a um paciente identificado por UUID."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Documentos listados com sucesso"),
            @ApiResponse(responseCode = "404", description = "Paciente não encontrado")
    })
    ResponseEntity<List<PersonalDocumentResUrlDTO>> findAll(UUID patientId);

    @Operation(
            summary = "Excluir um documento",
            description = "Remove permanentemente o documento do sistema com base no seu ID."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Documento excluído com sucesso"),
            @ApiResponse(responseCode = "404", description = "Documento não encontrado")
    })
    ResponseEntity<Void> delete(UUID id);

    @Operation(
            summary = "Busca Documentos que de um paciente",
            description = "Busca todos os Documentos que de um paciente que tem determinada tag."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Documentos listados com sucesso"),
            @ApiResponse(responseCode = "404", description = "Paciente não encontrado")
    })
    ResponseEntity<List<PersonalDocumentResUrlDTO>> findAllDocumentsByTag(UUID patientId, PersonalDocumentType documentType);

    @Operation(
            summary = "Obter conteúdo do arquivo",
            description = "Retorna o conteúdo binário do arquivo associado ao documento identificado pelo UUID."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Arquivo retornado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Documento ou arquivo não encontrado")
    })
    ResponseEntity<byte[]> findFileByDocumentId(UUID id);
}
