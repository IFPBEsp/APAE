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

const VaccinesContext = createContext<VaccinesContextData | undefined>(
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

    const fetchVaccines = useCallback(
        withFeedback(
            async () => {
                const response = await fetch("/api/vacinas");

                if (!response.ok) {
                    throw Error("Ocorreu um erro ao carregar as vacinas.");
                }

                const data = await response.json();
                setVaccines(data);
            },
            setLoading,
            setFeedback,
            {
                success: "Vacinas carregadas com sucesso.",
            },
        ),
        [],
    );

    const fetchVaccine = useCallback(
        withFeedback(
            async (params: FetchVaccineParams) => {
                const response = await fetch(`/api/vacinas/${params.id}`);

                console.log("TESTE");
                if (!response.ok) {
                    throw Error("Ocorreu um erro ao carregar vacina.");
                }

                return response.json();
            },
            setLoading,
            setFeedback,
            {
                success: "Vacina carregada com sucesso.",
            },
        ),
        [],
    );

    const createVaccine = useCallback(
        withFeedback(
            async (params: CreateVaccineParams): Promise<void> => {
                const response = await fetch("/api/vacinas", {
                    method: "POST",
                    headers: { "Content-Type": "application/json" },
                    body: JSON.stringify(params),
                });

                if (!response.ok) {
                    throw new Error("Ocorreu um erro ao criar vacina.");
                }

                fetchVaccines();
            },
            setLoading,
            setFeedback,
            { success: "Vacina criada com sucesso." },
        ),
        [],
    );

    const updateVaccine = useCallback(
        withFeedback(
            async ({ id, ...data }: UpdateVaccineParams) => {
                const response = await fetch(`/api/vacinas/${id}`, {
                    method: "PUT",
                    headers: { "Content-Type": "application/json" },
                    body: JSON.stringify(data),
                });

                if (!response.ok) {
                    throw new Error("Ocorreu um erro ao atualizar vacina.");
                }

                fetchVaccines();
            },
            setLoading,
            setFeedback,
            {
                success: "Vacina atualizada com sucesso.",
            },
        ),
        [],
    );

    const deleteVaccine = useCallback(
        withFeedback(
            async (params: DeleteVaccineParams) => {
                const response = await fetch(`/api/vacinas/${params.id}`, {
                    method: "DELETE",
                });

                if (!response.ok) {
                    const errorData = await response.json().catch(() => null);
                    throw new Error(errorData?.message || "Ocorreu um erro ao excluir a vacina.");
                }

                fetchVaccines();
            },
            setLoading,
            setFeedback,
            {
                success: "Vacina excluída com sucesso.",
            },
        ),
        [],
    );

    useEffect(() => {
        fetchVaccines();
    }, []);

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
