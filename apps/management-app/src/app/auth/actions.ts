"use server";

import { apiAuth } from "@/lib/axios";
import { FormSignUp } from "@/schemas/authSchema";
import { cookies } from "next/headers";
import { redirect } from "next/navigation";

export async function login(formData: FormData) {
  const email = formData.get("email");
  const password = formData.get("password");

  try {
    const response = await apiAuth.post("signin", { email, password });
    if (!response.status || response.status !== 200) {
      throw new Error("Erro ao fazer login");
    }

    const payload = {
      accessToken: response.data.access_token,
      expiresIn: 60 * 1000,
    };

    const cookieStore = await cookies();
    const expires = new Date(Date.now() + payload.expiresIn);
    cookieStore.set("session", JSON.stringify(payload), {
      expires,
      httpOnly: true,
      secure: process.env.NODE_ENV === "production",
      sameSite: "lax",
      path: "/",
    });
    return { status: "success", message: "Login bem-sucedido" };
  } catch (error) {
    console.error("Erro ao fazer login: ", error);
    return { status: "error", message: "Erro ao fazer login" };
  }
}

export async function logout() {
  const cookieStore = await cookies();
  cookieStore.delete("session");

  redirect("/login");
}

export async function signUp(form: FormSignUp) {
  const { email, nomeCompleto, cpf, senha } = form;
  try {
    const response = await apiAuth.post("signup", {
      fullname: nomeCompleto,
      email,
      cpf,
      password: senha,
    });
    if (!response.status || response.status !== 201) {
      throw new Error(`Erro ao cadastrar usuário: ${response.data.message}`);
    }
    return { status: "success", message: "Cadastro bem-sucedido" };
  } catch (error) {
    return {
      status: "error",
      message: `Erro: ${
        error instanceof Error ? error.message : "Erro ao cadastrar usuário"
      }`,
    };
  }
}
