"use client";

import { removeSessionCookie } from "./cookies";

export async function endSession(): Promise<void> {
  await removeSessionCookie();
  window.location.assign("/apae-geral/auth/login");
}