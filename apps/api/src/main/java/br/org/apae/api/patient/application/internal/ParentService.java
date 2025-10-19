package br.org.apae.api.patient.application.internal;

import java.util.List;

import org.springframework.stereotype.Service;

import br.org.apae.api.common.dto.patient.request.parent.CreateParentDTO;
import br.org.apae.api.common.dto.patient.response.parent.ParentResponseDTO;
import br.org.apae.api.patient.application.mappers.ParentMapper;
import br.org.apae.api.patient.domain.model.Parent;
import br.org.apae.api.patient.domain.model.Patient;
import br.org.apae.api.patient.domain.repository.ParentRepository;
import jakarta.transaction.Transactional;

@Service
public class ParentService {
  private final ParentRepository parentRepository;
  private final ParentMapper parentMapper;

  public ParentService(ParentRepository parentRepository, ParentMapper parentMapper) {
    this.parentRepository = parentRepository;
    this.parentMapper = parentMapper;
  }

  @Transactional
  public List<ParentResponseDTO> createParents(List<CreateParentDTO> parentDTOs, Patient patient) {
    List<Parent> parents = this.parentMapper.toEntityList(parentDTOs, patient);

    List<Parent> savedParents = this.parentRepository.saveAll(parents);
    patient.setParents(savedParents);

    return this.parentMapper.toResponseDTOList(savedParents);
  }
}
