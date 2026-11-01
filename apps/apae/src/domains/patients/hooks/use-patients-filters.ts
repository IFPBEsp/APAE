"use client";

import { useState, useEffect } from "react";
import { toast } from "react-toastify";
import { bffFetchJson } from "@/lib/bff-client";

interface PatientFilterOptions {
  disorderOptions: string[];
  yearOptions: string[];
  cityOptions: string[];
  serviceAreaOptions: string[];
  isLoading: boolean;
  error: string | null;
}

export function usePatientFilters(): PatientFilterOptions {
  const [disorderOptions, setDisorderOptions] = useState<string[]>([]);
  const [yearOptions, setYearOptions] = useState<string[]>([]);
  const [cityOptions, setCityOptions] = useState<string[]>([]);
  const [serviceAreaOptions, setServiceAreaOptions] = useState<string[]>([]);
  const [isLoading, setIsLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    const fetchFilterOptions = async () => {
      try {
        setIsLoading(true);
        setError(null);
        const [
          disordersData,
          yearsData,
          citiesData,
          serviceAreaData,
        ] = await Promise.all([
          bffFetchJson<string[]>("/apae-geral/api/patients/filtros/transtornos"),
          bffFetchJson<string[]>("/apae-geral/api/patients/filtros/anos"),
          bffFetchJson<string[]>("/apae-geral/api/patients/filtros/cidades"),
          bffFetchJson<string[]>("/apae-geral/api/patients/filtros/tipos-atendimento"),
        ]);

        setDisorderOptions(disordersData);
        setYearOptions(yearsData);
        setCityOptions(citiesData);
        setServiceAreaOptions(serviceAreaData);

      } catch (err) {
        const errorMsg = "Não foi possível carregar os filtros.";
        console.error("Erro ao buscar opções de filtro:", err);
        setError(errorMsg);
        toast.error(errorMsg);
      } finally {
        setIsLoading(false);
      }
    };
    
    fetchFilterOptions();
  }, []);

  return { disorderOptions, yearOptions, cityOptions, serviceAreaOptions, isLoading, error };
}