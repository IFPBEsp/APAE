package br.org.apae.api.paciente.interfaces.controllers;

import br.org.apae.api.common.dto.paciente.dto.create.CreatePatientDTO;
import br.org.apae.api.common.dto.paciente.dto.response.PatientResponseDTO;
import br.org.apae.api.common.dto.paciente.dto.update.UpdatePatientDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    @PostMapping
    ResponseEntity<Void> createPatient(@RequestBody @Valid CreatePatientDTO createPatientDTO);

    @Operation(summary = "Buscar paciente por ID", description = "Retorna os dados completos de um paciente específico pelo seu ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Paciente encontrado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Paciente não encontrado")
    })
    @GetMapping("/{id}")
    ResponseEntity<PatientResponseDTO> findById(@PathVariable UUID id);

    @Operation(summary = "Listar todos os pacientes", description = "Retorna uma lista de todos os pacientes cadastrados, sem aplicar filtros.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de pacientes retornada com sucesso")
    })
    @GetMapping
    ResponseEntity<List<PatientResponseDTO>> findAll();

    @Operation(summary = "Filtrar pacientes", description = "Retorna uma lista de pacientes com base em critérios de filtro dinâmicos. Os filtros suportados são: fullName, cpf, city.")
    @Parameter(name = "filters", description = "Exemplo de uso: /filter?fullName=João&cpf=123.456.789-00")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de pacientes filtrada retornada com sucesso")
    })
    @GetMapping("/filter")
    ResponseEntity<List<PatientResponseDTO>> findByFilter(@RequestParam Map<String, String> filters);

    @Operation(summary = "Atualizar um paciente", description = "Atualiza os dados de um paciente existente a partir do seu ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Paciente atualizado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos"),
            @ApiResponse(responseCode = "404", description = "Paciente não encontrado")
    })
    @PutMapping("/{id}")
    ResponseEntity<PatientResponseDTO> updatePatient(@PathVariable UUID id, @RequestBody @Valid UpdatePatientDTO updatePatientDTO);

    @Operation(summary = "Excluir um paciente", description = "Remove um paciente do sistema a partir do seu ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Paciente excluído com sucesso"),
            @ApiResponse(responseCode = "404", description = "Paciente não encontrado")
    })
    @DeleteMapping("/{id}")
    ResponseEntity<Void> deletePatient(@PathVariable UUID id);
}
