package br.org.apae.api.address.domain.exceptions;

public class AddressNotFoundException extends RuntimeException {
  private static final String MESSAGE = "Endereço não encontrado.";

  public AddressNotFoundException() {
    super(MESSAGE);
  }
}
