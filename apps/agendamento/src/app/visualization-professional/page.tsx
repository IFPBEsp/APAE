'use client'

import { useState } from 'react'
import { MoreHorizontal, Edit, Trash2 } from 'lucide-react'

import { Button } from '@/components/ui/button'
import { Input } from '@/components/ui/input'
import {
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableHeader,
  TableRow,
} from '@/components/ui/table'
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from '@/components/ui/select'
import {
  DropdownMenu,
  DropdownMenuContent,
  DropdownMenuItem,
  DropdownMenuLabel,
  DropdownMenuSeparator,
  DropdownMenuTrigger,
} from '@/components/ui/dropdown-menu'
import {
  AlertDialog,
  AlertDialogAction,
  AlertDialogCancel,
  AlertDialogContent,
  AlertDialogDescription,
  AlertDialogFooter,
  AlertDialogHeader,
  AlertDialogTitle,
  AlertDialogTrigger,
} from '@/components/ui/alert-dialog'
import Link from 'next/link'

interface Profissional {
  id: string
  nome: string
  documento: string
  area: string
  telefone: string
}

const mockData: Profissional[] = [
    { id: '1', nome: 'João', documento: 'CRN: 10644 / CRN-6', area: 'Nutrição', telefone: '(12) 91234-5678' },
    { id: '2', nome: 'Maria', documento: 'CREFITO: 87654 / SP', area: 'Fisioterapia', telefone: '(11) 98765-4321' },
    { id: '3', nome: 'Carlos', documento: 'CRM: 54321 / RJ', area: 'Clínico Geral', telefone: '(21) 91234-5678' },
    { id: '4', nome: 'Ana', documento: 'CRP: 06/12345', area: 'Psicologia', telefone: '(11) 95555-4444' },
    { id: '5', nome: 'Pedro', documento: 'COREN: 98765 / BA', area: 'Enfermagem', telefone: '(71) 93333-2222' }
]

export default function VisualizationProfessionalPage() {
  const [profissionais, setProfissionais] = useState<Profissional[]>(mockData)
  const [searchTerm, setSearchTerm] = useState('')
  const [areaFilter, setAreaFilter] = useState('all')

  const handleAddNew = () => {
    console.log('Navegar para a página de adicionar novo profissional')
  }

  const handleEdit = (id: string) => {
    console.log(`Navegar para a página de edição do profissional ${id}`)
  }
  
  const handleDeleteConfirm = (id: string) => {
    setProfissionais((prev) => prev.filter((p) => p.id !== id))
  }

  const filteredProfissionais = profissionais.filter((prof) => {
    const matchesSearch = prof.nome.toLowerCase().includes(searchTerm.toLowerCase()) ||
                          prof.documento.toLowerCase().includes(searchTerm.toLowerCase())
    const matchesArea = areaFilter === 'all' || prof.area === areaFilter
    return matchesSearch && matchesArea
  })
  
  const uniqueAreas = ['all', ...Array.from(new Set(mockData.map((p) => p.area)))]

  return (
    <div className="w-full bg-background p-4 md:p-6 lg:p-8">
      <div className="mx-auto w-full max-w-[1400px]">
        
        <header className="mb-8 flex flex-col items-stretch gap-4 md:flex-row md:items-center md:justify-between">
          <h1 className="text-2xl font-semibold text-foreground lg:text-3xl">
            Profissionais da Saúde
          </h1>
          <Link href="/register-profissional">
            <Button className="md:w-auto bg-[#020617]">
              Cadastrar Profissional
            </Button>
          </Link>
        </header>

        <div className="mb-6 flex flex-col gap-4 md:flex-row">
          <Input
            type="text"
            placeholder="Buscar por nome ou documento..."
            className="flex-grow"
            value={searchTerm}
            onChange={(e) => setSearchTerm(e.target.value)}
          />
          <Select value={areaFilter} onValueChange={setAreaFilter}>
            <SelectTrigger className="w-full md:w-[200px]">
              <SelectValue placeholder="Filtrar por área" />
            </SelectTrigger>
            <SelectContent>
              {uniqueAreas.map((area) => (
                <SelectItem key={area} value={area}>
                  {area === 'all' ? 'Todas as Áreas' : area}
                </SelectItem>
              ))}
            </SelectContent>
          </Select>
        </div>

        <div className="rounded-lg border bg-card">
          <Table>
            <TableHeader>
              <TableRow>
                <TableHead>Profissional</TableHead>
                <TableHead>Documento</TableHead>
                <TableHead>Área</TableHead>
                <TableHead className="hidden md:table-cell">Telefone</TableHead>
                <TableHead>
                  <span className="sr-only">Ações</span>
                </TableHead>
              </TableRow>
            </TableHeader>
            <TableBody>
              {filteredProfissionais.length > 0 ? (
                filteredProfissionais.map((prof) => (
                  <TableRow key={prof.id}>
                    <TableCell className="font-medium">{prof.nome}</TableCell>
                    <TableCell>{prof.documento}</TableCell>
                    <TableCell>{prof.area}</TableCell>
                    <TableCell className="hidden md:table-cell">{prof.telefone}</TableCell>
                    <TableCell className="text-right">
                      <AlertDialog>
                        <DropdownMenu>
                          <DropdownMenuTrigger asChild>
                            <Button variant="ghost" className="h-8 w-8 p-0">
                              <span className="sr-only">Abrir menu</span>
                              <MoreHorizontal className="h-4 w-4" />
                            </Button>
                          </DropdownMenuTrigger>
                          <DropdownMenuContent align="end">
                            <DropdownMenuLabel>Ações</DropdownMenuLabel>
                            <DropdownMenuItem onClick={() => handleEdit(prof.id)}>
                              <Edit className="mr-2 h-4 w-4" />
                              Editar
                            </DropdownMenuItem>
                            <DropdownMenuSeparator />
                            <AlertDialogTrigger asChild>
                              <DropdownMenuItem className="text-destructive focus:text-destructive">
                                <Trash2 className="mr-2 h-4 w-4" />
                                Excluir
                              </DropdownMenuItem>
                            </AlertDialogTrigger>
                          </DropdownMenuContent>
                        </DropdownMenu>

                        <AlertDialogContent>
                          <AlertDialogHeader>
                            <AlertDialogTitle>Você tem certeza?</AlertDialogTitle>
                            <AlertDialogDescription>
                              Esta ação não pode ser desfeita. Isso irá excluir permanentemente o profissional <strong className="font-medium">{prof.nome}</strong>.
                            </AlertDialogDescription>
                          </AlertDialogHeader>
                          <AlertDialogFooter>
                            <AlertDialogCancel>Cancelar</AlertDialogCancel>
                            <AlertDialogAction onClick={() => handleDeleteConfirm(prof.id)}>
                              Excluir
                            </AlertDialogAction>
                          </AlertDialogFooter>
                        </AlertDialogContent>
                      </AlertDialog>
                    </TableCell>
                  </TableRow>
                ))
              ) : (
                <TableRow>
                  <TableCell colSpan={5} className="h-24 text-center">
                    Nenhum profissional encontrado.
                  </TableCell>
                </TableRow>
              )}
            </TableBody>
          </Table>
        </div>
      </div>
    </div>
  )
}