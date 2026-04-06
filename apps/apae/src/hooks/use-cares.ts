import { useState, useEffect } from "react";

function useCares() {
    const [cares, setCares] = useState<unknown[]>([]);

    useEffect(() => {
        const fetching = async () => {
                const caresTypes = await fetch(`/api/tipo-atendimento`, {
                    method: "GET",
                });

            const data = await caresTypes.json();

            setCares(data);
        }
        fetching()
    }, [])

    return cares;
}

export default useCares;
