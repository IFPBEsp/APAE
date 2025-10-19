package br.org.apae.api.address.application.interfaces;

import br.org.apae.api.common.dto.address.AddressResponseDTO;
import br.org.apae.api.common.dto.address.CreateAddressDTO;

public interface AddressService {
  AddressResponseDTO createAddress(CreateAddressDTO createAddressDTO);
}
