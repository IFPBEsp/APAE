const REGISTER_KEY = "apae_register_cache";
const EDIT_PREFIX = "apae_edit_cache_";

export const registerDraftKey = () => REGISTER_KEY;
export const editDraftKey = (patientId: string | number) =>
  `${EDIT_PREFIX}${patientId}`;

type StoredDraft<T> = { ownerId: string; data: T };

const canUseStorage = () => typeof window !== "undefined";

export function saveDraft<T>(key: string, ownerId: string, data: T) {
  if (!canUseStorage()) return;
  try {
    const payload: StoredDraft<T> = { ownerId, data };
    localStorage.setItem(key, JSON.stringify(payload));
  } catch (error) {
    console.warn("Não foi possível salvar o rascunho:", error);
  }
}

export function loadDraft<T>(key: string, ownerId: string): T | null {
  if (!canUseStorage()) return null;
  try {
    const raw = localStorage.getItem(key);
    if (!raw) return null;
    const parsed = JSON.parse(raw) as Partial<StoredDraft<T>>;
    if (!parsed || parsed.ownerId !== ownerId || !parsed.data) {
      localStorage.removeItem(key);
      return null;
    }
    return parsed.data;
  } catch {
    localStorage.removeItem(key);
    return null;
  }
}

function patientDraftKeys(): string[] {
  const keys: string[] = [];
  for (let i = 0; i < localStorage.length; i++) {
    const k = localStorage.key(i);
    if (k === REGISTER_KEY || k?.startsWith(EDIT_PREFIX)) keys.push(k);
  }
  return keys;
}

export function clearAllPatientDrafts() {
  if (!canUseStorage()) return;
  patientDraftKeys().forEach((k) => localStorage.removeItem(k));
}

export function purgeForeignDrafts(currentUserId: string) {
  if (!canUseStorage()) return;
  for (const key of patientDraftKeys()) {
    try {
      const parsed = JSON.parse(localStorage.getItem(key) ?? "null");
      if (parsed?.ownerId !== currentUserId) localStorage.removeItem(key);
    } catch {
      localStorage.removeItem(key);
    }
  }
}
