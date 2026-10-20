import { Suspense } from "react";
import { VaccinesList } from "@/domains/vaccines/list/vaccines-list";

export default function VaccinesPage() {
  return (
    <Suspense
      fallback={
        <div className="min-h-screen bg-slate-100 flex items-center justify-center">
          Carregando...
        </div>
      }
    >
      <VaccinesList />
    </Suspense>
  );
}
