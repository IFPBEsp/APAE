import { Suspense } from "react";
import ResetPasswordClient from "@/domains/auth/reset-password/ResetPasswordClient";

export const dynamic = "force-dynamic";

export default function Page() {
  return (
    <Suspense fallback={<div>Carregando...</div>}>
      <ResetPasswordClient />
    </Suspense>
  );
}
