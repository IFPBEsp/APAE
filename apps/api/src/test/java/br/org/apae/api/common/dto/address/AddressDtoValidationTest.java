package br.org.apae.api.common.dto.address;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AddressDtoValidationTest {

    private static Validator validator;

    @BeforeAll
    static void setUpValidator() {
        try (var factory = Validation.buildDefaultValidatorFactory()) {
            validator = factory.getValidator();
        }
    }

    @ParameterizedTest
    @ValueSource(strings = {"Rua 7 de Setembro", "Av. Epitácio Pessoa", "Rua D'Ávila, Lote 2 - Centro"})
    @DisplayName("CreateAddressDTO e UpdateAddressDTO devem aceitar ruas válidas")
    void shouldAcceptValidStreets(String street) {
        var createDto = new CreateAddressDTO("Esperança", "58135-000", "PB", "Centro", street, "100", "Casa");
        var updateDto = new UpdateAddressDTO("Esperança", "58135-000", "PB", "Centro", street, "100", "Casa");

        assertTrue(validator.validate(createDto).isEmpty());
        assertTrue(validator.validate(updateDto).isEmpty());
    }

    @ParameterizedTest
    @ValueSource(strings = {"Apto 101/B", "Casa (fundos)", "Bloco #2, 1º andar", ""})
    @DisplayName("CreateAddressDTO e UpdateAddressDTO devem aceitar complementos com /, # e parênteses")
    void shouldAcceptValidComplements(String complement) {
        var createDto = new CreateAddressDTO("Esperança", "58135-000", "PB", "Centro", "Rua A", "SN", complement);
        var updateDto = new UpdateAddressDTO("Esperança", "58135-000", "PB", "Centro", "Rua A", "SN", complement);

        assertTrue(validator.validate(createDto).isEmpty());
        assertTrue(validator.validate(updateDto).isEmpty());
    }

    @ParameterizedTest
    @ValueSource(strings = {"Olho d'Água", "Pau dos Ferros", "Esperança"})
    @DisplayName("CreateAddressDTO e UpdateAddressDTO devem aceitar cidades válidas")
    void shouldAcceptValidCities(String city) {
        var createDto = new CreateAddressDTO(city, "58135-000", "PB", "Centro", "Rua A", "12A", null);
        var updateDto = new UpdateAddressDTO(city, "58135-000", "PB", "Centro", "Rua A", "12A", null);

        assertTrue(validator.validate(createDto).isEmpty());
        assertTrue(validator.validate(updateDto).isEmpty());
    }

    @ParameterizedTest
    @ValueSource(strings = {"Paraíba", "pb", "P", "PBA"})
    @DisplayName("CreateAddressDTO e UpdateAddressDTO devem rejeitar estado fora do formato UF (^[A-Z]{2}$)")
    void shouldRejectInvalidStates(String state) {
        var createDto = new CreateAddressDTO("Esperança", "58135-000", state, "Centro", "Rua A", "100", null);
        var updateDto = new UpdateAddressDTO("Esperança", "58135-000", state, "Centro", "Rua A", "100", null);

        assertFalse(validator.validate(createDto).isEmpty());
        assertFalse(validator.validate(updateDto).isEmpty());
    }

    @Test
    @DisplayName("CreateAddressDTO e UpdateAddressDTO devem rejeitar cidade com números e complemento com @")
    void shouldRejectInvalidCityAndComplement() {
        var invalidCityCreate = new CreateAddressDTO("Cidade 123", "58135-000", "PB", "Centro", "Rua A", "100", null);
        var invalidCityUpdate = new UpdateAddressDTO("Cidade 123", "58135-000", "PB", "Centro", "Rua A", "100", null);
        assertFalse(validator.validate(invalidCityCreate).isEmpty());
        assertFalse(validator.validate(invalidCityUpdate).isEmpty());

        var invalidComplementCreate = new CreateAddressDTO("Esperança", "58135-000", "PB", "Centro", "Rua A", "100", "Apto @ 10");
        var invalidComplementUpdate = new UpdateAddressDTO("Esperança", "58135-000", "PB", "Centro", "Rua A", "100", "Apto @ 10");
        assertFalse(validator.validate(invalidComplementCreate).isEmpty());
        assertFalse(validator.validate(invalidComplementUpdate).isEmpty());
    }
}
