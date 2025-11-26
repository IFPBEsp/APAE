package br.org.apae.api.appointment.application.internal;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import br.org.apae.api.appointment.application.interfaces.ConsultationHistoryApplicationService;
import br.org.apae.api.appointment.domain.exceptions.ConsultationHistoryBadRequestException;
import br.org.apae.api.appointment.domain.exceptions.ConsultationHistoryConflictException;
import br.org.apae.api.appointment.domain.exceptions.ConsultationHistoryNotFoundException;
import br.org.apae.api.appointment.domain.model.ConsultationHistory;
import br.org.apae.api.appointment.domain.repository.ConsultationHistoryRepository;
import br.org.apae.api.appointment.mapper.ConsultationHistoryMapper;
import br.org.apae.api.common.dto.appointment.request.consultation_history.CreateConsultationHistoryDTO;
import br.org.apae.api.common.dto.appointment.request.consultation_history.UpdateConsultationHistoryDTO;
import br.org.apae.api.common.dto.appointment.response.consultation_history.ConsultationHistoryResponseDTO;

@Service
public class ConsultationHistoryApplicationServiceImpl implements ConsultationHistoryApplicationService {
  private final ConsultationHistoryRepository consultationHistoryRepository;
  private final ConsultationHistoryMapper consultationHistoryMapper;

  public ConsultationHistoryApplicationServiceImpl(ConsultationHistoryRepository consultationHistoryRepository,
      ConsultationHistoryMapper consultationHistoryMapper) {
    this.consultationHistoryRepository = consultationHistoryRepository;
    this.consultationHistoryMapper = consultationHistoryMapper;
  }

  @Override
  public void create(CreateConsultationHistoryDTO dto) {
    validateCreate(dto);

    ConsultationHistory entity = this.consultationHistoryMapper.toEntity(dto);
    this.consultationHistoryRepository.save(entity);
  }

  @Override
  public Page<ConsultationHistoryResponseDTO> findAll(Pageable pageable) {
    return this.consultationHistoryRepository.findAll(pageable).map(consultationHistoryMapper::toResponse);
  }

  @Override
  public ConsultationHistoryResponseDTO findById(UUID id) {
    ConsultationHistory consultation = this.consultationHistoryRepository.findById(id)
        .orElseThrow(ConsultationHistoryNotFoundException::new);
    return this.consultationHistoryMapper.toResponse(consultation);
  }

  @Override
  public ConsultationHistoryResponseDTO update(UUID id, UpdateConsultationHistoryDTO dto) {
    ConsultationHistory entity = this.consultationHistoryRepository.findById(id)
        .orElseThrow(ConsultationHistoryNotFoundException::new);

    if (!dto.performed() && (dto.justification() == null || dto.justification().trim().isEmpty())) {
      throw new ConsultationHistoryBadRequestException();
    }

    entity = this.consultationHistoryMapper.updateEntity(entity, dto);
    entity = this.consultationHistoryRepository.save(entity);

    return this.consultationHistoryMapper.toResponse(entity);
  }

  @Override
  public void delete(UUID id) {
    if (!this.consultationHistoryRepository.existsById(id)) {
      throw new ConsultationHistoryNotFoundException();
    }

    this.consultationHistoryRepository.deleteById(id);
  }

  private void validateCreate(CreateConsultationHistoryDTO dto) {
    if (!dto.performed() && (dto.justification() == null || dto.justification().trim().isEmpty())) {
      throw new ConsultationHistoryBadRequestException();
    }

    boolean exists = this.consultationHistoryRepository.existsByAppointmentIdAndConsultationDateAndConsultationTime(
        dto.appointmentId(),
        dto.consultationDate(),
        dto.consultationTime());

    if (exists) {
      throw new ConsultationHistoryConflictException();
    }
  }
}
