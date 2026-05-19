'use client';

import { Check } from 'lucide-react';
import { Button } from '@/components/ui/button';
import {
  markAsPerformed,
} from '@/app/services/appointmentService';
import { useRouter } from 'next/navigation';
import {
  Dialog,
  DialogClose,
  DialogContent,
  DialogDescription,
  DialogFooter,
  DialogHeader,
  DialogTitle,
  DialogTrigger,
} from '../ui/dialog';

export default function ConfirmaRealizacaoButton({ id }: { id: string }) {
  const router = useRouter();

  const confirmarRealizacaoDaConsulta = async () => {
    try {
      const agendamentoRealizado = await markAsPerformed(id);
      router.push(`/historico-consultas/${agendamentoRealizado.id}`);
    } catch (e) {
      console.error(e);
    }
  };

  return (
    <Dialog>
      <DialogTrigger asChild>
        <Button
          className={`bg-transparent cursor-pointer text-["#4bbd35"] active:text-["#58e03d"] hover:bg-[rgba(0,0,0,0.1)] transition-colors`}
        >
          <Check />
        </Button>
      </DialogTrigger>
      <DialogContent className="w-full sm:max-w-[425px]">
        <DialogHeader>
          <DialogTitle>Confirmar realização da consulta?</DialogTitle>
          <DialogDescription>
            Ao confirmar, a consulta será marcada como realizada. Deseja
            continuar?
          </DialogDescription>
        </DialogHeader>
        <DialogFooter>
          <DialogClose asChild>
            <Button variant="outline">Não</Button>
          </DialogClose>
          <Button onClick={confirmarRealizacaoDaConsulta} type="submit">
            Sim
          </Button>
        </DialogFooter>
      </DialogContent>
    </Dialog>
  );
}
