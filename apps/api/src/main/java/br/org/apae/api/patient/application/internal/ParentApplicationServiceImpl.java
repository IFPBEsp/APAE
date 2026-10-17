package br.org.apae.api.patient.application.internal;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.org.apae.api.common.dto.patient.request.parent.CreateParentDTO;
import br.org.apae.api.common.dto.patient.request.parent.UpdateParentDTO;
import br.org.apae.api.common.dto.patient.response.parent.ParentResponseDTO;
import br.org.apae.api.patient.application.interfaces.ParentApplicationService;
import br.org.apae.api.patient.application.mappers.ParentMapper;
import br.org.apae.api.patient.domain.model.Parent;
import br.org.apae.api.patient.domain.repository.ParentRepository;

@Service
public class ParentApplicationServiceImpl implements ParentApplicationService {
  private final ParentRepository parentRepository;
  private final ParentMapper parentMapper;

  private final PatientDomainService patientDomainService;

  public ParentApplicationServiceImpl(ParentRepository parentRepository, ParentMapper parentMapper,
      PatientDomainService patientDomainService) {
    this.parentRepository = parentRepository;
    this.parentMapper = parentMapper;
    this.patientDomainService = patientDomainService;
  }

  @Override
  @Transactional
  public List<ParentResponseDTO> createParents(List<CreateParentDTO> parentDtos, UUID patientId) {
    patientDomainService.getByIdOrThrow(patientId);

    List<Parent> parents = parentMapper.toEntityList(parentDtos, patientId);

    List<Parent> savedParents = parentRepository.saveAll(parents);

    return parentMapper.toResponseDTOList(savedParents);
  }

  @Override
  @Transactional(readOnly = true)
  public List<ParentResponseDTO> findParentsByPatientId(UUID patientId) {
    List<Parent> parents = parentRepository.findAllByPatientId(patientId);

    return parentMapper.toResponseDTOList(parents);
  }

  @Override
  @Transactional
  public List<ParentResponseDTO> updateParents(List<UpdateParentDTO> parentsDtos, UUID patientId) {
    List<Parent> existingParents = parentRepository.findAllByPatientId(patientId);
    parentRepository.deleteAll(existingParents);

    List<Parent> updatedParents = parentMapper.updateEntityListFromDto(parentsDtos, patientId);

    parentRepository.saveAll(updatedParents);

    return parentMapper.toResponseDTOList(updatedParents);
  }
}
