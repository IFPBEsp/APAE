export async function getAllProfissionais() {
  const response = await fetch("/api/professionals", { method: "GET" });
  return response;
}

export async function deleteProfissional(id: string) {
  const response = await fetch(`/api/professionals/${id}`, {
    method: "DELETE",
  });
  return response;
}

export async function createProfissional(formData: FormData) {
  const response = await fetch("/api/professionals", {
    method: "POST",
    body: formData,
  });

  return response;
}

export async function updateProfissional(id: string, data: any) {
  const response = await fetch(`/api/professionals/${id}`, {
    method: "PUT",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify(data),
  });

  if (!response.ok) {
    const errorData = await response.json().catch(() => null);
    const message = errorData?.message || `Erro HTTP ${response.status}`;

    const error = new Error(message);
    throw error;
  }

  return response;
}

export async function getProfissionalById(id: string) {
  const response = await fetch(`/api/professionals/${id}`, {
    method: "GET",
  });

  if (!response.ok) {
    throw new Error("Erro ao buscar profissional");
  }

  return response.json();
}