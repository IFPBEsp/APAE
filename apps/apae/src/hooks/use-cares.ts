import { useState } from "react";
import { useEffect } from "react";

function fetchCares() {
    const [cares, setCares] = useState([]);

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

export default fetchCares;