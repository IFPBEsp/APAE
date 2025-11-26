package br.org.apae.api.appointment.application.interfaces;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import br.org.apae.api.common.dto.appointment.request.consultation_history.CreateConsultationHistoryDTO;
import br.org.apae.api.common.dto.appointment.request.consultation_history.UpdateConsultationHistoryDTO;
import br.org.apae.api.common.dto.appointment.response.consultation_history.ConsultationHistoryResponseDTO;

public interface ConsultationHistoryApplicationService {
  void create(CreateConsultationHistoryDTO dto);

  Page<ConsultationHistoryResponseDTO> findAll(Pageable pageable);

  ConsultationHistoryResponseDTO findById(UUID id);

  ConsultationHistoryResponseDTO update(UUID id, UpdateConsultationHistoryDTO dto);

  void delete(UUID id);
}
