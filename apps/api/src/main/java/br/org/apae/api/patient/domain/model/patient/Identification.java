package br.org.apae.api.patient.domain.model.patient;

import java.time.LocalDate;

import br.org.apae.api.patient.domain.exceptions.InvalidDataException;

import static br.org.apae.api.patient.domain.validation.ValidationUtils.requireNonNullOrEmpty;

public class Identification {
  private final String cpf;
  private final String rg;
  private final String cns;
  private final String nis;
  private final LocalDate issueDate;
  private final String issuingAgency;

  public Identification(String rg, String cpf, String cns, String nis, LocalDate issueDate, String issuingAgency) {
    requireNonNullOrEmpty(cpf, "CPF");
    requireNonNullOrEmpty(cns, "CNS");

    boolean hasRG = rg != null && !rg.isBlank();

    if (hasRG) {
      if (issueDate == null) {
            throw new InvalidDataException("Data de emissão é obrigatória quando há RG.");
        }

      if (issueDate.isAfter(LocalDate.now())) {
        throw new InvalidDataException("Data de emissão não pode ser no futuro.");
      }
    } else if (issueDate != null || (issuingAgency != null && !issuingAgency.isBlank())) {
      throw new InvalidDataException("Data de emissão e órgão emissor não podem ser informados sem RG.");
    }

    this.rg = hasRG ? rg : null;
    this.cpf = cpf;
    this.cns = cns;
    this.nis = (nis == null || nis.isBlank()) ? null : nis;
    this.issueDate = hasRG ? issueDate : null;
    this.issuingAgency = hasRG ? issuingAgency : null;
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
