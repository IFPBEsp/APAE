"use client";

import { Trash2 } from "lucide-react";
import { Button } from "@/components/ui/button";
import { deleteAgendamento } from "@/app/services/agendamentoService";
import { useRouter } from "next/navigation";

export default function TrashButton({ id }: { id: string }) {
  const router = useRouter();

  const deletarAgendamento = async () => {
    await deleteAgendamento(id);
    router.back();
  };

  return (
    <Button onClick={deletarAgendamento} className="bg-transparent cursor-pointer text-[#970D0D] active:text-[#0d4ffe] hover:bg-[rgba(0,0,0,0.1)] transition-colors">
      <Trash2 />
    </Button>
  );
}
