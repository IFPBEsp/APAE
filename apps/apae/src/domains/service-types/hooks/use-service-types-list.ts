"use client";

import { useEffect, useState } from "react";
import { toast } from "react-toastify";
import { bffFetchJson } from "@/lib/bff-client";
import { ServiceType } from "../service-types.types";

export function useServiceTypesList(searchName: string) {
  const [serviceTypes, setServiceTypes] = useState<ServiceType[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    async function fetchServiceTypes() {
      try {
        setLoading(true);
        setError(null);
        const data = await bffFetchJson<ServiceType[]>("/apae-geral/api/service-types");
        setServiceTypes(data);
      } catch (err) {
        const error = err as Error;
        setError(error.message);
        toast.error(error.message);
      } finally {
        setLoading(false);
      }
    }
    fetchServiceTypes();
  }, []);

  const filteredServiceTypes = serviceTypes.filter((serviceType) =>
    serviceType.area.toLowerCase().includes(searchName.toLowerCase())
  );

  return { serviceTypes: filteredServiceTypes, loading, error };
}
