"use server";

import { cookies } from "next/headers";
import { redirect } from "next/navigation";

export async function login(formData: FormData) {
  const email = formData.get("email");
  const password = formData.get("password");

  const payload = {
    accessToken: "access-token",
    expiresIn: 7 * 60 * 60 * 24 * 1000, // 7 days in milliseconds
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

  redirect("/");
}

export async function logout() {
  const cookieStore = await cookies();
  cookieStore.delete("session");

  redirect("/login");
}
