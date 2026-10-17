const KEY = "apae_session_id";

function generateId(): string {
  if (typeof crypto !== "undefined" && "randomUUID" in crypto) {
    return crypto.randomUUID();
  }
  return `${Date.now().toString(36)}-${Math.random().toString(36).slice(2)}`;
}

export function startSession(): string {
  const id = generateId();
  localStorage.setItem(KEY, id);
  return id;
}

export function getSessionId(): string {
  return localStorage.getItem(KEY) ?? startSession();
}

export function endSession() {
  localStorage.removeItem(KEY);
}
