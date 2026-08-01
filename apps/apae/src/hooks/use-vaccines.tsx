"use client";

import {
  createContext,
  useCallback,
  useContext,
  useEffect,
  useState,
} from "react";

type Vaccine = Readonly<{
    id: string;
    name: string;
    hasPatient: boolean;
}>;

interface VaccinesContextData {
    loading: boolean;
    vaccines: Vaccine[];
}

const VaccinesContext = createContext<VaccinesContextData | undefined>(
    undefined,
);

function VaccinesProvider({
  children,
}: Readonly<{
  children: React.ReactNode;
}>) {
  const [loading, setLoading] = useState<boolean>(false);
  const [vaccines, setVaccines] = useState<Vaccine[]>([]);

  const fetchVaccines = useCallback(async () => {
    setLoading(true);
    try {
      const response = await fetch("/apae-geral/api/vaccines");

      if (!response.ok) {
        throw Error("Ocorreu um erro ao carregar as vacinas.");
      }

      const data = await response.json();
      setVaccines(data);
    } finally {
      setLoading(false);
    }
  }, []);

  useEffect(() => {
    fetchVaccines();
  }, [fetchVaccines]);

  return (
    <VaccinesContext.Provider
      value={{
        loading,
        vaccines,
      }}
    >
      {children}
    </VaccinesContext.Provider>
  );
}

function useVaccinesContext() {
  const context = useContext(VaccinesContext);
  if (!context) {
    throw new Error(
      "useVaccinesContext must be used within a VaccinesProvider",
    );
  }
  return context;
}

export type {
  Vaccine,
};
export { useVaccinesContext, VaccinesProvider };
