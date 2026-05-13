"use client";

import { useEffect, useState } from "react";

type Care = {
  id: string;
  name: string;
};

function useCares() {
  const [cares, setCares] = useState<Care[]>([]);

  useEffect(() => {
    const fetching = async () => {
      const caresTypes = await fetch("/apae-geral/api/service-types", {
        method: "GET",
      });

      const data = await caresTypes.json();
      setCares(data);
    };

    fetching();
  }, []);

  return cares;
}

export default useCares;
