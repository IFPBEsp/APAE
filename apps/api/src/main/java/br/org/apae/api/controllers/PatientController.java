package br.org.apae.api.controllers;

import br.org.apae.api.paciente.dto.create.CreatePatientDTO;
import br.org.apae.api.paciente.dto.update.UpdatePatientDTO;
import br.org.apae.api.paciente.dto.response.PatientResponseDTO;
import br.org.apae.api.paciente.facade.IPatientFacade;
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

@RestController
@RequestMapping("/patients")
@Tag(name = "Patients", description = "Endpoints para gerenciamento de pacientes")
public class PatientController {

    private final IPatientFacade patientFacade;

    public PatientController(IPatientFacade patientFacade) {
        this.patientFacade = patientFacade;
    }

    @Operation(summary = "Cadastrar um novo paciente", description = "Cria um novo paciente no sistema com todos os seus dados.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Paciente cadastrado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos")
    })
    @PostMapping("/create")
    public ResponseEntity<Void> createPatient(@RequestBody @Valid CreatePatientDTO createPatientDTO) {
        patientFacade.createPatient(createPatientDTO);
        return ResponseEntity.status(201).build();
    }

    @Operation(summary = "Buscar paciente por ID", description = "Retorna os dados completos de um paciente específico pelo seu ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Paciente encontrado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Paciente não encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<PatientResponseDTO> findById(@PathVariable UUID id) {
        PatientResponseDTO patient = patientFacade.findById(id);
        return ResponseEntity.ok(patient);
    }

    @Operation(summary = "Listar todos os pacientes", description = "Retorna uma lista de todos os pacientes cadastrados, sem aplicar filtros.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de pacientes retornada com sucesso")
    })
    @GetMapping
    public ResponseEntity<List<PatientResponseDTO>> findAll() {
        List<PatientResponseDTO> patients = patientFacade.findAll();
        return ResponseEntity.ok(patients);
    }

    @Operation(summary = "Filtrar pacientes", description = "Retorna uma lista de pacientes com base em critérios de filtro dinâmicos. Os filtros suportados são: fullName, cpf, city.")
    @Parameter(name = "filters", description = "Exemplo de uso: /filter?fullName=João&cpf=123.456.789-00")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de pacientes filtrada retornada com sucesso")
    })
    @GetMapping("/filter")
    public ResponseEntity<List<PatientResponseDTO>> findByFilter(@RequestParam Map<String, String> filters) {
        List<PatientResponseDTO> patients = patientFacade.findByFilter(filters);
        return ResponseEntity.ok(patients);
    }


    @Operation(summary = "Atualizar um paciente", description = "Atualiza os dados de um paciente existente a partir do seu ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Paciente atualizado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos"),
            @ApiResponse(responseCode = "404", description = "Paciente não encontrado")
    })
    @PutMapping("/{id}")
    public ResponseEntity<PatientResponseDTO> updatePatient(@PathVariable UUID id, @RequestBody @Valid UpdatePatientDTO updatePatientDTO) {
        PatientResponseDTO updatedPatient = patientFacade.updatePatient(id, updatePatientDTO);
        return ResponseEntity.ok(updatedPatient);
    }

    @Operation(summary = "Excluir um paciente", description = "Remove um paciente do sistema a partir do seu ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Paciente excluído com sucesso"),
            @ApiResponse(responseCode = "404", description = "Paciente não encontrado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePatient(@PathVariable UUID id) {
        patientFacade.deletePatient(id);
        return ResponseEntity.noContent().build();
    }
}

