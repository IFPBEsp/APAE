"use client";

import { useState, useEffect } from "react";
import { toast } from "react-toastify";

interface PatientFilterOptions {
  transtornoOptions: string[];
  anoOptions: string[];
  cidadeOptions: string[];
  tipoAtendimentoOptions: string[];
  isLoading: boolean;
  error: string | null;
}

export function usePatientFilters(): PatientFilterOptions {
  const [transtornoOptions, setTranstornoOptions] = useState<string[]>([]);
  const [anoOptions, setAnoOptions] = useState<string[]>([]);
  const [cidadeOptions, setCidadeOptions] = useState<string[]>([]);
  const [tipoAtendimentoOptions, setTipoAtendimentoOptions] = useState<string[]>([]);
  const [isLoading, setIsLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    const fetchFilterOptions = async () => {
      try {
        setIsLoading(true);
        setError(null);
        const transtornosPromise = fetch('/api/patients/filtros/transtornos');
        const anosPromise = fetch('/api/patients/filtros/anos');
        const cidadesPromise = fetch('/api/patients/filtros/cidades');
        const tipoAtendimentosPromise = fetch('/api/patients/filtros/tipos-atendimento');

        const [
          transtornosResponse,
          anosResponse,
          cidadesResponse,
          tipoAtendimentosResponse
        ] = await Promise.all([
          transtornosPromise,
          anosPromise,
          cidadesPromise,
          tipoAtendimentosPromise
        ]);

        if (!transtornosResponse.ok) throw new Error('Falha ao buscar transtornos');
        if (!anosResponse.ok) throw new Error('Falha ao buscar anos');
        if (!cidadesResponse.ok) throw new Error('Falha ao buscar cidades');
        if (!tipoAtendimentosResponse.ok) throw new Error('Falha ao buscar tipos de atendimentos');

        const transtornosData = await transtornosResponse.json();
        const anosData = await anosResponse.json();
        const cidadesData = await cidadesResponse.json();
        const tipoAtendimentosData = await tipoAtendimentosResponse.json();
        
        setTranstornoOptions(transtornosData);
        setAnoOptions(anosData);
        setCidadeOptions(cidadesData);
        setTipoAtendimentoOptions(tipoAtendimentosData);

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

  return { transtornoOptions, anoOptions, cidadeOptions, tipoAtendimentoOptions, isLoading, error };
}