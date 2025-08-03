'use client'

import { useState } from "react"
import { format } from "date-fns"
import { ptBR } from "date-fns/locale"
import {
  CalendarDays,
  Users,
  MessageCircleWarning,
  CalendarX,
  SearchIcon,
  ChevronDown
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

import { InfoCard } from "@/components/shared/InfoCard"
import { Input } from "@/components/ui/input"
import { DropdownMenu, DropdownMenuContent, DropdownMenuItem, DropdownMenuTrigger } from "@/components/ui/dropdown-menu"

const areas = [
  'Área da Saúde',
  'Cardiologia',
  'Pediatria',
  'Neurologia',
  'Fisioterapia',
]

export default function AllApointments() {
  const [selectedDate, setSelectedDate] = useState<Date | undefined>(new Date())
  const [selectedArea, setSelectedArea] = useState('Área da Saúde')

  const appointments = [
    { name: "João Oliveira", confirmed: true, area: "Nutrição" },
    { name: "Maria Silva", confirmed: true, area: "Psicologia" },
    { name: "João Henrique", confirmed: true, area: "Psiquiatria" },
    { name: "Lucas Ferreira", confirmed: false, area: "Nutrição" },
    { name: "Rafael Andrade", confirmed: true, area: "Psiquiatria" },
    { name: "Ana Beatriz", confirmed: true, area: "Nutrição" },
    { name: "Júlia Fernandes", confirmed: true, area: "Psicologia" },
  ]

  return (
    <div className="min-h-screen w-full text-sm overflow-x-hidden">
      <main className="flex-1 p-3 sm:p-6 w-full max-w-none">
        <div className="mb-4 flex flex-col justify-between gap-3 sm:mb-6 sm:flex-row sm:items-center">
          <h1 className="text-lg font-bold sm:text-2xl">Todos os Agendamentos</h1>
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
            <Button className="w-full bg-blue-800 text-white hover:bg-blue-900 text-xs sm:w-auto sm:text-sm">
              Novo agendamento
            </Button>
          </div>
        </div>

        <div className="mb-4 grid grid-cols-1 gap-3 sm:grid-cols-2 lg:grid-cols-4">
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

       <div className="flex items-center gap-2 mb-4">
            <div className="relative w-full">
                <SearchIcon className="absolute left-3 top-1/2 h-4 w-4 -translate-y-1/2 text-muted-foreground" />
                <Input
                placeholder="Buscar paciente..."
                className="pl-10 pr-3"
                />
            </div>
            <DropdownMenu>
                <DropdownMenuTrigger asChild>
                <Button variant="outline" className="whitespace-nowrap">
                    {selectedArea}
                    <ChevronDown className="ml-2 h-4 w-4" />
                </Button>
                </DropdownMenuTrigger>
                <DropdownMenuContent align="end">
                {areas.map((area) => (
                    <DropdownMenuItem
                    key={area}
                    onClick={() => setSelectedArea(area)}
                    >
                    {area}
                    </DropdownMenuItem>
                ))}
                </DropdownMenuContent>
            </DropdownMenu>
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
                      <span className="cursor-pointer text-xs text-blue-800 underline hover:underline sm:text-sm">
                        Detalhes
                      </span>
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
