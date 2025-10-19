package br.org.apae.api.patient.application.internal;

import org.springframework.stereotype.Service;

import br.org.apae.api.address.application.interfaces.AddressService;
import br.org.apae.api.common.dto.address.AddressResponseDTO;
import br.org.apae.api.common.dto.patient.request.guardian.CreateGuardianDTO;
import br.org.apae.api.common.dto.patient.response.guardian.GuardianResponseDTO;
import br.org.apae.api.patient.application.mappers.GuardianMapper;
import br.org.apae.api.patient.domain.model.Guardian;
import br.org.apae.api.patient.domain.repository.GuardianRepository;
import jakarta.transaction.Transactional;

@Service
public class GuardianService {
  private final GuardianRepository guardianRepository;
  private final GuardianMapper guardianMapper;
  private final AddressService addressService;

  public GuardianService(GuardianRepository guardianRepository, GuardianMapper guardianMapper,
      AddressService addressService) {
    this.guardianRepository = guardianRepository;
    this.guardianMapper = guardianMapper;
    this.addressService = addressService;
  }

  @Transactional
  public GuardianResponseDTO createGuardian(CreateGuardianDTO createGuardianDTO) {
    AddressResponseDTO addressDto = this.addressService.createAddress(createGuardianDTO.address());

    Guardian guardian = this.guardianMapper.toEntity(createGuardianDTO, addressDto);
    Guardian guardianSaved = this.guardianRepository.save(guardian);

    return this.guardianMapper.toResponseDTO(guardianSaved);
  }
}
