"use client";

import { useState, useEffect } from "react";
import { bffFetchJson } from "@/lib/bff-client";

interface AbsenceAlertsResult {
  alertPatientIds: Set<string>;
  isLoading: boolean;
}

export function useAbsenceAlerts(): AbsenceAlertsResult {
  const [alertPatientIds, setAlertPatientIds] = useState<Set<string>>(
    new Set()
  );
  const [isLoading, setIsLoading] = useState(true);

  useEffect(() => {
    const fetchAbsences = async () => {
      try {
        setIsLoading(true);
        const data = await bffFetchJson<{ content: Array<{ patient: { id: string } }> }>(
          "/apae-geral/api/patients/with-absences?minAbsences=3"
        );
        const absencesList = data.content || [];
        const idsSet = new Set<string>(
          absencesList.map((item: { patient: { id: string } }) => item.patient.id)
        );

        setAlertPatientIds(idsSet);
      } catch (error) {
        console.error("[useAbsenceAlerts] Erro ao buscar ausências:", error);
      } finally {
        setIsLoading(false);
      }
    };

    fetchAbsences();
  }, []);

  return { alertPatientIds, isLoading };
}
