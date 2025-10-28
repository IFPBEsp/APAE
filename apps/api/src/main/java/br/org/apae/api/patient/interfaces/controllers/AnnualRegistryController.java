package br.org.apae.api.patient.interfaces.controllers;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestBody;

import br.org.apae.api.common.dto.patient.request.annual_registry.CreateAnnualRegistryDTO;
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
    @Operation(summary = "Cadastrar registro anual de um paciente", description = "Cria um novo registro anual para o paciente especificado pelo seu ID. "
            + "Esse registro contém informações como doenças, renda familiar e benefícios (BPC).")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Registro anual criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos"),
            @ApiResponse(responseCode = "404", description = "Paciente não encontrado")
    })
    @PostMapping
    ResponseEntity<AnnualRegistryResponseDTO> createRegistry(
            @Parameter(name = "id do paciente", description = "UUID do paciente para o qual o registro anual será criado", required = true, example = "a3c1b5f0-84b2-4c64-8bda-0cf78c648a3d") @PathVariable("id") UUID patientId,
            @Parameter(description = "Dados necessários para criar um novo registro anual do paciente.", required = true) @RequestBody @Valid CreateAnnualRegistryDTO createAnnualRegistryDTO);
}
