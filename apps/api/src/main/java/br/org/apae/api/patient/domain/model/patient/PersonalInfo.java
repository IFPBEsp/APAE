package br.org.apae.api.patient.domain.model.patient;

import java.time.LocalDate;
import static br.org.apae.api.patient.domain.validation.ValidationUtils.*;

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
    requireNonNullOrEmpty(contact, "Contato");
    requireNonNullOrEmpty(allergies, "Alergias");

    this.fullName = fullName;
    this.birthplace = birthplace;
    this.birthDate = birthDate;
    this.contact = contact;
    this.allergies = allergies;
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
