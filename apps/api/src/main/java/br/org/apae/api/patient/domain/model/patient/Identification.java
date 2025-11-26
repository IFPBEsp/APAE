package br.org.apae.api.patient.domain.model.patient;

import java.time.LocalDate;

import br.org.apae.api.patient.domain.exceptions.InvalidDataException;

import static br.org.apae.api.patient.domain.validation.ValidationUtils.*;

public class Identification {
  private final String cpf;
  private final String rg;
  private final String cns;
  private final String nis;
  private final LocalDate issueDate;
  private final String issuingAgency;

  public Identification(String rg, String cpf, String cns, String nis, LocalDate issueDate, String issuingAgency) {
    requireNonNullOrEmpty(rg, "RG");
    requireNonNullOrEmpty(cpf, "CPF");
    requireNonNullOrEmpty(cns, "CNS");
    requireNonNullOrEmpty(nis, "NIS");
    requireNonNull(issueDate, "Data de emissão");
    requireNonNullOrEmpty(issuingAgency, "Órgão emissor");

    if (issueDate.isAfter(LocalDate.now())) {
      throw new InvalidDataException("Data de emissão não pode ser no futuro.");
    }

    this.rg = rg;
    this.cpf = cpf;
    this.cns = cns;
    this.nis = nis;
    this.issueDate = issueDate;
    this.issuingAgency = issuingAgency;
  }

  public String getRg() {
    return rg;
  }

  public String getCpf() {
    return cpf;
  }

  public String getCns() {
    return cns;
  }

  public String getNis() {
    return nis;
  }

  public LocalDate getIssueDate() {
    return issueDate;
  }

  public String getIssuingAgency() {
    return issuingAgency;
  }
}
