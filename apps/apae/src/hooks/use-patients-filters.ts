"use client";

import { useState, useEffect } from "react";
import { toast } from "react-toastify";

interface PatientFilterOptions {
  disorderOptions: string[];
  anoOptions: string[];
  cityOptions: string[];
  serviceAreaOptions: string[];
  isLoading: boolean;
  error: string | null;
}

export function usePatientFilters(): PatientFilterOptions {
  const [disorderOptions, setDisorderOptions] = useState<string[]>([]);
  const [anoOptions, setAnoOptions] = useState<string[]>([]);
  const [cityOptions, setCityOptions] = useState<string[]>([]);
  const [serviceAreaOptions, setServiceAreaOptions] = useState<string[]>([]);
  const [isLoading, setIsLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    const fetchFilterOptions = async () => {
      try {
        setIsLoading(true);
        setError(null);
        const disordersPromise = fetch('/apae-geral/api/patients/filtros/transtornos');
        const anosPromise = fetch('/apae-geral/api/patients/filtros/anos');
        const citiesPromise = fetch('/apae-geral/api/patients/filtros/cidades');
        const serviceAreaPromise = fetch('/apae-geral/api/patients/filtros/tipos-atendimento');

        const [
          disordersResponse,
          anosResponse,
          citiesResponse,
          serviceAreaResponse
        ] = await Promise.all([
          disordersPromise,
          anosPromise,
          citiesPromise,
          serviceAreaPromise
        ]);

        if (!disordersResponse.ok) throw new Error('Falha ao buscar transtornos');
        if (!anosResponse.ok) throw new Error('Falha ao buscar anos');
        if (!citiesResponse.ok) throw new Error('Falha ao buscar cidades');
        if (!serviceAreaResponse.ok) throw new Error('Falha ao buscar tipos de atendimentos');

        const disordersData = await disordersResponse.json();
        const anosData = await anosResponse.json();
        const citiesData = await citiesResponse.json();
        const serviceAreaData = await serviceAreaResponse.json();
        
        setDisorderOptions(disordersData);
        setAnoOptions(anosData);
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

  return { disorderOptions, anoOptions, cityOptions, serviceAreaOptions, isLoading, error };
}