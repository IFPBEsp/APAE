"use client";
import { useParams, useRouter } from "next/navigation"; 
import { useEffect } from "react";
import { useMembersRegisterContext, MembersRegisterStep } from "../../../../../hooks/use-members-register-context";
import PersonalForm from "../../../create/personal/page";
import KinshipsForm from "../../../create/kinships/page";
import AddressForm from "../../../create/address/page";
import ResponsibleForm from "../../../create/responsible/page";
import ProfileForm from "../../../create/profile/page";
import AdditionalsForm from "../../../create/additional/page"; 

export default function EditPatientPage() {
  const { state: { step } } = useMembersRegisterContext();
  const router = useRouter(); 
  const { id, step: stepFromUrl } = useParams();

  useEffect(() => {
    if (step && step !== stepFromUrl) {
      router.push(`/patients/${id}/edit/${step}`);
    }
  }, [step, id, router, stepFromUrl]);

  return (
    <div className="w-full h-full max-w-7xl mx-auto py-2">
      <>
        {step === MembersRegisterStep.PERSONAL && <PersonalForm />}
        {step === MembersRegisterStep.KINSHIPS && <KinshipsForm />}
        {step === MembersRegisterStep.ADDRESS && <AddressForm />}
        {step === MembersRegisterStep.ADDITIONALS && <AdditionalsForm />} 
        {step === MembersRegisterStep.GUARDIAN && <ResponsibleForm />}
        {step === MembersRegisterStep.PROFILE && <ProfileForm />}
      </>
    </div>
  );
}