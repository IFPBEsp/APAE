const KEY = "apae_session_id";

function generateId(): string {
  if (typeof crypto !== "undefined" && "randomUUID" in crypto) {
    return crypto.randomUUID();
  }
  return `${Date.now().toString(36)}-${Math.random().toString(36).slice(2)}`;
}

export function startSession(userId?: string): string {
  const id = userId || generateId();
  localStorage.setItem(KEY, id);
  return id;
}

export function getSessionId(): string {
  return localStorage.getItem(KEY) ?? startSession();
}

export function endSession() {
  localStorage.removeItem(KEY);
}

export function readSessionId(): string | null {
  return localStorage.getItem(KEY);
}
