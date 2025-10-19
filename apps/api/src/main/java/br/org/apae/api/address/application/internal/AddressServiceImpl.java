package br.org.apae.api.address.application.internal;

import br.org.apae.api.address.application.interfaces.AddressService;
import br.org.apae.api.address.application.mapper.AddressMapper;
import br.org.apae.api.address.domain.model.Address;
import br.org.apae.api.address.domain.repository.AddressRepository;
import br.org.apae.api.common.dto.address.AddressResponseDTO;
import br.org.apae.api.common.dto.address.CreateAddressDTO;

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
}
