package br.org.apae.api.patient.application.internal;

import java.util.UUID;

import org.springframework.stereotype.Service;

import br.org.apae.api.patient.domain.exceptions.PatientNotFoundException;
import br.org.apae.api.patient.domain.model.Patient;
import br.org.apae.api.patient.domain.repository.PatientRepository;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PatientDomainService {
  private final PatientRepository patientRepository;

  public PatientDomainService(PatientRepository patientRepository) {
    this.patientRepository = patientRepository;
  }

  @Transactional(readOnly = true)
  public Patient getByIdOrThrow(UUID id) {
    return patientRepository.findById(id)
        .orElseThrow(PatientNotFoundException::new);
  }
}
