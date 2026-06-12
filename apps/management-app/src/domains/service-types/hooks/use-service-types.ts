"use client";

import { useEffect, useState } from "react";
import { toast } from "react-toastify";

import {
  createServiceType,
  deleteServiceType,
  listServiceTypes,
} from "../service-types.api";
import type { ServiceType } from "../service-types.types";

export function useServiceTypes() {
  const [serviceTypes, setServiceTypes] = useState<ServiceType[]>([]);
  const [isLoading, setIsLoading] = useState(true);
  const [isSaving, setIsSaving] = useState(false);
  const [error, setError] = useState<string | null>(null);
  const [searchName, setSearchName] = useState("");

  async function loadServiceTypes() {
    try {
      setIsLoading(true);
      setError(null);
      setServiceTypes(await listServiceTypes());
    } catch (error) {
      const message = error instanceof Error ? error.message : "Falha ao buscar os tipos de atendimento.";
      setError(message);
      toast.error(message);
    } finally {
      setIsLoading(false);
    }
  }

  useEffect(() => {
    loadServiceTypes();
  }, []);

  async function handleCreateServiceType(area: string) {
    try {
      setIsSaving(true);
      await createServiceType({ area });
      toast.success("Tipo de atendimento criado com sucesso!");
      await loadServiceTypes();
    } catch (error) {
      const message = error instanceof Error ? error.message : "Falha ao criar tipo de atendimento.";
      toast.error(message);
      throw error;
    } finally {
      setIsSaving(false);
    }
  }

  async function handleDeleteServiceType(id: string | number) {
    try {
      await deleteServiceType(id);
      setServiceTypes((current) => current.filter((serviceType) => serviceType.id !== id));
      toast.success("Tipo de atendimento removido com sucesso!");
    } catch (error) {
      const message = error instanceof Error ? error.message : "Falha ao remover tipo de atendimento.";
      toast.error(message);
      throw error;
    }
  }

  const filteredServiceTypes = serviceTypes.filter((serviceType) =>
    serviceType.area.toLowerCase().includes(searchName.toLowerCase()),
  );

  return {
    error,
    filteredServiceTypes,
    isLoading,
    isSaving,
    searchName,
    setSearchName,
    createServiceType: handleCreateServiceType,
    deleteServiceType: handleDeleteServiceType,
  };
}
