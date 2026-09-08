"use client";

import Link from "next/link";
import { useRouter } from "next/navigation";
import { UserX, UserCheck, MoreHorizontal, Edit, Eye } from "lucide-react";

import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from "@/components/ui/select";
import { Table, TableBody, TableCell, TableHead, TableHeader, TableRow } from "@/components/ui/table";
import { DropdownMenu, DropdownMenuContent, DropdownMenuItem, DropdownMenuLabel, DropdownMenuSeparator, DropdownMenuTrigger } from "@/components/ui/dropdown-menu";
import { AlertDialog, AlertDialogAction, AlertDialogCancel, AlertDialogContent, AlertDialogDescription, AlertDialogFooter, AlertDialogHeader, AlertDialogTitle, AlertDialogTrigger } from "@/components/ui/alert-dialog";

import { useFetchProfessionals } from "@/hooks/profissional/use-fetch-profissional";
import { useInactivateProfessional } from "@/hooks/profissional/use-inactivate-profissional";
import { useActivateProfessional } from "@/hooks/profissional/use-activate-profissional";
import { useProfessionalFilters } from "@/hooks/profissional/use-professional-filters";
import { getStatusActionConfig } from "@/domains/professional/shared/professional.utils";

export default function VisualizationProfessionalPage() {
  const router = useRouter();

  const {
    searchTerm, setSearchTerm,
    areaFilter, setAreaFilter,
    statusFilter, setStatusFilter,
    filteredProfessionals,
    uniqueAreas,
  } = useProfessionalFilters([]);

  const { professionals, loading, error, setProfessionals } = useFetchProfessionals(statusFilter === "activate");

  const { inactivate } = useInactivateProfessional();
  const { activate } = useActivateProfessional();

  const { label: actionLabel, itemClass: actionItemClass, buttonClass: actionButtonClass } = getStatusActionConfig(statusFilter);

  const actionIcon = statusFilter === "activate"
    ? <UserX className="mr-2 h-4 w-4" />
    : <UserCheck className="mr-2 h-4 w-4" />;

  const handleConfirm = async (id: string) => {
    try {
      if (statusFilter === "activate") await inactivate(id);
      else await activate(id);
      setProfessionals((prev) => prev.filter((p) => p.id !== id));
    } catch (err) {
      console.error(err);
    }
  };

  // rederiva filtros com a lista real
  const { filteredProfessionals: filtered, uniqueAreas: areas } = useProfessionalFilters(professionals);

  // sync estado externo
  const displayList = professionals.filter((prof) => {
    const name = prof.name?.toLowerCase() || "";
    const document = prof.professionalDocument?.toLowerCase() || "";
    const cpf = prof.cpf?.toLowerCase() || "";
    const term = searchTerm.toLowerCase();
    const matchesSearch = name.includes(term) || document.includes(term) || cpf.includes(term);
    const matchesArea = areaFilter === "all" || prof.serviceArea.area === areaFilter;
    return matchesSearch && matchesArea;
  });

  const displayAreas = ["all", ...Array.from(new Set(professionals.map((p) => p.serviceArea.area)))];

  return (
    <div className="w-full bg-background p-4 md:p-6 lg:p-8">
      <div className="mx-auto w-full max-w-[1400px]">
        <header className="mb-8 flex flex-col gap-4 md:flex-row md:items-center md:justify-between">
          <h1 className="text-2xl font-semibold lg:text-3xl text-[#0D4F97]">Profissionais da Saúde</h1>
          <Button className="bg-[#0D4F97] hover:bg-blue-900 cursor-pointer" onClick={() => router.push("/professionals/create")}>
            Cadastrar Profissional
          </Button>
        </header>

        <div className="mb-6 flex flex-col gap-4 md:flex-row">
          <Input
            placeholder="Buscar por nome, CPF ou documento..."
            className="flex-grow border-[#0D4F97]"
            value={searchTerm}
            onChange={(e) => setSearchTerm(e.target.value)}
          />
          <Select value={statusFilter} onValueChange={(v) => setStatusFilter(v as "activate" | "inactivate")}>
            <SelectTrigger className="w-full md:w-[200px] border-[#0D4F97] text-[#0D4F97]">
              <SelectValue placeholder="Situação" />
            </SelectTrigger>
            <SelectContent>
              <SelectItem value="activate">Ativo</SelectItem>
              <SelectItem value="inactivate">Inativo</SelectItem>
            </SelectContent>
          </Select>
          <Select value={areaFilter} onValueChange={setAreaFilter}>
            <SelectTrigger className="w-full md:w-[200px] border-[#0D4F97] text-[#0D4F97]">
              <SelectValue placeholder="Filtrar por área" />
            </SelectTrigger>
            <SelectContent>
              {displayAreas.map((area) => (
                <SelectItem key={area} value={area}>{area === "all" ? "Todas as Áreas" : area}</SelectItem>
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
                  <TableHead>Profissional</TableHead>
                  <TableHead>Documento</TableHead>
                  <TableHead>Área</TableHead>
                  <TableHead className="hidden md:table-cell">Telefone</TableHead>
                  <TableHead />
                </TableRow>
              </TableHeader>
              <TableBody>
                {displayList.length > 0 ? (
                  displayList.map((prof) => (
                    <TableRow key={prof.id}>
                      <TableCell className="font-medium">{prof.name}</TableCell>
                      <TableCell>{prof.professionalDocument}</TableCell>
                      <TableCell>{prof.serviceArea.area}</TableCell>
                      <TableCell className="hidden md:table-cell">{prof.phoneNumber}</TableCell>
                      <TableCell className="text-right">
                        <AlertDialog>
                          <DropdownMenu>
                            <DropdownMenuTrigger asChild>
                              <Button variant="ghost" className="h-8 w-8 p-0 cursor-pointer">
                                <MoreHorizontal className="h-4 w-4" />
                              </Button>
                            </DropdownMenuTrigger>
                            <DropdownMenuContent align="end">
                              <DropdownMenuLabel>Ações</DropdownMenuLabel>
                              <Link href={`/professionals/view/${prof.id}`}>
                                <DropdownMenuItem><Eye className="mr-2 h-4 w-4" />Visualizar Perfil</DropdownMenuItem>
                              </Link>
                              <DropdownMenuItem onClick={() => router.push(`/professionals/edit/${prof.id}`)}>
                                <Edit className="mr-2 h-4 w-4" />Editar
                              </DropdownMenuItem>
                              <DropdownMenuSeparator />
                              <AlertDialogTrigger asChild>
                                <DropdownMenuItem className={actionItemClass}>
                                  {actionIcon}{actionLabel}
                                </DropdownMenuItem>
                              </AlertDialogTrigger>
                            </DropdownMenuContent>
                          </DropdownMenu>
                          <AlertDialogContent>
                            <AlertDialogHeader>
                              <AlertDialogTitle>Você tem certeza?</AlertDialogTitle>
                              <AlertDialogDescription>
                                {statusFilter === "activate"
                                  ? `Esta ação irá inativar o profissional ${prof.name}.`
                                  : `Esta ação irá reativar o profissional ${prof.name}.`}
                              </AlertDialogDescription>
                            </AlertDialogHeader>
                            <AlertDialogFooter>
                              <AlertDialogCancel>Cancelar</AlertDialogCancel>
                              <AlertDialogAction className={actionButtonClass} onClick={() => handleConfirm(prof.id)}>
                                {actionLabel}
                              </AlertDialogAction>
                            </AlertDialogFooter>
                          </AlertDialogContent>
                        </AlertDialog>
                      </TableCell>
                    </TableRow>
                  ))
                ) : (
                  <TableRow>
                    <TableCell colSpan={5} className="h-24 text-center">Nenhum profissional encontrado.</TableCell>
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
