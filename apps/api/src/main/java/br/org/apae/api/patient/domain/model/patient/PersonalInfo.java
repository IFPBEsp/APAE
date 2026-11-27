package br.org.apae.api.patient.domain.model.patient;

import java.time.LocalDate;
import static br.org.apae.api.patient.domain.validation.ValidationUtils.requireNonNull;
import static br.org.apae.api.patient.domain.validation.ValidationUtils.requireNonNullOrEmpty;

public class PersonalInfo {
  private final String fullName;
  private final String birthplace;
  private final LocalDate birthDate;
  private final String contact;
  private final String allergies;
  private final boolean isStudent;

  public PersonalInfo(String fullName, String birthplace, LocalDate birthDate,
      String contact, String allergies, boolean isStudent) {

    requireNonNullOrEmpty(fullName, "Nome completo");
    requireNonNullOrEmpty(birthplace, "Naturalidade");
    requireNonNull(birthDate, "Data de nascimento");

    this.fullName = fullName;
    this.birthplace = birthplace;
    this.birthDate = birthDate;
    this.contact = (contact == null || contact.isBlank()) ? null : contact;
    this.allergies = (allergies == null || allergies.isBlank()) ? null : allergies;
    this.isStudent = isStudent;
  }

  public String getFullName() {
    return fullName;
  }

  public String getBirthplace() {
    return birthplace;
  }

  public LocalDate getBirthDate() {
    return birthDate;
  }

  public String getContact() {
    return contact;
  }

  public String getAllergies() {
    return allergies;
  }

  public boolean isStudent() {
    return isStudent;
  }
}
