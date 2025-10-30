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
    createVaccine: (params: CreateVaccineParams) => Promise<void>;
    updateVaccine: (params: UpdateVaccineParams) => Promise<void>;
    deleteVaccine: (params: DeleteVaccineParams) => Promise<void>;
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
    const [feedback, setFeedback] = useState<Feedback>({
        message: "",
        success: false,
        error: false,
    });
    const [vaccines, setVaccines] = useState<Vaccine[]>([{
        id: "1",
        name: "teste"
    }]);

    const fetchVaccines = useCallback(async () => { }, []);

    const createVaccine = useCallback(
        async (params: CreateVaccineParams) => { },
        [],
    );

    const updateVaccine = useCallback(
        async (params: UpdateVaccineParams) => { },
        [],
    );

    const deleteVaccine = useCallback(
        async (params: DeleteVaccineParams) => { },
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
