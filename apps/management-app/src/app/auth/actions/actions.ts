"use server";

import { createAuthAPI } from "@/lib/axios";
import { removeSessionCookie, setSessionCookie } from "@/lib/cookies";
import { FormSignUp } from "@/schemas/authSchema";
import { AxiosError } from "axios";
import { redirect } from "next/navigation";

export async function login(formData: FormData) {
  const email = formData.get("email");
  const password = formData.get("password");

  if (!email || !password) {
    return { status: "error", message: "Email e senha são obrigatórios" };
  }

  try {
    const api = await createAuthAPI();
    const response = await api.post("signin", { email, password });

    if (!response.status || response.status !== 200) {
      throw new Error("Erro ao fazer login");
    }

    const payload = {
      accessToken: response.data.access_token,
      expiresIn: 60 * 1000,
    };

    setSessionCookie(payload);

    return { status: "success", message: "Login bem-sucedido" };
  } catch (error) {
    console.error("Erro ao fazer login: ", error);
    if (error instanceof Error && error.message.includes("401")) {
      return { status: "error", message: "Credenciais inválidas" };
    }

    return {
      status: "error",
      message: "Erro ao fazer login. Tente novamente.",
    };
  }
}

export async function logout() {
  removeSessionCookie();

  redirect("/auth/login");
}

export async function signUp(form: FormSignUp) {
  const { email, nomeCompleto, cpf, senha } = form;

  if (!email || !nomeCompleto || !cpf || !senha) {
    return { status: "error", message: "Todos os campos são obrigatórios" };
  }

  try {
    const api = await createAuthAPI();
    const response = await api.post("/signup", {
      fullName: nomeCompleto,
      email,
      cpf,
      password: senha,
    });
    console.log(response);

    if (response.status !== 201) {
      throw new Error(
        `Erro ao cadastrar usuário: ${
          response.data?.message || "Erro desconhecido"
        }`
      );
    }

    return { status: "success", message: "Cadastro bem-sucedido" };
  } catch (error: unknown) {
    console.error(error);

    if (error instanceof Error && error.message.includes("401")) {
      return {
        status: "error",
        message: "Não autorizado para realizar cadastro",
      };
    }

    return {
      status: "error",
      message: `Erro: ${
        error && (error as AxiosError).response?.data
          ? ((error as AxiosError).response?.data as { message?: string })
              .message
          : "Erro ao cadastrar usuário"
      }`,
    };
  }
}
