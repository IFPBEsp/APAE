"use client";

import Link from "next/link";
import { useState } from "react";
import { useRouter } from "next/navigation";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from "@/components/ui/select";
import {
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableHeader,
  TableRow,
} from "@/components/ui/table";
import { MoreHorizontal, Edit, Eye, CircleX, CircleCheck } from "lucide-react";
import {
  DropdownMenu,
  DropdownMenuContent,
  DropdownMenuItem,
  DropdownMenuLabel,
  DropdownMenuSeparator,
  DropdownMenuTrigger,
} from "@/components/ui/dropdown-menu";
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
} from "@/components/ui/alert-dialog";

import { useFetchProfessionals } from "@/hooks/profissional/use-fetch-profissional";
import { useActivateProfissional } from "@/hooks/profissional/use-activate-profissional";
import { useInactivateProfissional } from "@/hooks/profissional/use-inactivate-profissional";

export default function VisualizationProfessionalPage() {
  const { profissionais, loading, error, setProfissionais } =
    useFetchProfessionals();
  const { activate } = useActivateProfissional();
  const { inactivate } = useInactivateProfissional();

  const router = useRouter();

  const [searchTerm, setSearchTerm] = useState("");
  const [areaFilter, setAreaFilter] = useState("all");

  const handleAddNew = () => {
    router.push("/register-profissional");
  };

  const handleEdit = (id: string) => {
    router.push(`/update-profissional/${id}`);
  };

  const handleActivateConfirm = async (id: string) => {
    try {
      await activate(id);
      setProfissionais((prev) => prev.map((p) => p.id === id ? { ...p, ativo: true } : p));
    } catch (error) {
      console.error("Erro ao ativar profissional", error);
    }
  };

  const handleInactivateConfirm = async (id: string) => {
    try {
      await inactivate(id);
      setProfissionais((prev) => prev.map((p) => p.id === id ? { ...p, ativo: false } : p));
      console.log('Profissional inativado com sucesso');
    } catch (error) {
      console.error("Erro ao ativar profissional", error);
    }
  };

  const filteredProfissionais = profissionais.filter((prof) => {
    const matchesSearch =
      prof.nome.toLowerCase().includes(searchTerm.toLowerCase()) ||
      prof.docProfissional.toLowerCase().includes(searchTerm.toLowerCase());
    const matchesArea = areaFilter === "all" || prof.areaDaSaude === areaFilter;
    return matchesSearch && matchesArea;
  });

  const uniqueAreas = [
    "all",
    ...Array.from(new Set(profissionais.map((p) => p.areaDaSaude))),
  ];

  return (
    <div className="w-full bg-background p-4 md:p-6 lg:p-8">
      <div className="mx-auto w-full max-w-[1400px]">
        <header className="mb-8 flex flex-col items-stretch gap-4 md:flex-row md:items-center md:justify-between">
          <h1 className="text-2xl font-semibold lg:text-3xl text-[#0D4F97]"
          >
            Profissionais da Saúde
          </h1>
          <Button
            className="md:w-auto bg-[#0D4F97] hover:bg-blue-900"
            onClick={handleAddNew}
          >
            Cadastrar Profissional
          </Button>
        </header>

        <div className="mb-6 flex flex-col gap-4 md:flex-row">
          <Input
            type="text"
            placeholder="Buscar por nome ou documento..."
            className="flex-grow border-[#0D4F97]"
            value={searchTerm}
            onChange={(e) => setSearchTerm(e.target.value)}
          />
          <Select value={areaFilter} onValueChange={setAreaFilter}>
            <SelectTrigger 
              className="w-full md:w-[200px] border-[#0D4F97] hover:bg-accent text-[#0D4F97]" 
            >
              <SelectValue placeholder="Filtrar por área" />
            </SelectTrigger>
            <SelectContent>
              {uniqueAreas.map((area) => (
                <SelectItem  className="border-[#0D4F97]" key={area} value={area} >
                  {area === "all" ? "Todas as Áreas" : area}
                </SelectItem>
              ))}
            </SelectContent>
          </Select>
        </div>

        {loading && <p>Carregando...</p>}
        {error && <p className="text-red-500">{error}</p>}

        {!loading && !error && (
          <div className="rounded-lg border bg-card">
            <Table>
              <TableHeader>
                <TableRow>
                  <TableHead className="text-[#0D4F97]">Profissional</TableHead>
                  <TableHead className="text-[#0D4F97]">Documento</TableHead>
                  <TableHead className="text-[#0D4F97]">Área</TableHead>
                  <TableHead className="hidden md:table-cell text-[#0D4F97]">
                    Telefone
                  </TableHead>
                  <TableHead className="text-[#0D4F97]">Status</TableHead>
                  <TableHead>
                    <span className="sr-only text-[#0D4F97]">Ações</span>
                  </TableHead>
                </TableRow>
              </TableHeader>
              <TableBody>
                {filteredProfissionais.length > 0 ? (
                  filteredProfissionais.map((prof) => (
                    <TableRow key={prof.id}>
                      <TableCell className="font-medium">{prof.nome}</TableCell>
                      <TableCell>{prof.docProfissional}</TableCell>
                      <TableCell>{prof.areaDaSaude}</TableCell>
                      <TableCell className="hidden md:table-cell">
                        {prof.telefone}
                      </TableCell>
                      <TableCell>{prof.ativo ? "Ativo" : "Inativo"}</TableCell>
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
                              <Link href={`/profissionais/${prof.id}`} passHref>
                                  <DropdownMenuItem>
                                    <Eye className="mr-2 h-4 w-4" />
                                    Visualizar Perfil
                                  </DropdownMenuItem>
                              </Link>

                              <DropdownMenuItem
                                onClick={() => handleEdit(prof.id)}
                              >
                                <Edit className="mr-2 h-4 w-4" />
                                Editar
                              </DropdownMenuItem>
                              <DropdownMenuSeparator />
                              <AlertDialogTrigger asChild>
                                { prof.ativo ?
                                  <DropdownMenuItem className="text-destructive focus:text-destructive">
                                    <CircleX className="mr-2 h-4 w-4" />
                                    Inativar
                                  </DropdownMenuItem>
                                  :
                                  <DropdownMenuItem className="text-default focus:text-default text-[#008000]">
                                    <CircleCheck className="mr-2 h-4 w-4" />
                                    Ativar
                                  </DropdownMenuItem>
                                }
                              </AlertDialogTrigger>
                            </DropdownMenuContent>
                          </DropdownMenu>

                          <AlertDialogContent>
                            <AlertDialogHeader>
                              <AlertDialogTitle>
                                Você tem certeza?
                              </AlertDialogTitle>
                              <AlertDialogDescription>
                                Esta ação irá {prof.ativo ? "inativar" : "ativar"} o profissional{" "}
                                <strong className="font-medium">
                                  {prof.nome}
                                </strong>
                                .
                              </AlertDialogDescription>
                            </AlertDialogHeader>
                            <AlertDialogFooter>
                              <AlertDialogCancel>Cancelar</AlertDialogCancel>
                              <AlertDialogAction
                                onClick={() => 
                                  prof.ativo ? handleInactivateConfirm(prof.id) : handleActivateConfirm(prof.id)
                                }
                              >
                                {prof.ativo ? "Inativar" : "Ativar"}
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
        )}
      </div>
    </div>
  );
}
