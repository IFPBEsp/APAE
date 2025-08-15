'use client'

import { useState } from "react"
import { format } from "date-fns"
import { ptBR } from "date-fns/locale"
import {
  CalendarDays,
  Users,
  MessageCircleWarning,
  CalendarX
} from "lucide-react"

import { Button } from "@/components/ui/button"
import { Card, CardContent } from "@/components/ui/card"
import {
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableHeader,
  TableRow
} from "@/components/ui/table"
import { Calendar } from "@/components/ui/calendar"
import {
  Popover,
  PopoverContent,
  PopoverTrigger
} from "@/components/ui/popover"
import { Badge } from "@/components/ui/badge"

import {
  Dialog,
  DialogContent,
  DialogHeader,
  DialogTitle,
  DialogTrigger,
  DialogDescription,
} from "@/components/ui/dialog"

import { AppointmentForm } from "@/components/forms/AppointmentForm"
import { InfoCard } from "@/components/shared/InfoCard"
import Link from "next/link";

export default function DashboardPage() {
  const [selectedDate, setSelectedDate] = useState<Date | undefined>(new Date())

  const appointments = [
    { id: 1, name: "João Oliveira", confirmed: true, area: "Nutrição" },
    { id: 2, name: "Maria Silva", confirmed: true, area: "Psicologia" },
    { id: 3, name: "João Henrique", confirmed: true, area: "Psiquiatria" },
    { id: 4, name: "Lucas Ferreira", confirmed: false, area: "Nutrição" },
    { id: 5, name: "Rafael Andrade", confirmed: true, area: "Psiquiatria" },
    { id: 6, name: "Ana Beatriz", confirmed: true, area: "Nutrição" },
    { id: 7, name: "Júlia Fernandes", confirmed: true, area: "Psicologia" },
  ]
  return (
    <div className="min-h-screen w-full text-sm overflow-x-hidden">
      <main className="flex-1 p-3 sm:p-6 max-w-[100vw] mx-auto">
        <div className="mb-4 flex flex-col justify-between gap-3 sm:mb-6 sm:flex-row sm:items-center">
          <h1 className="text-lg font-bold sm:text-2xl">Agendamentos de Hoje</h1>
          <div className="flex flex-col gap-2 sm:flex-row sm:items-center">
            <Popover>
              <PopoverTrigger asChild>
                <Button
                  variant="outline"
                  className="w-full justify-start bg-white text-left font-normal text-xs sm:w-[220px] sm:text-sm"
                >
                  <CalendarDays className="mr-2 h-4 w-4" />
                  {selectedDate
                    ? format(selectedDate, "dd 'de' MMMM 'de' yyyy", { locale: ptBR })
                    : <span>Escolha uma data</span>}
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
                <Button className="w-full bg-blue-800 text-white hover:bg-blue-900 text-xs sm:w-auto sm:text-sm">
                  Novo agendamento
                </Button>
              </DialogTrigger>
              <DialogContent className="w-full sm:max-w-[425px]">
                <DialogHeader>
                  <DialogTitle>Cadastrar Novo Agendamento</DialogTitle>
                  <DialogDescription>
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
            title="Agendados pra hoje"
            icon={Users}
            value={6}
            subtitle="5 confirmados, 2 pendentes"
          />
          <InfoCard
            title="Todos os agendamentos"
            icon={Users}
            value={25}
          />
          <InfoCard
            title="Sem justificativa"
            icon={MessageCircleWarning}
            value={3}
            iconColor="text-red-400"
            subtitle="Pacientes que não justificaram suas faltas"
          />
          <InfoCard
            title="Não confirmados"
            icon={CalendarX}
            value={2}
            subtitle="Consultas que não foram confirmadas"
          />
        </div>

        <Card>
          <CardContent className="p-0">
            <Table>
              <TableHeader>
                <TableRow>
                  <TableHead className="px-3 py-2 text-xs sm:px-4 sm:py-3 sm:text-sm">Paciente</TableHead>
                  <TableHead className="px-3 py-2 text-xs sm:px-4 sm:py-3 sm:text-sm">Confirmou Presença</TableHead>
                  <TableHead className="px-3 py-2 text-xs sm:px-4 sm:py-3 sm:text-sm">Área</TableHead>
                  <TableHead className="px-3 py-2 text-xs sm:px-4 sm:py-3 sm:text-sm">Ações</TableHead>
                </TableRow>
              </TableHeader>
              <TableBody>
                {appointments.map((item, index) => (
                  <TableRow key={index}>
                    <TableCell className="px-3 py-2 text-xs sm:px-4 sm:py-3 sm:text-sm">{item.name}</TableCell>
                    <TableCell className="px-3 py-2">
                      <Badge
                        variant="outline"
                        className={`text-xs ${item.confirmed ? "text-green-400" : "text-red-400"} sm:text-sm`}
                      >
                        {item.confirmed ? "Sim" : "Não"}
                      </Badge>
                    </TableCell>
                    <TableCell className="px-3 py-2 text-xs sm:px-4 sm:py-3 sm:text-sm">{item.area}</TableCell>
                    <TableCell className="px-3 py-2">
                      <Link
                          href={`/agendamentos/${item.id}`}
                          className="cursor-pointer text-xs text-blue-800 underline hover:underline sm:text-sm"
                      >
                        Detalhes
                      </Link>
                    </TableCell>
                  </TableRow>
                ))}
              </TableBody>
            </Table>
          </CardContent>
        </Card>
      </main>
    </div>
  )
}