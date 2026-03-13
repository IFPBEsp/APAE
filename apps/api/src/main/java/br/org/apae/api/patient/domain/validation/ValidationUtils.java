package br.org.apae.api.patient.domain.validation;

import br.org.apae.api.patient.domain.exceptions.InvalidDataException;

public class ValidationUtils {
  private ValidationUtils() {
  }

  public static void requireNonNullOrEmpty(String value, String fieldName) {
    if (value == null || value.trim().isEmpty()) {
      throw new InvalidDataException(fieldName + " não pode ser nulo ou vazio.");
    }
  }

  public static void requireNonNull(Object value, String fieldName) {
    if (value == null) {
      throw new InvalidDataException(fieldName + " não pode ser nulo.");
    }
  }
}
