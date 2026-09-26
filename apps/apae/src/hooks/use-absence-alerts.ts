"use client";

import { useState, useEffect } from "react";

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
        const response = await fetch(
          "/apae-geral/api/patients/with-absences?minAbsences=3"
        );

        if (!response.ok) {
          throw new Error("Erro ao buscar pacientes com faltas");
        }

        const data = await response.json();
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
