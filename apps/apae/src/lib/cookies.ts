"use server";

import { cookies } from "next/headers";

export async function getTokenFromCookie(): Promise<string | null> {
  try {
    const cookieStore = await cookies();

    const sessionCookie = cookieStore.get("session");

    if (sessionCookie?.value) {
      const sessionData = JSON.parse(sessionCookie.value);
      return sessionData.accessToken || null;
    }

    return null;
  } catch (error) {
    console.error("Erro ao obter token do cookie:", error);
    return null;
  }
}

export async function removeSessionCookie(): Promise<void> {
  try {
    const cookieStore = await cookies();
    cookieStore.delete("session");
  } catch (error) {
    console.error("Erro ao remover cookie de sessão:", error);
  }
}

export async function setSessionCookie(payload: { accessToken: string }): Promise<void> {
  try {
    const cookieStore = await cookies();

    cookieStore.set("session", JSON.stringify(payload), {
      httpOnly: true,
      secure: process.env.NODE_ENV === "production",
      sameSite: "lax",
      path: "/",
    });
  } catch (error) {
    console.error("Erro ao definir cookie de sessão:", error);
  }
}
