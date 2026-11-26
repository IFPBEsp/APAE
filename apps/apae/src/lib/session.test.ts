import { afterEach, beforeEach, describe, expect, it, vi } from "vitest";
import { endSession } from "./session";
import { removeSessionCookie } from "./cookies";
import { endSession as endDraftSession } from "@/domains/auth/session-id";
import { clearAllPatientDrafts } from "@/domains/patients/utils/draft-storage";

vi.mock("./cookies", () => ({ removeSessionCookie: vi.fn() }));
vi.mock("@/domains/auth/session-id", () => ({ endSession: vi.fn() }));
vi.mock("@/domains/patients/utils/draft-storage", () => ({ clearAllPatientDrafts: vi.fn() }));

describe("endSession", () => {
  const assign = vi.fn();

  beforeEach(() => {
    assign.mockClear();
    vi.stubGlobal("window", { location: { assign } });
    vi.clearAllMocks();
  });

  afterEach(() => {
    vi.unstubAllGlobals();
    vi.restoreAllMocks();
  });

  it("clears drafts on user logout", async () => {
    await endSession();

    expect(clearAllPatientDrafts).toHaveBeenCalledOnce();
    expect(endDraftSession).toHaveBeenCalledOnce();
    expect(assign).toHaveBeenCalledWith("/apae-geral/auth/login");
    expect(removeSessionCookie).toHaveBeenCalledOnce();
  });

  it("preserves drafts when the session expires", async () => {
    await endSession({ preserveDrafts: true });

    expect(clearAllPatientDrafts).not.toHaveBeenCalled();
    expect(endDraftSession).toHaveBeenCalledOnce();
    expect(assign).toHaveBeenCalledWith("/apae-geral/auth/login?sessionExpired=true");
    expect(removeSessionCookie).toHaveBeenCalledOnce();
  });
});
