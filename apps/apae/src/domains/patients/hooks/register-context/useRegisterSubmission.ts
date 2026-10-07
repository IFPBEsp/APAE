import { useCallback } from "react";
import type { MembersRegisterState } from "./types";
import { serializeCivilDate } from "@/lib/date";
import { buildPatientPayload } from "@/domains/patients/shared/patient.utils";

export function useRegisterSubmission(state: MembersRegisterState, STORAGE_KEY: string) {
  return useCallback(
    async (id?: string) => {
      const { additionals, profile } = state;
      const patient = buildPatientPayload(state);

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
      }

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
    },
    [state, STORAGE_KEY],
  );
}
