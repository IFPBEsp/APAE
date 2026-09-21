import { useEffect, useState } from "react";
import { UseFormReturn } from "react-hook-form";
import { FormValues } from "@/components/forms/form-health-professional";

export type State = { id: number; nome: string; sigla: string };
export type City = { id: number; nome: string };

export function useStatesCities(form: UseFormReturn<FormValues>) {
  const [states, setStates] = useState<State[]>([]);
  const [cities, setCities] = useState<City[]>([]);

  useEffect(() => {
    fetch("https://servicodados.ibge.gov.br/api/v1/localidades/estados?orderBy=nome")
      .then((res) => res.json())
      .then((data: State[]) => setStates(data));
  }, []);

  useEffect(() => {
    const subscription = form.watch((values, { name }) => {
      if (name === "state") {
        const selectedState = values.state;
        if (!selectedState) {
          setCities([]);
          return;
        }
        const state = states.find((e) => e.sigla === selectedState);
        if (!state) {
          setCities([]);
          return;
        }
        fetch(`https://servicodados.ibge.gov.br/api/v1/localidades/estados/${state.id}/municipios`)
          .then((res) => res.json())
          .then((data: City[]) => setCities(data))
          .catch(() => setCities([]));
      }
    });
    return () => subscription.unsubscribe();
  }, [form, states]);

  return { states, cities };
}