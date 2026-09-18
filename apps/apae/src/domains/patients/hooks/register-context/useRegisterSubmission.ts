import { useCallback } from "react";
import { MembersRegisterState } from "./types";
import { serializeCivilDate } from "@/lib/date";

export function useRegisterSubmission(state: MembersRegisterState, STORAGE_KEY: string) {
  return useCallback(
    async (id?: string) => {
      const { personal, address, additionals, guardian, kinships, profile } =
        state;

      const parseIncome = (val: string) => {
        const clean = String(val).replace(/[^\d]/g, "");
        const num = parseFloat(clean) * 0.01;
        return isNaN(num) ? 0.0 : num;
      };

      interface PatientPayload {
        fullName: string;
        nationality: string;
        birthDate: string | null;
        contact: string;
        birthCertificateNumber: string;
        registryOffice: string;
        fls: string;
        book: string;
        rg: string;
        issueDate: string | null;
        issuingAgency: string;
        cpf: string;
        cns: string;
        nis: string;
        registrationDate: Date | string | null;
        allergies: string;
        continuousMedication: string;
        isStudent: boolean;
        address: {
          city: string;
          cep: string;
          state: string;
          neighborhood: string;
          street: string;
          number: string;
          complement: string;
        };
        guardian: {
          name: string;
          contact: string;
          kinship: string;
          address: {
            city: string;
            cep: string;
            state: string;
            neighborhood: string;
            street: string;
            number: string;
            complement: string;
          };
        };
        parents: {
          name: string;
          rg: string;
          cpf: string;
          profession: string;
          isAlive: boolean;
          kinship: string;
        }[];
        vaccineNames: { name: string }[];
        annualRegistry: {
          bpc: boolean;
          diseases: string;
          continuousMedication: string;
          serviceArea: { area: string }[];
          familyIncome: number;
          year: number;
          disorders: { name: string }[];
        };
      }

      const annualRegistryData = {
        bpc: additionals.bpc,
        diseases: additionals.diseases || "Nenhuma",
        continuousMedication: additionals.medications || "Nenhum",
        serviceArea: additionals.care.types.map((area: string) => ({ area })),
        familyIncome: parseIncome(additionals.householdIncome),
        year: new Date().getFullYear(),
        disorders: additionals.disability.types.map((name: string) => ({
          name,
        })),
      };

      const patient: PatientPayload = {
        fullName: personal.name || "Não informado",
        nationality: personal.birth.place || "Brasileiro",
        birthDate: serializeCivilDate(personal.birth.date),
        contact: personal.phone || "Não informado",
        birthCertificateNumber: personal.birth.certificate || "0",
        registryOffice: "Cartorio",
        fls: "0",
        book: "0",
        rg: personal.rg.number || "0",
        issueDate: serializeCivilDate(personal.rg.issuing.date),
        issuingAgency: personal.rg.issuing.body || "SSP/SP",
        cpf: personal.cpf,
        cns: personal.cns || "000 0000 0000 0000",
        nis: personal.nis || "0",
        registrationDate: serializeCivilDate(personal.rg.issuing.date),
        allergies: additionals.allergies || "Nenhuma",
        continuousMedication: additionals.medications || "Nenhum",

        isStudent: profile.role === "student",
        address: {
          city: address.city || "Não informado",
          cep: address.cep || "00000-000",
          state: address.state || "Não informado",
          neighborhood: address.neighborhood || "Não informado",
          street: address.street || "Não informado",
          number: address.number || "S/N",
          complement: address.complement || "",
        },
        guardian: {
          name: guardian.name || "Não informado",
          contact: guardian.contact || "Não informado",
          kinship: guardian.kinship || "Não informado",
          address: {
            city: guardian.address.city || "Não informado",
            cep: guardian.address.cep || "00000-000",
            state: guardian.address.state || "Não informado",
            neighborhood: guardian.address.neighborhood || "Não informado",
            street: guardian.address.street || "Não informado",
            number: guardian.address.number || "SN",
            complement: guardian.address.complement || "",
          },
        },
        parents: kinships.map((k) => ({
          name: k.name || "Não informado",
          rg: k.rg || "0",
          cpf: k.cpf || "000.000.000-00",
          profession: k.occupation || "Não informado",
          isAlive: k.alive,
          kinship: k.type || "Pai/Mãe",
        })),
        vaccineNames: additionals.vaccines.map((v) => ({ name: v })),
        annualRegistry: annualRegistryData,
      };

      if (id) {
        const res = await fetch(`/apae-geral/api/patients/${id}`, {
          method: "PUT",
          headers: { "Content-Type": "application/json" },
          body: JSON.stringify(patient),
        });

        if (res.ok) {
          localStorage.removeItem(STORAGE_KEY);
        }

        if (profile.photo instanceof File && res.ok) {
          const photoFormData = new FormData();
          photoFormData.append("photo", profile.photo);
          await fetch(`/apae-geral/api/patients/${id}/photo`, {
            method: "PUT",
            body: photoFormData,
          });
        }

        const data = await res.json().catch(() => ({}));
        return { status: res.status, data };
      } else {
        patient.registrationDate = serializeCivilDate(new Date());

        const formData = new FormData();
        formData.append(
          "patient",
          new Blob([JSON.stringify(patient)], { type: "application/json" }),
        );

        if (profile.photo instanceof File) {
          formData.append("photo", profile.photo);
        }

        if (additionals.disability.report instanceof File) {
          formData.append("reports", additionals.disability.report);
        }

        if (additionals.care.referral instanceof File) {
          formData.append("referrals", additionals.care.referral);
        }

        const res = await fetch("/apae-geral/api/patients", {
          method: "POST",
          body: formData,
        });

        if (res.ok) {
          localStorage.removeItem(STORAGE_KEY);
        }

        const data = await res.json().catch(() => ({}));
        return { status: res.status, data };
      }
    },
    [state, STORAGE_KEY],
  );
}
