'use client';

import { useState } from 'react';
import { Button } from '@/components/ui/button';
import {
  Dialog,
  DialogTrigger,
  DialogContent,
  DialogHeader,
  DialogTitle,
  DialogDescription,
} from '@/components/ui/dialog';
import { Label } from '@/components/ui/label';
import { Textarea } from '@/components/ui/textarea';
import { DialogClose } from '@radix-ui/react-dialog';

export function RegistrarFaltaButton() {
  const [motivo, setMotivo] = useState('');

  const handleConfirm = () => {
    console.log('Motivo da falta:', motivo);
  };

  return (
    <Dialog>
      <DialogTrigger asChild>
        <Button className="bg-[#0D4F97] hover:bg-[#0b417f] text-white font-semibold px-4 py-2 rounded-md w-fit">
          Registrar falta
        </Button>
      </DialogTrigger>

      <DialogContent className="w-full sm:max-w-[425px]">
        <DialogHeader>
          <DialogTitle>Registrar falta</DialogTitle>
          <DialogDescription>
            Descreva o motivo da falta e confirme o registro.
          </DialogDescription>
        </DialogHeader>

        {/* Campo de texto para o motivo */}
        <div className="mt-4">
          <Label htmlFor="motivo" className="font-medium">
            Motivo da falta
          </Label>
          <Textarea
            id="motivo"
            placeholder="Digite o motivo da falta..."
            value={motivo}
            onChange={e => setMotivo(e.target.value)}
            className="mt-2"
          />
        </div>

        {/* Botões de ação */}
        <div className="flex justify-end gap-3 mt-6">
          <DialogClose asChild>
            <Button
              variant="outline"
              className="border-[#0D4F97] text-[#0D4F97] hover:bg-[#E6F0FA]"
            >
              Cancelar
            </Button>
          </DialogClose>

          {/* CONFIRMAR */}
          <Button
            className="bg-[#0D4F97] hover:bg-[#0b417f] text-white font-semibold"
            onClick={handleConfirm}
          >
            Confirmar
          </Button>
        </div>
      </DialogContent>
    </Dialog>
  );
}
