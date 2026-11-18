"use client";

import {
  createContext,
  useCallback,
  useContext,
  useEffect,
  useState,
} from "react";

import { bffFetchJson } from "@/lib/bff-client";
import type { Vaccine, CreateVaccineParams, UpdateVaccineParams, DeleteVaccineParams } from "@/domains/vaccines/vaccines.types";

type FetchVaccineParams = Readonly<{
    id: string;
}>;

type Feedback = Readonly<{
  message: string;
  success: boolean;
  error: boolean;
}>;

interface VaccinesContextData {
    loading: boolean;
    fetchVaccines: () => Promise<void>;
    feedback: Feedback;
    vaccines: Vaccine[];
    fetchVaccine: (params: FetchVaccineParams) => Promise<Vaccine>;
    createVaccine: (params: CreateVaccineParams) => Promise<void>;
    updateVaccine: (params: UpdateVaccineParams) => Promise<void>;
    deleteVaccine: (params: DeleteVaccineParams) => Promise<void>;
}

const VaccinesContext = createContext<VaccinesContextData | undefined>(
    undefined,
);

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
      const message =
        error instanceof Error ? error.message : "Erro desconhecido.";
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
        const data = await bffFetchJson<Vaccine[]>("/apae-geral/api/vaccines");
        setVaccines(data);
      },
      setLoading,
      setFeedback,
    )();
  }, []);

  const fetchVaccine = useCallback(async (params: FetchVaccineParams) => {
    return withFeedback(
      async (currentParams: FetchVaccineParams) => {
        return bffFetchJson<Vaccine>(`/apae-geral/api/vaccines/${currentParams.id}`);
      },
      setLoading,
      setFeedback,
    )(params);
  }, []);

  const createVaccine = useCallback(
    async (params: CreateVaccineParams) => {
      return withFeedback(
        async (currentParams: CreateVaccineParams): Promise<void> => {
          await bffFetchJson("/apae-geral/api/vaccines", {
            method: "POST",
            body: JSON.stringify(currentParams),
          });

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
          await bffFetchJson(`/apae-geral/api/vaccines/${id}`, {
            method: "PUT",
            body: JSON.stringify(data),
          });

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
          await bffFetchJson(`/apae-geral/api/vaccines/${currentParams.id}`, {
            method: "DELETE",
          });

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
        fetchVaccines,
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
    throw new Error(
      "useVaccinesContext must be used within a VaccinesProvider",
    );
  }
  return context;
}

export type {
  Vaccine,
  Feedback,
  CreateVaccineParams,
  UpdateVaccineParams,
  DeleteVaccineParams,
};
export { useVaccinesContext, VaccinesProvider };