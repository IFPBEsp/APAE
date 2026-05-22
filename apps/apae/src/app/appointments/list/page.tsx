'use client';

import {
  CalendarDays,
  SearchIcon,
  Users,
  AlertTriangle // Import do Ícone
} from 'lucide-react';
import { useEffect, useState, useRef } from 'react';

import { Button } from '@/components/ui/button';
import { Calendar } from '@/components/ui/calendar';
import { Card, CardContent } from '@/components/ui/card';
import {
  Popover,
  PopoverContent,
  PopoverTrigger,
} from '@/components/ui/popover';
import {
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableHeader,
  TableRow,
} from '@/components/ui/table';
import { AppointmentForm } from '@/components/forms/AppointmentForm';
import { InfoCard } from '@/components/shared/InfoCard';
import {
  Dialog,
  DialogContent,
  DialogDescription,
  DialogHeader,
  DialogTitle,
  DialogTrigger,
} from '@/components/ui/dialog';
import { Input } from '@/components/ui/input';
import {
  Select, SelectContent,
  SelectGroup, SelectItem, SelectLabel,
  SelectTrigger,
  SelectValue
} from '@/components/ui/select';
import { formatDatePTBR } from '@/lib/utils';
import { ptBR } from 'date-fns/locale';
import Link from "next/link";
import {
  Appointment,
  getAppointments,
  getServiceAreas,
} from "../../services/appointmentService";
import {Tooltip, TooltipContent, TooltipProvider, TooltipTrigger} from "@radix-ui/react-tooltip";

type Area = {
  id: number;
  name: string;
};

