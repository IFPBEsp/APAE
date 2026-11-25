"use client";

import { removeSessionCookie } from "@/lib/cookies";
import { endSession as endDraftSession } from "@/domains/auth/session-id";
import { clearAllPatientDrafts } from "@/domains/patients/utils/draft-storage";

export async function endSession(options: { preserveDrafts?: boolean } = {}): Promise<void> {
  await removeSessionCookie();
  if (!options.preserveDrafts) clearAllPatientDrafts();
  endDraftSession();
  window.location.assign(options.preserveDrafts
    ? "/apae-geral/auth/login?sessionExpired=true"
    : "/apae-geral/auth/login");
}
