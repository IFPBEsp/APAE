import { describe, expect, it } from "vitest";
import { draftNeedsFileWarning, getStepForBackendField, hasRequiredRegistrationFiles } from "./patient.utils";
import { MembersRegisterStep } from "../hooks/register-context/types";

describe("getStepForBackendField", () => {
  it.each(["reports", "referrals"])("maps %s to additional information", (field) => {
    expect(getStepForBackendField(field)).toBe(MembersRegisterStep.ADDITIONALS);
  });

  it("requires both medical documents before registration", () => {
    const additionals = {
      disability: { report: new File(["laudo"], "laudo.pdf"), types: [] },
      care: { referral: new File(["encaminhamento"], "encaminhamento.pdf"), types: [] },
    };
    expect(hasRequiredRegistrationFiles(additionals)).toBe(true);
    expect(hasRequiredRegistrationFiles({
      disability: { report: undefined, types: [] },
      care: { referral: undefined, types: [] },
    })).toBe(false);
  });

  it("warns when a restored draft has passed additional information", () => {
    expect(draftNeedsFileWarning(MembersRegisterStep.PROFILE)).toBe(true);
    expect(draftNeedsFileWarning(MembersRegisterStep.ADDITIONALS)).toBe(false);
    expect(draftNeedsFileWarning(MembersRegisterStep.GUARDIAN)).toBe(false);
  });
});
