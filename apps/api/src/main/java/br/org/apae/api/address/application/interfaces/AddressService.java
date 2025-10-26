package br.org.apae.api.address.application.interfaces;

import java.util.UUID;

import br.org.apae.api.common.dto.address.AddressResponseDTO;
import br.org.apae.api.common.dto.address.CreateAddressDTO;
import br.org.apae.api.common.dto.address.UpdateAddressDTO;

public interface AddressService {
  AddressResponseDTO createAddress(CreateAddressDTO createAddressDTO);

  AddressResponseDTO updateAddress(UUID addressId, UpdateAddressDTO updateAddressDTO);
}
