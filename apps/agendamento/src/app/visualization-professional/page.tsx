"use client";

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
import { MoreHorizontal, Edit, Trash2 } from "lucide-react";
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
import { useRemoveProfissional } from "@/hooks/profissional/use-delete-profissional";

export default function VisualizationProfessionalPage() {
  const { profissionais, loading, error, setProfissionais } =
    useFetchProfessionals();
  const { remove } = useRemoveProfissional();

  const router = useRouter();

  const [searchTerm, setSearchTerm] = useState("");
  const [areaFilter, setAreaFilter] = useState("all");

  const handleAddNew = () => {
    router.push("/register-profissional");
  };

  const handleEdit = (id: string) => {
    router.push(`/update-profissional/${id}`);
  };

  const handleDeleteConfirm = async (id: string) => {
    try {
      await remove(id);
      setProfissionais((prev) => prev.filter((p) => p.id !== id));
    } catch (error) {
      console.error("Erro ao excluir profissional", error);
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
          <h1 className="text-2xl font-semibold lg:text-3xl" style={{ color: "#003b93" }}>
            Profissionais da Saúde
          </h1>
          <Button
            style={{ backgroundColor: "#0D4F97" }}
            className="md:w-auto bg-blue-800 hover:bg-blue-900"
            onClick={handleAddNew}
          >
            Cadastrar Profissional
          </Button>
        </header>

        <div className="mb-6 flex flex-col gap-4 md:flex-row">
          <Input
            style={{ borderColor: "#0D4F97" }}
            type="text"
            placeholder="Buscar por nome ou documento..."
            className="flex-grow"
            value={searchTerm}
            onChange={(e) => setSearchTerm(e.target.value)}
          />
          <Select value={areaFilter} onValueChange={setAreaFilter}>
            <SelectTrigger 
              className="w-full md:w-[200px]" 
              style={{ borderColor: "#0D4F97", color: "#0D4F97" }}  
            >
              <SelectValue placeholder="Filtrar por área" />
            </SelectTrigger>
            <SelectContent>
              {uniqueAreas.map((area) => (
                <SelectItem key={area} value={area} style={{ color: "#0D4F97" }}>
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
                              <DropdownMenuItem
                                onClick={() => handleEdit(prof.id)}
                              >
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
                              <AlertDialogTitle>
                                Você tem certeza?
                              </AlertDialogTitle>
                              <AlertDialogDescription>
                                Esta ação não pode ser desfeita. Isso irá
                                excluir permanentemente o profissional{" "}
                                <strong className="font-medium">
                                  {prof.nome}
                                </strong>
                                .
                              </AlertDialogDescription>
                            </AlertDialogHeader>
                            <AlertDialogFooter>
                              <AlertDialogCancel>Cancelar</AlertDialogCancel>
                              <AlertDialogAction
                                onClick={() => handleDeleteConfirm(prof.id)}
                              >
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
        )}
      </div>
    </div>
  );
}
