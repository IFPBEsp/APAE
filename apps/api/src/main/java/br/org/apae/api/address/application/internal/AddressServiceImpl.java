package br.org.apae.api.address.application.internal;

import java.util.UUID;

import org.springframework.stereotype.Service;

import br.org.apae.api.address.application.interfaces.AddressService;
import br.org.apae.api.address.application.mapper.AddressMapper;
import br.org.apae.api.address.domain.exceptions.AddressNotFoundException;
import br.org.apae.api.address.domain.model.Address;
import br.org.apae.api.address.domain.repository.AddressRepository;
import br.org.apae.api.common.dto.address.AddressResponseDTO;
import br.org.apae.api.common.dto.address.CreateAddressDTO;
import br.org.apae.api.common.dto.address.UpdateAddressDTO;
import jakarta.transaction.Transactional;

@Service
public class AddressServiceImpl implements AddressService {
  private final AddressRepository addressRepository;
  private final AddressMapper addressMapper;

  public AddressServiceImpl(AddressRepository addressRepository, AddressMapper addressMapper) {
    this.addressRepository = addressRepository;
    this.addressMapper = addressMapper;
  }

  @Override
  public AddressResponseDTO createAddress(CreateAddressDTO createAddressDTO) {
    Address address = this.addressMapper.toEntity(createAddressDTO);
    Address addressCreated = this.addressRepository.save(address);

    return addressMapper.toResponseDTO(addressCreated);
  }

  @Override
  @Transactional
  public AddressResponseDTO updateAddress(UUID addressId, UpdateAddressDTO updateAddressDto) {
    Address address = addressRepository.findById(addressId).orElseThrow(AddressNotFoundException::new);

    Address addressUpdated = addressMapper.updateEntityFromDto(address, updateAddressDto);

    return addressMapper.toResponseDTO(addressUpdated);
  }
}
