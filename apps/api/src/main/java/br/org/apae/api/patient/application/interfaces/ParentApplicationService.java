package br.org.apae.api.patient.application.interfaces;

import java.util.List;
import java.util.UUID;

import br.org.apae.api.common.dto.patient.request.parent.CreateParentDTO;
import br.org.apae.api.common.dto.patient.request.parent.UpdateParentDTO;
import br.org.apae.api.common.dto.patient.response.parent.ParentResponseDTO;

public interface ParentApplicationService {
  List<ParentResponseDTO> createParents(List<CreateParentDTO> parentDtos, UUID patientId);

  List<ParentResponseDTO> findParentsByPatientId(UUID patientId);

  List<ParentResponseDTO> updateParents(List<UpdateParentDTO> parentDtos, UUID patientId);

  void deleteParents(UUID patientId);
}
