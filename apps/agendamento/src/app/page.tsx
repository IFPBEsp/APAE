'use client'

import { AppSidebar } from "@/components/sidebar/sidebar"
import { CalendarDays } from "lucide-react"
import { Button } from "@/components/ui/button"
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import {
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableHeader,
  TableRow,
} from "@/components/ui/table"

export default function DashboardPage() {
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
        {/* Header */}
        <div className="flex justify-between items-center mb-4">
          <h1 className="text-2xl font-bold text-blue-800">Agendamentos de Hoje</h1>
          <div className="flex items-center gap-2">
            <div className="flex items-center border rounded-md px-3 py-2 text-sm text-muted-foreground bg-white">
              <CalendarDays className="mr-2 h-4 w-4" />
              <span>Feb 10, 2025</span>
            </div>
            <Button> Novo agendamento </Button>
          </div>
        </div>

        {/* Cards */}
        <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-4 mb-6">
          {cards.map((card, i) => (
            <Card key={i}>
              <CardHeader>
                <CardTitle className="text-sm text-muted-foreground">{card.title}</CardTitle>
              </CardHeader>
              <CardContent>
                <div className="text-2xl font-bold">{card.value}</div>
                <div className="text-xs text-muted-foreground mt-1">{card.subtitle}</div>
              </CardContent>
            </Card>
          ))}
        </div>

        {/* Tabela */}
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
                      <span className={item.confirmed ? "text-green-600" : "text-red-500"}>
                        {item.confirmed ? "Sim" : "Não"}
                      </span>
                    </TableCell>
                    <TableCell>{item.area}</TableCell>
                    <TableCell className="text-blue-600 hover:underline cursor-pointer">
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
