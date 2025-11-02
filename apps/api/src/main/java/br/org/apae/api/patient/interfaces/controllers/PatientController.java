package br.org.apae.api.patient.interfaces.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import br.org.apae.api.common.dto.patient.request.documents.CreateDocumentsDTO;
import br.org.apae.api.common.dto.patient.request.patient.CreatePatientDTO;
import br.org.apae.api.common.dto.patient.request.patient.UpdatePatientDTO;
import br.org.apae.api.common.dto.patient.response.patient.PatientResponseDTO;
import br.org.apae.api.common.dto.patient.response.patient.PatientSummaryResponseDTO;

import java.util.List;
import java.util.UUID;

@RequestMapping("/patients")
@Tag(name = "Patients", description = "Endpoints para gerenciamento de pacientes")
public interface PatientController {

        @Operation(summary = "Cadastrar um novo paciente", description = "Cria um novo paciente no sistema com todos os seus dados.")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "201", description = "Paciente cadastrado com sucesso"),
                        @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos")
        })
        @PostMapping(consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
        ResponseEntity<PatientResponseDTO> createPatient(@RequestPart("patient") @Valid CreatePatientDTO patient,
                        @ModelAttribute @Valid CreateDocumentsDTO documents);

        @Operation(summary = "Buscar paciente por ID", description = "Retorna os dados completos de um paciente específico pelo seu ID.")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Paciente encontrado com sucesso"),
                        @ApiResponse(responseCode = "404", description = "Paciente não encontrado")
        })
        @GetMapping("/{id}")
        ResponseEntity<PatientResponseDTO> findById(@PathVariable UUID id);

        @Operation(summary = "Listar e filtrar pacientes",
                description = "Retorna uma lista de pacientes com base em critérios de filtro dinâmicos via query params.")
        @ApiResponses(value = {
                @ApiResponse(responseCode = "200", description = "Lista de pacientes filtrada retornada com sucesso")
        })
        @GetMapping
        ResponseEntity<List<PatientSummaryResponseDTO>> findWithFilters(
                @Parameter(description = "Filtrar por nome parcial do paciente")
                @RequestParam(name = "Nome", required = false) String nome,

//                @Parameter(description = "Filtrar por tipo de atendimento (ex: paciente, aluno)")
//                @RequestParam(name = "tipo_atendimento", required = false) String tipoAtendimento,

                @Parameter(description = "Filtrar por transtorno")
                @RequestParam(name = "transtorno", required = false) String transtorno,

                @Parameter(description = "Filtrar por ano de cadastro ou referência")
                @RequestParam(name = "ano", required = false) String ano,

                @Parameter(description = "Filtrar por cidade")
                @RequestParam(name = "cidade", required = false) String cidade
        );

        @Operation(summary = "Atualizar um paciente")
        @ApiResponses(value = {
                @ApiResponse(responseCode = "200", description = "Paciente atualizado com sucesso"),
                @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos"),
                @ApiResponse(responseCode = "404", description = "Paciente não encontrado")
        })
        @PutMapping("/{id}")
        ResponseEntity<PatientResponseDTO> updatePatient(@PathVariable UUID id,
                        @RequestBody @Valid UpdatePatientDTO updatePatientDTO);

        @Operation(summary = "Excluir um paciente", description = "Remove um paciente do sistema a partir do seu ID.")
        @ApiResponses(value = {
                @ApiResponse(responseCode = "204", description = "Paciente excluído com sucesso"),
                @ApiResponse(responseCode = "404", description = "Paciente não encontrado")
        })
        @PatchMapping("/{id}")
        ResponseEntity<Void> deletePatient(@PathVariable UUID id);
//
//        @Operation(summary = "Lista os tipos de atendimento para o filtro")
//        @GetMapping("/filtros/tipos-atendimento")
//        ResponseEntity<List<String>> getTiposAtendimento();

        @Operation(summary = "Lista os transtornos para o filtro")
        @GetMapping("/filtros/transtornos")
        ResponseEntity<List<String>> getTranstornos();

        @Operation(summary = "Lista os anos para o filtro")
        @GetMapping("/filtros/anos")
        ResponseEntity<List<String>> getAnos();

        @Operation(summary = "Lista as cidades para o filtro")
        @GetMapping("/filtros/cidades")
        ResponseEntity<List<String>> getCidades();
}