package br.org.apae.api.patient.domain.model.patient;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static br.org.apae.api.patient.domain.validation.ValidationUtils.*;

public class BirthRecord {
  private final String birthCertificateNumber;
  private final String registryOffice;
  private final String fls;
  private final String book;
  private final LocalDate registrationDate;

  public BirthRecord(String birthCertificateNumber, String registryOffice,
      String fls, String book, LocalDate registrationDate) {

    requireNonNullOrEmpty(birthCertificateNumber, "Número do registro de nascimento");
    requireNonNullOrEmpty(registryOffice, "Cartório");
    requireNonNullOrEmpty(fls, "FLS");
    requireNonNullOrEmpty(book, "Livro");
    requireNonNull(registrationDate, "Data de registro");

    if (registrationDate.isAfter(LocalDate.now())) {
      throw new IllegalArgumentException("Data de registro não pode ser no futuro: " + registrationDate.format(DateTimeFormatter.ISO_DATE));
    }

    this.birthCertificateNumber = birthCertificateNumber;
    this.registryOffice = registryOffice;
    this.fls = fls;
    this.book = book;
    this.registrationDate = registrationDate;
  }

  public String getBirthCertificateNumber() {
    return birthCertificateNumber;
  }

  public String getRegistryOffice() {
    return registryOffice;
  }

  public String getFls() {
    return fls;
  }

  public String getBook() {
    return book;
  }

  public LocalDate getRegistrationDate() {
    return registrationDate;
  }
}
