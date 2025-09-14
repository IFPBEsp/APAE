import { useEffect, useState } from "react";
import { UseFormReturn } from "react-hook-form";

export function useEstadosECidades(form: UseFormReturn<any>) {
  const [estados, setEstados] = useState<any[]>([]);
  const [cidades, setCidades] = useState<any[]>([]);

  useEffect(() => {
    fetch(
      "https://servicodados.ibge.gov.br/api/v1/localidades/estados?orderBy=nome"
    )
      .then((res) => res.json())
      .then((data) => setEstados(data));
  }, []);

  useEffect(() => {
    const estadoSelecionado = form.getValues("estado");
    if (estadoSelecionado) {
      fetch(
        `https://servicodados.ibge.gov.br/api/v1/localidades/estados/${estadoSelecionado}/municipios`
      )
        .then((res) => res.json())
        .then((data) => setCidades(data));
    }
  }, [form.watch("estado")]);

  return { estados, cidades };
}
