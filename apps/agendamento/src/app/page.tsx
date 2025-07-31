'use client'

import { useState } from "react"
import { format } from "date-fns"
import { ptBR } from "date-fns/locale"
import { CalendarDays, Users, CreditCard, Activity } from "lucide-react"  

import { AppSidebar } from "@/components/sidebar/sidebar"
import { Button } from "@/components/ui/button"
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card"
import { Table, TableBody, TableCell, TableHead, TableHeader, TableRow } from "@/components/ui/table"
import { Calendar } from "@/components/ui/calendar"
import { Popover, PopoverContent, PopoverTrigger } from "@/components/ui/popover"
import { Badge } from "@/components/ui/badge" 

export default function DashboardPage() {
  const [selectedDate, setSelectedDate] = useState<Date | undefined>(new Date())

  const cards = [
    { title: "Agendados pra hoje", value: "6", subtitle: "5 confirmados, 1 pendente" },
    { title: "Todos os agendamentos", value: "25", subtitle: "+180.1% from last month" },
    { title: "Sem justificativa", value: "3", subtitle: "+19% from last month" },
    { title: "Não confirmados", value: "2", subtitle: "+201 since last hour" },
  ]

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
    <div className="flex min-h-screen bg-muted text-sm">
      <AppSidebar />

      <main className="flex-1 p-6">
        <div className="flex justify-between items-center mb-4">
          <h1 className="text-2xl font-bold text-blue-800">Agendamentos de Hoje</h1>
          <div className="flex items-center gap-2">

            <Popover>
              <PopoverTrigger asChild>
                <Button
                  variant="outline"
                  className="w-[220px] justify-start text-left font-normal bg-white"
                >
                  <CalendarDays className="mr-2 h-4 w-4" />
                  {selectedDate ? format(selectedDate, "dd 'de' MMMM 'de' yyyy", { locale: ptBR }) : <span>Escolha uma data</span>}
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

            <Button className="bg-blue-800 text-white hover:bg-blue-900">Novo agendamento</Button>
          </div>
        </div>

        <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-4 mb-6">
          {cards.map((card, i) => (
            <Card key={i}>
              <CardHeader className="flex justify-between items-center">
                <CardTitle className="text-sm text-black">{card.title}</CardTitle>
                {(card.title === "Agendados pra hoje" || card.title === "Todos os agendamentos") && (
                  <Users className="h-7 w-7 text-black-600" />
                )}

                {card.title === "Sem justificativa" && (
                  <CreditCard className="h-7 w-7 text-red-600" />
                )}

                {card.title === "Não confirmados" && (
                  <Activity className="h-7 w-7 text-red-600" />
                )}
              </CardHeader>
              <CardContent>
                <div className="text-2xl font-bold">{card.value}</div>
                <div className="text-xs text-muted-foreground mt-1">{card.subtitle}</div>
              </CardContent>
            </Card>
          ))}
        </div>

        <Card>
          <CardContent>
            <Table>
              <TableHeader>
                <TableRow>
                  <TableHead>Paciente</TableHead>
                  <TableHead>Confirmado</TableHead>
                  <TableHead>Área de atendimento</TableHead>
                  <TableHead>Ações</TableHead>
                </TableRow>
              </TableHeader>
              <TableBody>
                {appointments.map((item, index) => (
                  <TableRow key={index}>
                    <TableCell>{item.name}</TableCell>
                    <TableCell>
                      <Badge variant="outline" className={item.confirmed ? "border-green-600 text-green-600" : "border-red-500 text-red-500"}>
                        {item.confirmed ? "Sim" : "Não"}
                      </Badge>
                    </TableCell>
                    <TableCell>{item.area}</TableCell>
                    <TableCell className="text-black-600 hover:underline cursor-pointer">
                      Detalhes
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
