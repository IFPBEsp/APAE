"use client";

import { removeSessionCookie } from "@/lib/cookies";
import { endSession as endDraftSession } from "@/domains/auth/session-id";
import { clearAllPatientDrafts } from "@/domains/patients/utils/draft-storage";

export async function endSession(): Promise<void> {
  await removeSessionCookie();
  clearAllPatientDrafts();
  endDraftSession();
  window.location.assign("/apae-geral/auth/login");
}
