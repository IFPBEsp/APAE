package br.org.apae.api.controllers.absence;

import br.org.apae.api.appointment.application.interfaces.AbsenceApplicationService;
import br.org.apae.api.appointment.interfaces.controllers.AbsenceController;
import br.org.apae.api.common.dto.appointment.request.absence.CreateAbsenceDTO;
import br.org.apae.api.common.dto.appointment.response.absence.AbsenceResponseDTO;
import br.org.apae.api.common.dto.appointment.response.absence.JustifyAbsenceDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.UUID;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
public class AbsenceControllerImpl implements AbsenceController {

    private final AbsenceApplicationService service;

    public AbsenceControllerImpl(AbsenceApplicationService service) {
        this.service = service;
    }

    @Override
    public ResponseEntity<AbsenceResponseDTO> register(CreateAbsenceDTO dto) {
        AbsenceResponseDTO response = service.register(dto);

        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(response.id())
                .toUri();

        return ResponseEntity.created(uri).body(response);
    }

    @Override
    public ResponseEntity<Page<AbsenceResponseDTO>> findAll(
            UUID generatedId, UUID patientId, UUID professionalId, Pageable pageable) {

        Page<AbsenceResponseDTO> response = service.findAllByFilters(
                generatedId, patientId, professionalId, pageable);

        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<AbsenceResponseDTO> justifyAbsence(UUID id, JustifyAbsenceDTO dto) {
        AbsenceResponseDTO response = service.justify(id, dto);
        return ResponseEntity.ok(response);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public void handleIllegalArgument(IllegalArgumentException ex) {
        // Argumentos inválidos vindos do serviço (ex.: falta já registrada) resultam em 400.
    }
}