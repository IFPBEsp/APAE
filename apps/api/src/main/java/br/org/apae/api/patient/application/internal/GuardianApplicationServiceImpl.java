package br.org.apae.api.patient.application.internal;

import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.org.apae.api.common.dto.patient.request.guardian.CreateGuardianDTO;
import br.org.apae.api.common.dto.patient.request.guardian.UpdateGuardianDTO;
import br.org.apae.api.common.dto.patient.response.guardian.GuardianResponseDTO;
import br.org.apae.api.patient.application.interfaces.GuardianApplicationService;
import br.org.apae.api.patient.application.mappers.GuardianMapper;
import br.org.apae.api.patient.domain.exceptions.GuardianNotFoundException;
import br.org.apae.api.patient.domain.model.Guardian;
import br.org.apae.api.patient.domain.repository.GuardianRepository;

@Service
public class GuardianApplicationServiceImpl implements GuardianApplicationService {
  private final GuardianRepository guardianRepository;
  private final GuardianMapper guardianMapper;

  private final PatientDomainService patientDomainService;

  public GuardianApplicationServiceImpl(GuardianRepository guardianRepository, GuardianMapper guardianMapper,
      PatientDomainService patientDomainService) {
    this.guardianRepository = guardianRepository;
    this.guardianMapper = guardianMapper;
    this.patientDomainService = patientDomainService;
  }

  @Override
  @Transactional
  public GuardianResponseDTO createGuardian(CreateGuardianDTO createGuardianDTO, UUID patientId) {
    patientDomainService.getByIdOrThrow(patientId);

    Guardian guardian = guardianMapper.toEntity(createGuardianDTO, patientId);
    Guardian guardianSaved = guardianRepository.save(guardian);

    return guardianMapper.toResponseDTO(guardianSaved);
  }

  @Override
  @Transactional(readOnly = true)
  public GuardianResponseDTO findGuardianByPatientId(UUID patientId) {
    Guardian guardian = guardianRepository.findByPatientId(patientId).orElseThrow(GuardianNotFoundException::new);

    return guardianMapper.toResponseDTO(guardian);
  }

  @Override
  @Transactional
  public GuardianResponseDTO updateGuardian(UpdateGuardianDTO updateGuardianDTO, UUID patientId) {
    Guardian guardian = guardianRepository.findByPatientId(patientId).orElseThrow(GuardianNotFoundException::new);

    Guardian updatedGuardian = guardianMapper.updateEntityFromDto(guardian, updateGuardianDTO, patientId);

    guardianRepository.save(updatedGuardian);

    return guardianMapper.toResponseDTO(updatedGuardian);
  }

  @Override
  @Transactional
  public void deleteGuardian(UUID patientId) {
    Guardian guardian = guardianRepository.findByPatientId(patientId).orElseThrow(GuardianNotFoundException::new);

    guardianRepository.deleteById(guardian.getId());
  }
}
