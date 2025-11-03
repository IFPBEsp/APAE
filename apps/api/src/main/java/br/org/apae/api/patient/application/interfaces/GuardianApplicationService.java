package br.org.apae.api.patient.application.interfaces;

import java.util.UUID;

import br.org.apae.api.common.dto.patient.request.guardian.CreateGuardianDTO;
import br.org.apae.api.common.dto.patient.request.guardian.UpdateGuardianDTO;
import br.org.apae.api.common.dto.patient.response.guardian.GuardianResponseDTO;

public interface GuardianApplicationService {
  GuardianResponseDTO createGuardian(CreateGuardianDTO createGuardianDto, UUID patientId);

  GuardianResponseDTO findGuardianByPatientId(UUID patientId);

  GuardianResponseDTO updateGuardian(UpdateGuardianDTO updateGuardianDTO, UUID patientId);

  void deleteGuardian(UUID patientId);
}
