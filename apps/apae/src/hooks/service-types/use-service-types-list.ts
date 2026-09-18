import { useEffect, useState } from "react";
import { toast } from "react-toastify";
import { ServiceType } from "@/types/service-type";

export function useServiceTypesList(searchName: string) {
  const [serviceTypes, setServiceTypes] = useState<ServiceType[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    async function fetchServiceTypes() {
      try {
        setLoading(true);
        setError(null);
        const response = await fetch("/apae-geral/api/service-types");
        if (!response.ok) {
          throw new Error("Falha ao buscar os tipos de atendimentos.");
        }
        const data: ServiceType[] = await response.json();
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
