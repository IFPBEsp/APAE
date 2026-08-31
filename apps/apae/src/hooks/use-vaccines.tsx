"use client";

import { createContext, useCallback, useContext, useEffect, useState } from "react";

type Vaccine = Readonly<{
  id: string;
  name: string;
  hasPatient: boolean;
}>;

type FetchVaccineParams = Readonly<{
  id: string;
}>;

type CreateVaccineParams = Readonly<{
  name: string;
}>;

type UpdateVaccineParams = Readonly<{
  id: string;
  name: string;
}>;

type DeleteVaccineParams = Readonly<{
  id: string;
}>;

type Feedback = Readonly<{
  message: string;
  success: boolean;
  error: boolean;
}>;

interface VaccinesContextData {
  loading: boolean;
  feedback: Feedback;
  vaccines: Vaccine[];
  fetchVaccine: (params: FetchVaccineParams) => Promise<Vaccine>;
  createVaccine: (params: CreateVaccineParams) => Promise<void>;
  updateVaccine: (params: UpdateVaccineParams) => Promise<void>;
  deleteVaccine: (params: DeleteVaccineParams) => Promise<void>;
}

const VaccinesContext = createContext<VaccinesContextData | undefined>(undefined);

type WithFeedbackMessages = {
  success?: string;
};

function withFeedback<TArgs extends readonly unknown[], TReturn>(
  fn: (...args: TArgs) => Promise<TReturn>,
  setLoading: (loading: boolean) => void,
  setFeedback: (feedback: Feedback) => void,
  messages?: WithFeedbackMessages,
) {
  return async (...args: TArgs): Promise<TReturn> => {
    setLoading(true);
    try {
      const result = await fn(...args);

      if (messages?.success) {
        setFeedback({
          message: messages.success,
          success: true,
          error: false,
        });
      } else {
        setFeedback({ message: "", success: false, error: false });
      }

      return result;
    } catch (error) {
      const message = error instanceof Error ? error.message : "Erro desconhecido.";
      setFeedback({
        message,
        success: false,
        error: true,
      });
      throw error;
    } finally {
      setLoading(false);
    }
  };
}

function VaccinesProvider({
  children,
}: Readonly<{
  children: React.ReactNode;
}>) {
  const [loading, setLoading] = useState<boolean>(false);
  const [feedback, setFeedback] = useState<Feedback>({
    message: "",
    success: false,
    error: false,
  });
  const [vaccines, setVaccines] = useState<Vaccine[]>([]);

  const fetchVaccines = useCallback(async () => {
    return withFeedback(
      async () => {
        const response = await fetch("/apae-geral/api/vaccines");

        if (!response.ok) {
          throw Error("Ocorreu um erro ao carregar as vacinas.");
        }

        const data = await response.json();
        setVaccines(data);
      },
      setLoading,
      setFeedback,
    )();
  }, []);

  const fetchVaccine = useCallback(async (params: FetchVaccineParams) => {
    return withFeedback(
      async (currentParams: FetchVaccineParams) => {
        const response = await fetch(`/apae-geral/api/vaccines/${currentParams.id}`);

        if (!response.ok) {
          throw Error("Ocorreu um erro ao carregar vacina.");
        }

        return response.json();
      },
      setLoading,
      setFeedback,
    )(params);
  }, []);

  const createVaccine = useCallback(
    async (params: CreateVaccineParams) => {
      return withFeedback(
        async (currentParams: CreateVaccineParams): Promise<void> => {
          const response = await fetch("/apae-geral/api/vaccines", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(currentParams),
          });

          if (!response.ok) {
            throw new Error("Ocorreu um erro ao criar vacina.");
          }

          await fetchVaccines();
        },
        setLoading,
        setFeedback,
        { success: "Vacina criada com sucesso." },
      )(params);
    },
    [fetchVaccines],
  );

  const updateVaccine = useCallback(
    async (params: UpdateVaccineParams) => {
      return withFeedback(
        async ({ id, ...data }: UpdateVaccineParams) => {
          const response = await fetch(`/apae-geral/api/vaccines/${id}`, {
            method: "PUT",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(data),
          });

          if (!response.ok) {
            throw new Error("Ocorreu um erro ao atualizar vacina.");
          }

          await fetchVaccines();
        },
        setLoading,
        setFeedback,
        {
          success: "Vacina atualizada com sucesso.",
        },
      )(params);
    },
    [fetchVaccines],
  );

  const deleteVaccine = useCallback(
    async (params: DeleteVaccineParams) => {
      return withFeedback(
        async (currentParams: DeleteVaccineParams) => {
          const response = await fetch(`/apae-geral/api/vaccines/${currentParams.id}`, {
            method: "DELETE",
          });

          if (!response.ok) {
            const errorData = await response.json().catch(() => null);
            throw new Error(errorData?.message || "Ocorreu um erro ao excluir a vacina.");
          }

          await fetchVaccines();
        },
        setLoading,
        setFeedback,
        {
          success: "Vacina excluída com sucesso.",
        },
      )(params);
    },
    [fetchVaccines],
  );

  useEffect(() => {
    fetchVaccines();
  }, [fetchVaccines]);

  return (
    <VaccinesContext.Provider
      value={{
        loading,
        feedback,
        vaccines,
        fetchVaccine,
        createVaccine,
        updateVaccine,
        deleteVaccine,
      }}
    >
      {children}
    </VaccinesContext.Provider>
  );
}

function useVaccinesContext() {
  const context = useContext(VaccinesContext);
  if (!context) {
    throw new Error("useVaccinesContext must be used within a VaccinesProvider");
  }
  return context;
}

export type { Vaccine, Feedback, CreateVaccineParams, UpdateVaccineParams, DeleteVaccineParams };
export { useVaccinesContext, VaccinesProvider };