export default function AllApointments() {
  const [selectedDate, setSelectedDate] = useState<Date | undefined>(undefined);
  const [selectedArea, setSelectedArea] = useState('');
  const [selectedStatus, setSelectedStatus] = useState('');
  const [searchName, setSearchName] = useState('');
  const [areas, setAreas] = useState<Area[]>([]);
  const [appointments, setAppointments] = useState<Appointment[]>([]);
  const [alertPatientIds, setAlertPatientIds] = useState<Set<string>>(new Set()); // ESTADO DO ALERTA
  const initialized = useRef(false);
  const [isCreateOpen, setIsCreateOpen] = useState(false);

useEffect(() => {
  if (appointments.length > 0) return;
  
  const fetchAppointments = async () => {

    if (initialized.current) return;
    try {
      initialized.current = true;

      const response = await getAppointments(undefined, undefined, 0, 100);

      setAppointments(response.content as Appointment[])

      const areasData = await getServiceAreas();
      const existingAreas: Area[] = areasData.map(
        (area, index) => ({ id: index, name: area })
      );
      setAreas(existingAreas);

      // Busca direta na API (lógica que está funcionando)
      const absencesResponse = await fetch('/apae-geral/api/patients/with-absences?minAbsences=3');
      
      if (!absencesResponse.ok) {
        throw new Error('Erro ao buscar pacientes com faltas');
      }

      const absencesData = await absencesResponse.json();
      const absencesList = absencesData.content || [];
      const idsSet = new Set<string>(absencesList.map((item: any) => item.patient.id));
      
      setAlertPatientIds(idsSet);

    } catch (error) {
      console.error(error);
      initialized.current = false;
    }
  };
  
  fetchAppointments();
}, [appointments]);

  const filteredAppointments = appointments.filter((appointment) => {
    const matchesDate = selectedDate
      ? formatDatePTBR(appointment.initialDate) ===
        formatDatePTBR(selectedDate.toString())
      : true;

    const search = searchName.toLowerCase();

    const matchesSearch =
      appointment.annualRegistration.patient.fullName
        .toLowerCase()
        .includes(search) ||
      appointment.professional.name
        .toLowerCase()
        .includes(search);

    const matchesArea = selectedArea
      ? appointment.professional.healthSector === selectedArea
        : true;

    const matchesStatus = selectedStatus
        ? selectedStatus === 'ativo'
            ? appointment.isActive === true
            : appointment.isActive === false
        : true;

    return matchesDate && matchesSearch && matchesArea && matchesStatus;
  });

  const clearFilter = () => {
    setSelectedArea('');
    setSearchName('');
    setSelectedDate(undefined);
    setSelectedStatus('');
  };

  const getTooltip = (item: Appointment) => {
    if (!item.isActive && item.replacedByDate) {
      return `Substituído por agendamento de ${formatDatePTBR(item.replacedByDate)}`;
    }
    if (item.isActive && item.updatedFromDate) {
      return `Atualizado a partir do agendamento de ${formatDatePTBR(item.updatedFromDate)}`;
    }
    return 'Agendamento ativo';
  };

  return (
    <div className="min-h-screen w-full text-sm overflow-x-hidden">
      <main className="flex-1 p-3 sm:p-6 w-full max-w-none">
        <div className="mb-4 flex flex-col justify-between gap-3 sm:mb-6 sm:flex-row sm:items-center">
          <h1 className="text-lg font-bold sm:text-2xl text-[#0D4F97]">
            Todos os Agendamentos
          </h1>
          <div className="flex flex-col gap-2 sm:flex-row sm:items-center">
            <Popover>
              <PopoverTrigger asChild>
                <Button
                  variant="outline"
                  className="w-full justify-start bg-white text-left font-normal text-xs sm:w-[220px] sm:text-sm border-[#0D4F97]"
                >
                  <CalendarDays className="mr-2 h-4 w-4 text-[#0D4F97]" />
                  {selectedDate ? (formatDatePTBR(selectedDate.toString())) : (<span className="text-[#0D4F97]">Escolha uma data</span>)}
                </Button>
              </PopoverTrigger>
              <PopoverContent className="w-auto p-0 bg-white">
                <Calendar
                  mode="single"
                  selected={selectedDate}
                  onSelect={setSelectedDate}
                  locale={ptBR}
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

        <div className="mb-4 grid grid-cols-1 gap-3 sm:grid-cols-2 lg:grid-cols-4">
          <InfoCard
            title="Todos os agendamentos"
            icon={Users}
            value={appointments.length}
            titleClassName="text-[#0D4F97]"
            valueClassName="text-[#0D4F97]"
          />
        </div>

        <div className="flex items-center gap-2 mb-4">
          <div className="relative w-full">
            <SearchIcon className="absolute left-3 top-1/2 h-4 w-4 -translate-y-1/2 text-muted-foreground" />
            <Input
              placeholder="Buscar paciente ou profissional..."
              className="pl-10 pr-3 border-[#0D4F97]"
              value={searchName}
              onChange={e => {
                setSearchName(e.target.value)
              }}
            />
          </div>

          <Select value={selectedArea} onValueChange={setSelectedArea}>
            <SelectTrigger className="data-[placeholder]:text-[#0D4F97] border-[#0D4F97] hover:bg-accent">
              <SelectValue placeholder="Área da Saúde" />
            </SelectTrigger>
            <SelectContent>
              <SelectGroup>
                <SelectLabel>Áreas da saúde</SelectLabel>
                {areas.map(area => (
                  <SelectItem key={area.id} value={area.name}>
                    {area.name}
                  </SelectItem>
                ))}
              </SelectGroup>
            </SelectContent>
          </Select>

          <Select value={selectedStatus} onValueChange={setSelectedStatus}>
            <SelectTrigger className="data-[placeholder]:text-[#0D4F97] border-[#0D4F97] hover:bg-accent">
              <SelectValue placeholder="Status" />
            </SelectTrigger>
            <SelectContent>
              <SelectGroup>
                <SelectLabel>Status</SelectLabel>
                <SelectItem value="ativo">Ativo</SelectItem>
                <SelectItem value="inativo">Inativo</SelectItem>
              </SelectGroup>
            </SelectContent>
          </Select>
          {(selectedArea || selectedDate || searchName || selectedStatus) && (
            <Button
              variant="outline"
              onClick={clearFilter}
              className="w-auto text-xs sm:text-sm text-red-600 hover:bg-red-50 hover:text-red-600 border-red-600"
            >
              Limpar Filtro
            </Button>
          )}
        </div>

        <Card>
          <CardContent className="p-0">
            <Table>
              <TableHeader>
                <TableRow>
                  <TableHead className="px-3 py-2 text-xs sm:px-4 sm:py-3 sm:text-sm text-[#0D4F97]">
                    Paciente
                  </TableHead>
                  <TableHead className="px-3 py-2 text-xs sm:px-4 sm:py-3 sm:text-sm text-[#0D4F97]">
                    Próxima Consulta
                  </TableHead>
                  <TableHead className="px-3 py-2 text-xs sm:px-4 sm:py-3 sm:text-sm text-[#0D4F97]">
                    Profissional
                  </TableHead>
                  <TableHead className="px-3 py-2 text-xs sm:px-4 sm:py-3 sm:text-sm text-[#0D4F97]">
                    Status
                  </TableHead>
                  <TableHead className="px-3 py-2 text-xs sm:px-4 sm:py-3 sm:text-sm text-[#0D4F97]">
                    Ações
                  </TableHead>
                </TableRow>
              </TableHeader>
              <TableBody>
                {filteredAppointments.map((item, index) => {
                  return (
                    <TableRow key={index} className={!item.isActive ? 'text-gray-400' : ''}>
                      <TableCell className="px-3 py-2 text-xs sm:px-4 sm:py-3 sm:text-sm">
                        <div className="flex items-center gap-2">
                          <span className="truncate">{item.annualRegistration.patient.fullName}</span>
                          
                          {/* CRUZA O ID DO PACIENTE DA TABELA COM O SET DE FALTOSOS */}
                          {alertPatientIds.has(item.annualRegistration.patient.id) && (
                            <TooltipProvider>
                              <Tooltip>
                                <TooltipTrigger asChild>
                                  <AlertTriangle className="h-4 w-4 text-amber-500 shrink-0 cursor-help" />
                                </TooltipTrigger>
                                <TooltipContent>
                                  <p>Paciente com 3+ faltas não justificadas</p>
                                </TooltipContent>
                              </Tooltip>
                            </TooltipProvider>
                          )}
                        </div>
                      </TableCell>
                      <TableCell className={`px-3 py-2 text-xs sm:px-4 sm:py-3 sm:text-sm ${!item.isActive ? 'text-gray-400' : 'text-gray-800'}`}>
                        {formatDatePTBR(item.initialDate)}
                      </TableCell>
                      <TableCell className="px-3 py-2 text-xs sm:px-4 sm:py-3 sm:text-sm">
                        {item.professional.name}
                      </TableCell>
                      <TableCell>
                        <div className="flex items-center gap-1">
                            <span className={item.isActive ? 'text-green-600 font-medium' : 'text-red-400 font-medium'}>
                              {item.isActive ? 'Ativo' : 'Inativo'}
                            </span>
                          <TooltipProvider>
                            <Tooltip>
                              <TooltipTrigger asChild>
                                <span className="cursor-help text-gray-400 text-xs">ⓘ</span>
                              </TooltipTrigger>
                              <TooltipContent className="bg-white text-gray-800 shadow-lg border border-gray-200 rounded-lg p-3 max-w-[280px] break-words">
                                <div className="flex items-center gap-2 mb-2">
                                  <div className="h-7 w-7 rounded-full bg-[#0D4F97] flex items-center justify-center text-white text-xs font-bold">
                                    {item.professional.name.charAt(0)}
                                  </div>
                                  <div className="flex flex-col">
                                    <span className="text-xs font-semibold text-gray-800">{item.professional.name}</span>
                                    <span className="text-[10px] text-gray-400">{formatDatePTBR(item.initialDate)}</span>
                                  </div>
                                </div>
                                <p className="text-xs text-gray-600 break-words whitespace-normal">{getTooltip(item)}</p>
                              </TooltipContent>
                            </Tooltip>
                          </TooltipProvider>
                        </div>
                      </TableCell>
                      <TableCell className="px-3 py-2">
                        <Link
                          href={`/appointments/${item.id}`}
                          className="cursor-pointer text-xs text-blue-800 underline hover:underline sm:text-sm"
                        >
                          Detalhes
                        </Link>
                      </TableCell>
                    </TableRow>
                  );
                })}
              </TableBody>
            </Table>
          </CardContent>
        </Card>
      </main>
    </div>
  );
}