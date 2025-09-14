import { useEffect, useState } from "react";
import { UseFormReturn } from "react-hook-form";
import { FormValues } from "@/components/forms/form-health-professional";

export type Estado = { id: number; nome: string; sigla: string };
export type Cidade = { id: number; nome: string };

export function useEstadosECidades(form: UseFormReturn<FormValues>) {
  const [estados, setEstados] = useState<Estado[]>([]);
  const [cidades, setCidades] = useState<Cidade[]>([]);

  useEffect(() => {
    fetch("https://servicodados.ibge.gov.br/api/v1/localidades/estados?orderBy=nome")
      .then((res) => res.json())
      .then((data: Estado[]) => setEstados(data));
  }, []);

  useEffect(() => {
    const subscription = form.watch((values, { name }) => {
      if (name === "estado") {
        const estadoSelecionado = values.estado;
        if (!estadoSelecionado) {
          setCidades([]);
          return;
        }

        const estado = estados.find((e) => e.sigla === estadoSelecionado);
        if (!estado) {
          setCidades([]);
          return;
        }

        fetch(`https://servicodados.ibge.gov.br/api/v1/localidades/estados/${estado.id}/municipios`)
          .then((res) => res.json())
          .then((data: Cidade[]) => setCidades(data))
          .catch(() => setCidades([]));
      }
    });

    return () => subscription.unsubscribe();
  }, [form, estados]);

  return { estados, cidades };
}
