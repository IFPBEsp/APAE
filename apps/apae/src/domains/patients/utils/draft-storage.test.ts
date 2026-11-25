import { beforeEach, describe, expect, it } from "vitest";
import { clearForeignPatientDrafts, loadDraft, saveDraft } from "./draft-storage";

describe("patient draft ownership", () => {
  beforeEach(() => localStorage.clear());

  it("keeps the same user's draft and removes another user's draft", () => {
    saveDraft("apae_register_cache", "user-a", { name: "Rascunho A" });
    saveDraft("apae_edit_cache_1", "user-b", { name: "Rascunho B" });

    clearForeignPatientDrafts("user-a");

    expect(loadDraft("apae_register_cache", "user-a")).toEqual({ name: "Rascunho A" });
    expect(localStorage.getItem("apae_edit_cache_1")).toBeNull();
  });
});
