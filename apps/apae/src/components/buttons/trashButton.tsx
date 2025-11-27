"use client";

import { Trash2 } from "lucide-react";
import { Button } from "@/components/ui/button";
import { deleteAgendamento } from "@/app/services/agendamentoService";
import { useRouter } from "next/navigation";
import {
  Dialog,
  DialogClose,
  DialogContent,
  DialogDescription,
  DialogFooter,
  DialogHeader,
  DialogTitle,
  DialogTrigger,
} from "../ui/dialog";

export default function TrashButton({ id, realizado }: { id: string, realizado: boolean }) {
  const router = useRouter();

  const deletarAgendamento = async () => {
    await deleteAgendamento(id, realizado);
    router.back();
  };

  return (
    <Dialog>
      <DialogTrigger asChild>
        <Button
          className="bg-transparent cursor-pointer text-[#970D0D] active:text-[#c21111] hover:bg-[rgba(0,0,0,0.1)] transition-colors"
        >
          <Trash2 />
        </Button>
      </DialogTrigger>
      <DialogContent className="w-full sm:max-w-[425px]">
        <DialogHeader>
          <DialogTitle>Confirmar exclusão</DialogTitle>
          <DialogDescription>
            Ao confirmar, o agendamento será exluído permanentemente. Deseja
            continuar?
          </DialogDescription>
        </DialogHeader>
        <DialogFooter>
          <DialogClose asChild>
            <Button variant="outline">Não</Button>
          </DialogClose>
          <Button onClick={deletarAgendamento} type="submit">
            Sim
          </Button>
        </DialogFooter>
      </DialogContent>
    </Dialog>
  );
}
