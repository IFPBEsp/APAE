'use client';

import { Button } from '@/components/ui/button';
import { Calendar } from '@/components/ui/calendar';
import {
  Popover,
  PopoverContent,
  PopoverTrigger,
} from '@/components/ui/popover';
import {
  Dialog,
  DialogContent,
  DialogDescription,
  DialogHeader,
  DialogTitle,
  DialogTrigger,
} from '@/components/ui/dialog';
import { AppointmentForm } from '@/components/forms/AppointmentForm';
import { format } from 'date-fns';
import { ptBR } from 'date-fns/locale';
import { CalendarDays } from 'lucide-react';

interface DashboardHeaderProps {
  selectedDate: Date;
  setSelectedDate: (date: Date) => void;
  isCreateOpen: boolean;
  setIsCreateOpen: (open: boolean) => void;
}

export function DashboardHeader({
  selectedDate,
  setSelectedDate,
  isCreateOpen,
  setIsCreateOpen,
}: DashboardHeaderProps) {
  return (
    <div className="mb-4 flex flex-col justify-between gap-3 sm:mb-6 sm:flex-row sm:items-center">
      <h1 className="text-lg font-bold sm:text-2xl text-[#0D4F97]">
        Agendamentos do Dia
      </h1>
      <div className="flex flex-col gap-2 sm:flex-row sm:items-center">
        <Popover>
          <PopoverTrigger asChild>
            <Button
              variant="outline"
              className="w-full justify-start bg-white border-[#0D4F97] text-left text-[#0D4F97] font-normal text-xs sm:w-[220px] sm:text-sm"
            >
              <CalendarDays className="mr-2 h-4 w-4" />
              {selectedDate ? (
                format(selectedDate, "dd 'de' MMMM 'de' yyyy", { locale: ptBR })
              ) : (
                <span>Escolha uma data</span>
              )}
            </Button>
          </PopoverTrigger>
          <PopoverContent className="w-auto p-0 bg-white">
            <Calendar
              mode="single"
              selected={selectedDate}
              onSelect={(date) => date && setSelectedDate(date)}
              initialFocus
              locale={ptBR}
              required
            />
          </PopoverContent>
        </Popover>

        <Dialog open={isCreateOpen} onOpenChange={setIsCreateOpen}>
          <DialogTrigger asChild>
            <Button className="w-full bg-[#0D4F97] text-white hover:bg-blue-900 text-xs sm:w-auto sm:text-sm">
              Novo agendamento
            </Button>
          </DialogTrigger>
          <DialogContent className="w-full sm:max-w-[425px]">
            <DialogHeader>
              <DialogTitle className="text-[#0D4F97]">
                Cadastrar Novo Agendamento
              </DialogTitle>
              <DialogDescription className="text-[#0D4F97] opacity-50">
                Preencha os detalhes abaixo para agendar uma consulta.
              </DialogDescription>
            </DialogHeader>
            {isCreateOpen && <AppointmentForm />}
          </DialogContent>
        </Dialog>
      </div>
    </div>
  );
}