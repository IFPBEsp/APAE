"use client";
import { toast } from "react-toastify";
import { Trash2 } from "lucide-react";
import { Button } from "@/components/ui/button";
import { deleteAppointment } from "@/app/services/appointmentService";
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

export default function TrashButton({
  id,
  hasHistory,
}: {
  id: string;
  realized: boolean;
  hasHistory: boolean;
}) {
  const router = useRouter();

  const excludeAppointment = async () => {
    await deleteAppointment(id);
    router.back();
  };

  if (hasHistory) {
    return (
      <Button
        className="bg-transparent cursor-not-allowed text-[#970D0D] opacity-50 hover:bg-[rgba(0,0,0,0.1)] transition-colors"
        onClick={() => toast.warning("Este agendamento possui histórico e não pode ser excluído.")}
      >
        <Trash2 />
      </Button>
    );
  }

  return (
    <Dialog>
      <DialogTrigger asChild>
        <Button className="bg-transparent cursor-pointer text-[#970D0D] active:text-[#c21111] hover:bg-[rgba(0,0,0,0.1)] transition-colors">
          <Trash2 />
        </Button>
      </DialogTrigger>
      <DialogContent className="w-full sm:max-w-[425px]">
        <DialogHeader>
          <DialogTitle>Confirmar exclusão</DialogTitle>
          <DialogDescription>
            Ao confirmar, o agendamento será exluído permanentemente. Deseja continuar?
          </DialogDescription>
        </DialogHeader>
        <DialogFooter>
          <DialogClose asChild>
            <Button variant="outline">Não</Button>
          </DialogClose>
          <Button onClick={excludeAppointment} type="submit">
            Sim
          </Button>
        </DialogFooter>
      </DialogContent>
    </Dialog>
  );
}
