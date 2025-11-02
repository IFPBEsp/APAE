"use client";

import { useState, useEffect } from "react";
import { format } from "date-fns";
import { ptBR } from "date-fns/locale";
import {
  CalendarDays,
  MessageCircleWarning,
  CalendarX,
  SearchIcon,
  Users,
} from "lucide-react";

import { Button } from "@/components/ui/button";
import { Card, CardContent } from "@/components/ui/card";
import {
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableHeader,
  TableRow,
} from "@/components/ui/table";
import { Calendar } from "@/components/ui/calendar";
import {
  Popover,
  PopoverContent,
  PopoverTrigger,
} from "@/components/ui/popover";

import { InfoCard } from "@/components/shared/InfoCard";
import { Input } from "@/components/ui/input";
import { Select, SelectItem } from "@/components/ui/select";
import {
  SelectContent,
  SelectGroup,
  SelectLabel,
  SelectTrigger,
  SelectValue,
} from "@/components/ui/select";
import {
  Appointment,
  getAppointments,
  getAreasDaSaude,
} from "../services/AppointmentService";
import { separaETransformaEmNumero } from "@/lib/utils";
import Link from "next/link";
import {
  Dialog,
  DialogContent,
  DialogDescription,
  DialogHeader,
  DialogTitle,
  DialogTrigger,
} from "@/components/ui/dialog";
import { AppointmentForm } from "@/components/forms/AppointmentForm";

type Area = {
  id: number;
  name: string;
};

export default function AllApointments() {
  const [selectedDate, setSelectedDate] = useState<Date | undefined>(undefined);
  const [selectedArea, setSelectedArea] = useState("");
  const [searchName, setSearchName] = useState("");
  const [areas, setAreas] = useState<Area[]>([]);
  const [appointments, setAppointments] = useState<Appointment[]>([]);

  useEffect(() => {
    const fetchAppointments = async () => {
      const response = await getAppointments();
      setAppointments(response);
      const areasExistentes: Area[] = (await getAreasDaSaude()).map(
        (area, index) => ({ id: index, name: area } as Area)
      );
      setAreas(areasExistentes);
    };
    fetchAppointments();
  }, []);

  const filteredAppointments = appointments.filter((appointment) => {
    const matchesArea = selectedArea
      ? appointment.profissional.areaDaSaude === selectedArea
      : true;
    const matchesPatientName = appointment.paciente.nome
      .toLowerCase()
      .includes(searchName.trim().toLowerCase());
    const matchesProfessionalName = appointment.profissional.nome
      .toLowerCase()
      .includes(searchName.trim().toLowerCase());
    const dateAppointment = separaETransformaEmNumero(
      appointment.proximaConsulta,
      "-"
    );
    const matchesDate = selectedDate
      ? new Date(
          dateAppointment[0],
          dateAppointment[1],
          dateAppointment[2]
        ).toDateString() === selectedDate.toDateString()
      : true;
    return (
      matchesArea &&
      (matchesPatientName || matchesProfessionalName) &&
      matchesDate
    );
  });

  const dataPassou = (data: string, horario: string) => {
    const [ano, mes, dia] = data.split("-");
    const [hora, minuto, segundo] = horario.split(":");
    const emDate = new Date(parseInt(ano), parseInt(mes), parseInt(dia), parseInt(hora), parseInt(minuto), parseInt(segundo));
    const agora = new Date();

    return agora > emDate;
  }

  const semJustificativa = appointments.filter(
    (appointment) => dataPassou(appointment.proximaConsulta, appointment.horaProximaConsulta) && !appointment.justificativa
  );

  const clearFilter = () => {
    setSelectedArea("");
    setSearchName("");
    setSelectedDate(undefined);
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
                  {selectedDate ? (
                    format(selectedDate, "dd 'de' MMMM 'de' yyyy", {
                      locale: ptBR,
                    })
                  ) : (
                    <span className="text-[#0D4F97]">Escolha uma data</span>
                  )}
                </Button>
              </PopoverTrigger>
              <PopoverContent className="w-auto p-0 bg-white">
                <Calendar
                  mode="single"
                  selected={selectedDate}
                  onSelect={setSelectedDate}
                  initialFocus
                  locale={ptBR}
                />
              </PopoverContent>
            </Popover>
            <Dialog>
              <DialogTrigger asChild>
                <Button className="w-full bg-[#0D4F97] text-white hover:bg-blue-900 text-xs sm:w-auto sm:text-sm">
                  Novo agendamento
                </Button>
              </DialogTrigger>
              <DialogContent className="w-full sm:max-w-[425px]">
                <DialogHeader>
                  <DialogTitle className="text-[#0D4F97]">Cadastrar Novo Agendamento</DialogTitle>
                  <DialogDescription className="text-[#0D4F97] opacity-50">
                    Preencha os detalhes abaixo para agendar uma consulta.
                  </DialogDescription>
                </DialogHeader>
                <AppointmentForm />
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
          <InfoCard
            title="Sem justificativa"
            icon={MessageCircleWarning}
            value={semJustificativa.length}
            iconColor="text-red-400"
            subtitle="Pacientes que não justificaram suas faltas"
            titleClassName="text-[#0D4F97]"
            valueClassName="text-[#0D4F97]"
          />
          <InfoCard
            title="Não confirmados"
            icon={CalendarX}
            value={
              appointments.filter((appointment) => !appointment.confirmado)
                .length
            }
            subtitle="Consultas que não foram confirmadas"
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
              onChange={(e) => setSearchName(e.target.value)}
            />
          </div>

          <Select onValueChange={setSelectedArea}>
            <SelectTrigger className="data-[placeholder]:text-[#0D4F97] border-[#0D4F97] hover:bg-accent">
              <SelectValue placeholder="Área da Saúde" />
            </SelectTrigger>
            <SelectContent>
              <SelectGroup>
                <SelectLabel>Áreas da saúde</SelectLabel>
                {areas.map((area) => (
                  <SelectItem key={area.id} value={area.name}>
                    {area.name}
                  </SelectItem>
                ))}
              </SelectGroup>
            </SelectContent>
          </Select>
          {(selectedArea || selectedDate || searchName) && (
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
                    Ações
                  </TableHead>
                </TableRow>
              </TableHeader>
              <TableBody>
                {filteredAppointments.map((item, index) => {
                  const dateAppointment = separaETransformaEmNumero(
                    item.proximaConsulta,
                    "-"
                  );
                  return (
                    <TableRow key={index}>
                      <TableCell className="px-3 py-2 text-xs sm:px-4 sm:py-3 sm:text-sm">
                        {item.paciente.nome}
                      </TableCell>
                      <TableCell className="px-3 py-2 text-xs sm:px-4 sm:py-3 sm:text-sm text-gray-800">
                        {format(
                          new Date(
                            dateAppointment[0],
                            dateAppointment[1],
                            dateAppointment[2]
                          ),
                          "dd 'de' MMMM 'de' yyyy",
                          { locale: ptBR }
                        )}
                      </TableCell>
                      <TableCell className="px-3 py-2 text-xs sm:px-4 sm:py-3 sm:text-sm">
                        {item.profissional.nome}
                      </TableCell>
                      <TableCell className="px-3 py-2">
                        <Link
                          href={`/agendamentos/${item.id}`}
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
