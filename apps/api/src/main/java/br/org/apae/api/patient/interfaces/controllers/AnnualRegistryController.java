package br.org.apae.api.patient.interfaces.controllers;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import br.org.apae.api.common.dto.patient.request.annual_registry.CreateAnnualRegistryDTO;
import br.org.apae.api.common.dto.patient.request.annual_registry.ReplaceAnnualRegistryDTO;
import br.org.apae.api.common.dto.patient.request.annual_registry.UpdateAnnualRegistryDTO;
import br.org.apae.api.common.dto.patient.response.annual_registry.AnnualRegistryResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RequestMapping("/patients/{id}/annual-registry")
@Tag(name = "Annual Registry", description = "Endpoints para gerenciamento dos registros anuais dos pacientes")
public interface AnnualRegistryController {

    @Operation(summary = "Cadastrar registro anual de um paciente", description = "Cria um novo registro anual para o paciente especificado pelo seu ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Registro anual criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos"),
            @ApiResponse(responseCode = "404", description = "Paciente não encontrado")
    })
    @PostMapping
    ResponseEntity<AnnualRegistryResponseDTO> createRegistry(
            @Parameter(name = "id do paciente", description = "UUID do paciente para o qual o registro anual será criado", required = true) @PathVariable("id") UUID patientId,
            @Parameter(description = "Dados necessários para criar um novo registro anual do paciente.", required = true) @RequestBody @Valid CreateAnnualRegistryDTO createAnnualRegistryDTO);

    @Operation(summary = "Buscar registro anual por ano", description = "Busca um registro anual específico de um paciente pelo ano.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Registro encontrado"),
            @ApiResponse(responseCode = "404", description = "Paciente ou registro não encontrado")
    })
    @GetMapping("/{year}")
    ResponseEntity<AnnualRegistryResponseDTO> getRegistryByYear(
            @Parameter(description = "ID do paciente") @PathVariable("id") UUID patientId,
            @Parameter(description = "Ano do registro (ex: 2025)") @PathVariable("year") Integer year);

    @Operation(summary = "Atualizar registro anual (Parcial/PATCH)", description = "Atualiza um registro anual existente pelo seu ID único. Envie apenas os campos que deseja alterar.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Registro atualizado"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "404", description = "Paciente ou registro não encontrado")
    })
    @PatchMapping("/{registryId}")
    ResponseEntity<AnnualRegistryResponseDTO> updateRegistry(
            @Parameter(description = "ID do paciente") @PathVariable("id") UUID patientId,
            @Parameter(description = "ID do registro a ser atualizado") @PathVariable("registryId") UUID registryId,
            @RequestBody @Valid UpdateAnnualRegistryDTO updateDto);

    @Operation(summary = "Substituir registro anual (Total/PUT)", description = "Substitui *totalmente* um registro anual. Todos os campos são obrigatórios.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Registro substituído"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "404", description = "Paciente ou registro não encontrado")
    })
    @PutMapping("/{registryId}")
    ResponseEntity<AnnualRegistryResponseDTO> replaceRegistry(
            @Parameter(description = "ID do paciente") @PathVariable("id") UUID patientId,
            @Parameter(description = "ID do registro a ser substituído") @PathVariable("registryId") UUID registryId,
            @RequestBody @Valid ReplaceAnnualRegistryDTO replaceDto);

    @Operation(summary = "Excluir registro anual", description = "Exclui um registro anual existente pelo seu ID único.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Registro excluído"),
            @ApiResponse(responseCode = "404", description = "Paciente ou registro não encontrado")
    })
    @DeleteMapping("/{registryId}")
    ResponseEntity<Void> deleteRegistry(
            @Parameter(description = "ID do paciente") @PathVariable("id") UUID patientId,
            @Parameter(description = "ID do registro a ser excluído") @PathVariable("registryId") UUID registryId);
}