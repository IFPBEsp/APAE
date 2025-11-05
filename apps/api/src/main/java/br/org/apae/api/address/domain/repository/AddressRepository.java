package br.org.apae.api.address.domain.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import br.org.apae.api.address.domain.model.Address;

public interface AddressRepository extends JpaRepository<Address, UUID> {

}
