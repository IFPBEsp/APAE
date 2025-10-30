package br.org.apae.api.controllers.appointment;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import br.org.apae.api.appointment.application.interfaces.ConsultationHistoryApplicationService;
import br.org.apae.api.appointment.interfaces.controllers.ConsultationHistoryController;
import br.org.apae.api.common.dto.appointment.request.consultation_history.CreateConsultationHistoryDTO;
import br.org.apae.api.common.dto.appointment.request.consultation_history.UpdateConsultationHistoryDTO;
import br.org.apae.api.common.dto.appointment.response.consultation_history.ConsultationHistoryResponseDTO;
import jakarta.validation.Valid;

@RestController
public class ConsultationHistoryControllerImpl implements ConsultationHistoryController {
  private final ConsultationHistoryApplicationService consultationHistoryApplicationService;

  public ConsultationHistoryControllerImpl(
      ConsultationHistoryApplicationService consultationHistoryApplicationService) {
    this.consultationHistoryApplicationService = consultationHistoryApplicationService;
  }

  @Override
  public ResponseEntity<Void> create(@Valid CreateConsultationHistoryDTO dto) {
    consultationHistoryApplicationService.create(dto);
    return ResponseEntity.status(201).build();
  }

  @Override
  public ResponseEntity<Page<ConsultationHistoryResponseDTO>> findAll(Pageable pageable) {
    return ResponseEntity.ok(consultationHistoryApplicationService.findAll(pageable));
  }

  @Override
  public ResponseEntity<ConsultationHistoryResponseDTO> findById(UUID id) {
    ConsultationHistoryResponseDTO consultation = consultationHistoryApplicationService.findById(id);
    return ResponseEntity.ok(consultation);
  }

  @Override
  public ResponseEntity<ConsultationHistoryResponseDTO> update(UUID id, @Valid UpdateConsultationHistoryDTO dto) {
    ConsultationHistoryResponseDTO consultationUpdated = consultationHistoryApplicationService.update(id, dto);
    return ResponseEntity.ok(consultationUpdated);
  }

  @Override
  public ResponseEntity<Void> delete(UUID id) {
    consultationHistoryApplicationService.delete(id);
    return ResponseEntity.noContent().build();
  }
}
