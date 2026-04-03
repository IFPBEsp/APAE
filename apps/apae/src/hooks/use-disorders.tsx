"use client";

import {
  createContext,
  useCallback,
  useContext,
  useEffect,
  useState,
} from "react";

type Disorder = Readonly<{
  id: string;
  name: string;
  hasPatient: boolean;
}>;

type FetchDisorderParams = Readonly<{
  id: string;
}>;

type CreateDisorderParams = Readonly<{
  name: string;
}>;

type UpdateDisorderParams = Readonly<{
  id: string;
  name: string;
}>;

type DeleteDisorderParams = Readonly<{
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
  return async (...args: TArgs): Promise<TReturn | void> => {
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

  const fetchDisorders = useCallback(
    withFeedback(
      async () => {
        const response = await fetch("/api/transtornos");

        if (!response.ok) {
          throw Error("Ocorreu um erro ao carregar as transtornos.");
        }

        const data = await response.json();
        setDisorders(data);
      },
      setLoading,
      setFeedback,
      {
        success: "Transtornos carregadas com sucesso.",
      },
    ),
    [],
  );

  const fetchDisorder = useCallback(
    withFeedback(
      async (params: FetchDisorderParams) => {
        const response = await fetch(`/api/transtornos/${params.id}`);

        if (!response.ok) {
          throw Error("Ocorreu um erro ao carregar transtorno.");
        }

        return response.json();
      },
      setLoading,
      setFeedback,
      {
        success: "Transtorno carregada com sucesso.",
      },
    ),
    [],
  );

  const createDisorder = useCallback(
    withFeedback(
      async (params: CreateDisorderParams): Promise<void> => {
        const response = await fetch("/api/transtornos", {
          method: "POST",
          headers: { "Content-Type": "application/json" },
          body: JSON.stringify(params),
        });

        if (!response.ok) {
          throw new Error("Ocorreu um erro ao criar transtorno.");
        }

        fetchDisorders();
      },
      setLoading,
      setFeedback,
      { success: "Transtorno criada com sucesso." },
    ),
    [],
  );

  const updateDisorder = useCallback(
    withFeedback(
      async ({ id, ...data }: UpdateDisorderParams) => {
        const response = await fetch(`/api/transtornos/${id}`, {
          method: "PUT",
          headers: { "Content-Type": "application/json" },
          body: JSON.stringify(data),
        });

        if (!response.ok) {
          throw new Error("Ocorreu um erro ao atualizar transtorno.");
        }

        fetchDisorders();
      },
      setLoading,
      setFeedback,
      {
        success: "Transtorno atualizado com sucesso.",
      },
    ),
    [],
  );

  const deleteDisorder = useCallback(
    withFeedback(
      async (params: DeleteDisorderParams) => {
        const response = await fetch(`/api/transtornos/${params.id}`, {
          method: "DELETE",
        });

        if (!response.ok) {
          const errorMessage = await response.text();
          throw new Error(errorMessage || "Ocorreu um erro ao excluir transtorno.");
        }

        fetchDisorders();
      },
      setLoading,
      setFeedback,
      {
        success: "Transtorno excluído com sucesso.",
      },
    ),
    [],
  );

  useEffect(() => {
    fetchDisorders();
  }, []);

  return (
    <DisordersContext.Provider
      value={{
        loading,
        feedback,
        disorders,
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

export type {
  Disorder,
  Feedback,
  CreateDisorderParams,
  UpdateDisorderParams,
  DeleteDisorderParams,
};
export { useDisordersContext, DisordersProvider };
