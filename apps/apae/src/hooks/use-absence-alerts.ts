"use client";

import { useCallback, useState, useEffect } from "react";
import { bffFetchJson } from "@/lib/bff-client";

const MIN_ABSENCES_FOR_ALERT = 3;

interface AbsenceAlertsResult {
  alertPatientIds: Set<string>;
  isLoading: boolean;
  refreshAlerts: () => Promise<void>;
}

export function useAbsenceAlerts(): AbsenceAlertsResult {
  const [alertPatientIds, setAlertPatientIds] = useState<Set<string>>(
    new Set()
  );
  const [isLoading, setIsLoading] = useState(true);

  const fetchAbsences = useCallback(async () => {
    try {
      setIsLoading(true);
      const ids = await bffFetchJson<string[]>(
        `/apae-geral/api/patients/absence-alert-ids?minAbsences=${MIN_ABSENCES_FOR_ALERT}`
      );

      setAlertPatientIds(new Set(ids));
    } catch (error) {
      console.error("[useAbsenceAlerts] Erro ao buscar ausências:", error);
    } finally {
      setIsLoading(false);
    }
  }, []);

  useEffect(() => {
    void fetchAbsences();
  }, [fetchAbsences]);

  return { alertPatientIds, isLoading, refreshAlerts: fetchAbsences };
}

