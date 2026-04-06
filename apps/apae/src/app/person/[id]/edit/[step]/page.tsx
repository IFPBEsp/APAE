"use client";

import { useParams, useRouter } from "next/navigation"; 
import { useEffect } from "react";
import { useMembersRegisterContext, MembersRegisterStep } from "../../../../../hooks/use-members-register-context";
import PersonalForm from "../../../register/personal/page";
import KinshipsForm from "../../../register/kinships/page";
import AddressForm from "../../../register/address/page";
import ResponsibleForm from "../../../register/responsible/page";
import ProfileForm from "../../../register/profile/page";

export default function EditPatientPage() {
  const { state: { step } } = useMembersRegisterContext();
  const router = useRouter(); 
  const { id, step: stepFromUrl } = useParams();

  useEffect(() => {
    if (step && step !== stepFromUrl) {
      router.push(`/person/${id}/edit/${step}`);
    }
  }, [step, id, router, stepFromUrl]);

  return (
    <div className="container mx-auto py-6">
      <div className="mb-6 px-4"><h1 className="text-3xl font-bold text-[#0D4F97]">Editar Paciente</h1></div>
      <div className="bg-white rounded-xl shadow-sm border p-4 md:p-8 min-h-[400px]">
        {step === MembersRegisterStep.PERSONAL && <PersonalForm />}
        {step === MembersRegisterStep.KINSHIPS && <KinshipsForm />}
        {step === MembersRegisterStep.ADDRESS && <AddressForm />}
        {step === MembersRegisterStep.GUARDIAN && <ResponsibleForm />}
        {step === MembersRegisterStep.PROFILE && <ProfileForm />}
      </div>
    </div>
  );
}