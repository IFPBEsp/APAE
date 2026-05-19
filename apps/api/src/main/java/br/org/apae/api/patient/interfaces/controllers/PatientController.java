package br.org.apae.api.patient.interfaces.controllers;

import br.org.apae.api.common.dto.patient.response.patient.PatientWithAbsencesResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import br.org.apae.api.common.dto.patient.request.documents.CreateDocumentsDTO;
import br.org.apae.api.common.dto.patient.request.patient.CreatePatientDTO;
import br.org.apae.api.common.dto.patient.request.patient.UpdatePatientDTO;
import br.org.apae.api.common.dto.patient.response.patient.PatientResponseDTO;
import br.org.apae.api.common.dto.patient.response.patient.PatientSummaryResponseDTO;

import java.util.List;
import java.util.Map;
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
        // DOCUMENTAÇÃO SWAGGER (Para aparecer os campos na tela do Swagger)
        @Parameter(name = "name", description = "Nome parcial", in = ParameterIn.QUERY)
        @Parameter(name = "disorder", description = "Transtorno", in = ParameterIn.QUERY)
        @Parameter(name = "year", description = "Ano", in = ParameterIn.QUERY)
        @Parameter(name = "city", description = "Cidade", in = ParameterIn.QUERY)
        @Parameter(name = "treatmentType", description = "Tipo de atendimento", in = ParameterIn.QUERY)
        @GetMapping
        ResponseEntity<Page<PatientSummaryResponseDTO>> findWithFilters(
                // Ocultamos o Map do Swagger para ele não criar um campo JSON estranho
                @Parameter(hidden = true)
                @RequestParam Map<String, String> filters,
                @Parameter(hidden = true) Pageable pageable
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

        @Operation(summary = "Atualizar foto de perfil do paciente")
        @ApiResponses(value = {
                @ApiResponse(responseCode = "200", description = "Foto atualizada com sucesso"),
                @ApiResponse(responseCode = "400", description = "Arquivo invalido"),
                @ApiResponse(responseCode = "404", description = "Paciente nao encontrado")
        })
        @PutMapping(value = "/{id}/photo", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
        ResponseEntity<PatientResponseDTO> updatePatientPhoto(@PathVariable UUID id,
                                                              @RequestPart("photo") org.springframework.web.multipart.MultipartFile photo);

        @Operation(summary = "Excluir um paciente", description = "Remove um paciente do sistema a partir do seu ID.")
        @ApiResponses(value = {
                @ApiResponse(responseCode = "204", description = "Paciente excluído com sucesso"),
                @ApiResponse(responseCode = "404", description = "Paciente não encontrado")
        })
        @PatchMapping("/{id}")
        ResponseEntity<Void> deletePatient(@PathVariable UUID id);

        @Operation(
                summary = "Listar pacientes com faltas",
                description = "Retorna uma lista paginada de pacientes que possuem pelo menos o número mínimo de faltas informado."
        )
        @ApiResponses(value = {
                @ApiResponse(responseCode = "200", description = "Lista de pacientes retornada com sucesso"),
                @ApiResponse(responseCode = "400", description = "Parâmetro de requisição inválido")
        })
        @GetMapping("/with-absences")
        ResponseEntity<Page<PatientWithAbsencesResponseDTO>> findPatientsWithAbsences(
                @Parameter(
                        description = "Número mínimo de faltas para filtrar os pacientes",
                        example = "2"
                )
                @RequestParam(defaultValue = "1") Integer minAbsences,
                @Parameter(
                        description = "Texto para busca (ex: nome do paciente)",
                        example = "John"
                )
                @RequestParam(required = false) String name,
                @Parameter(hidden = true)
                Pageable pageable
        );

        @Operation(summary = "Lista os tipos de atendimento para o filtro")
        @GetMapping("/filtros/tipos-atendimento")
        ResponseEntity<List<String>> getTiposAtendimento();

        @Operation(summary = "Lista os transtornos para o filtro",
                description = "Retorna uma lista de nomes de transtornos cadastrados para usar no filtro de pacientes.")
        @ApiResponses(value = {
                @ApiResponse(responseCode = "200", description = "Lista de transtornos retornada com sucesso")
        })
        @GetMapping("/filtros/transtornos")
        ResponseEntity<List<String>> getTranstornos();

        @Operation(summary = "Lista os anos para o filtro",
                description = "Retorna uma lista de anos (como strings) dos registros anuais para usar no filtro de pacientes.")
        @ApiResponses(value = {
                @ApiResponse(responseCode = "200", description = "Lista de anos retornada com sucesso")
        })
        @GetMapping("/filtros/anos")
        ResponseEntity<List<String>> getAnos();


        @Operation(summary = "Lista as cidades para o filtro",
                description = "Retorna uma lista de cidades distintas dos pacientes cadastrados para usar no filtro.")
        @ApiResponses(value = {
                @ApiResponse(responseCode = "200", description = "Lista de cidades retornada com sucesso")
        })
        @GetMapping("/filtros/cidades")
        ResponseEntity<List<String>> getCidades();
}
