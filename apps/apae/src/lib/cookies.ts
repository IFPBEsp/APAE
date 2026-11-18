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

export async function getRoleFromCookie(): Promise<string | null> {
  try {
    const cookieStore = await cookies();

    const sessionCookie = cookieStore.get("session");

    if (!sessionCookie?.value) {
      return null;
    }

    const { accessToken } = JSON.parse(sessionCookie.value);
    const base64Url = accessToken.split(".")[1];
    const base64 = base64Url.replace(/-/g, "+").replace(/_/g, "/");
    const padded = base64 + "=".repeat((4 - (base64.length % 4)) % 4);
    const payload = JSON.parse(atob(padded));

    return typeof payload.role === "string" ? payload.role : null;
  } catch {
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

export async function setSessionCookie(payload: {
  accessToken: string;
}, expiresAtSec?: number): Promise<void> {
  try {
    const cookieStore = await cookies();

    const options: {
      httpOnly: boolean;
      secure: boolean;
      sameSite: "lax";
      path: string;
      maxAge?: number;
      expires?: Date;
    } = {
      httpOnly: true,
      secure: process.env.NODE_ENV === "production",
      sameSite: "lax",
      path: "/",
    };

    if (expiresAtSec) {
      const now = Math.floor(Date.now() / 1000);
      const maxAge = expiresAtSec - now;
      if (maxAge > 0) {
        options.maxAge = maxAge;
        options.expires = new Date(expiresAtSec * 1000);
      }
    }

    cookieStore.set("session", JSON.stringify(payload), options);
  } catch (error) {
    console.error("Erro ao definir cookie de sessão:", error);
  }
}
