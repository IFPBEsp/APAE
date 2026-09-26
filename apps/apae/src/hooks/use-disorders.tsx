"use client";

import {
  createContext,
  useCallback,
  useContext,
  useEffect,
  useState,
} from "react";
import type {
  Disorder,
  CreateDisorderParams,
  UpdateDisorderParams,
  DeleteDisorderParams,
} from "@/domains/disorders/disorders.types";
import {
  fetchDisordersApi,
  fetchDisorderApi,
  createDisorderApi,
  updateDisorderApi,
  deleteDisorderApi,
} from "@/domains/disorders/disorders.api";

type FetchDisorderParams = Readonly<{
  id: string;
}>;

type Feedback = Readonly<{
  message: string;
  success: boolean;
  error: boolean;
}>;

interface DisordersContextData {
  loading: boolean;
  feedback: Feedback;
  disorders: Disorder[];
  fetchDisorders: () => Promise<void>;
  fetchDisorder: (params: FetchDisorderParams) => Promise<Disorder>;
  createDisorder: (params: CreateDisorderParams) => Promise<void>;
  updateDisorder: (params: UpdateDisorderParams) => Promise<void>;
  deleteDisorder: (params: DeleteDisorderParams) => Promise<void>;
}

const DisordersContext = createContext<DisordersContextData | undefined>(
  undefined,
);

type WithFeedbackMessages = {
  success: string;
};

function withFeedback<TArgs extends readonly unknown[], TReturn>(
  fn: (...args: TArgs) => Promise<TReturn>,
  setLoading: (loading: boolean) => void,
  setFeedback: (feedback: Feedback) => void,
  messages: WithFeedbackMessages,
) {
  return async (...args: TArgs): Promise<TReturn> => {
    setLoading(true);
    try {
      const result = await fn(...args);
      setFeedback({
        message: messages.success,
        success: true,
        error: false,
      });
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

function DisordersProvider({
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
  const [disorders, setDisorders] = useState<Disorder[]>([]);

  const fetchDisorders = useCallback(async () => {
    return withFeedback(
      async () => {
        const data = await fetchDisordersApi();
        setDisorders(data);
      },
      setLoading,
      setFeedback,
      {
        success: "Transtornos carregadas com sucesso.",
      },
    )();
  }, []);

  const fetchDisorder = useCallback(async (params: FetchDisorderParams) => {
    return withFeedback(
      async (currentParams: FetchDisorderParams) => {
        return fetchDisorderApi(currentParams.id);
      },
      setLoading,
      setFeedback,
      {
        success: "Transtorno carregada com sucesso.",
      },
    )(params);
  }, []);

  const createDisorder = useCallback(
    async (params: CreateDisorderParams) => {
      return withFeedback(
        async (currentParams: CreateDisorderParams): Promise<void> => {
          await createDisorderApi(currentParams);
          await fetchDisorders();
        },
        setLoading,
        setFeedback,
        { success: "Transtorno criada com sucesso." },
      )(params);
    },
    [fetchDisorders],
  );

  const updateDisorder = useCallback(
    async (params: UpdateDisorderParams) => {
      return withFeedback(
        async (currentParams: UpdateDisorderParams) => {
          await updateDisorderApi(currentParams);
          await fetchDisorders();
        },
        setLoading,
        setFeedback,
        {
          success: "Transtorno atualizado com sucesso.",
        },
      )(params);
    },
    [fetchDisorders],
  );

  const deleteDisorder = useCallback(
    async (params: DeleteDisorderParams) => {
      return withFeedback(
        async (currentParams: DeleteDisorderParams) => {
          await deleteDisorderApi(currentParams);
          await fetchDisorders();
        },
        setLoading,
        setFeedback,
        {
          success: "Transtorno excluído com sucesso.",
        },
      )(params);
    },
    [fetchDisorders],
  );

  useEffect(() => {
    fetchDisorders();
  }, [fetchDisorders]);

  return (
    <DisordersContext.Provider
      value={{
        loading,
        feedback,
        disorders,
        fetchDisorders,
        fetchDisorder,
        createDisorder,
        updateDisorder,
        deleteDisorder,
      }}
    >
      {children}
    </DisordersContext.Provider>
  );
}

function useDisordersContext() {
  const context = useContext(DisordersContext);
  if (!context) {
    throw new Error(
      "useDisordersContext must be used within a DisordersProvider",
    );
  }
  return context;
}

export type { Feedback };
export { useDisordersContext, DisordersProvider };
