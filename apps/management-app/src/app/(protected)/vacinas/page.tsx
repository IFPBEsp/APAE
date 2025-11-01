"use client";

import { useState } from "react";
import Link from "next/link";
import { Button } from "@/components/ui/button";
import { Plus, Loader2, Edit, Trash2 } from "lucide-react";
import { SearchFilters } from "@/components/search-filters";
import { useVaccinesContext, Vaccine } from "@/hooks/use-vaccines";
import { useRouter } from "next/navigation";

type VaccinesListItemProps = Readonly<{
    vaccine: Vaccine;
}>;

function VaccinesListItem({ vaccine }: VaccinesListItemProps) {
    const { deleteVaccine } = useVaccinesContext();
    const router = useRouter();

    const onEdit = () => {
        router.push(`/vacinas/${vaccine.id}/edit`);
    };

    const onDelete = () => {
        if (confirm("Tem certeza que deseja excluir esta vacina?")) {
            deleteVaccine(vaccine);
        }
    };

    return (
        <div className="flex items-center justify-between p-4 border-b hover:bg-gray-50 transition-colors">
            <div className="flex-1">
                <h3 className="font-bold text-base text-[#003B93]">
                    {vaccine.name}
                </h3>
            </div>
            <div className="flex items-center gap-2 ml-4">
                <Button
                    variant="outline"
                    size="icon"
                    className="h-8 w-8"
                    onClick={onEdit}
                    aria-label="Editar"
                >
                    <Edit className="h-4 w-4" />
                </Button>
                <Button
                    variant="outline"
                    size="icon"
                    className="h-8 w-8 hover:bg-red-50 hover:border-red-500"
                    onClick={onDelete}
                    aria-label="Excluir"
                >
                    <Trash2 className="h-4 w-4 text-red-500" />
                </Button>
            </div>
        </div>
    );
}

function VaccinesList() {
    const { vaccines, loading, feedback } = useVaccinesContext();

    if (loading) {
        return (
            <div className="flex justify-center items-center p-10">
                <Loader2 className="h-8 w-8 animate-spin text-gray-500" />
            </div>
        );
    }

    if (feedback.error) {
        return <p className="text-center text-red-500">{feedback.message}</p>;
    }

    if (vaccines.length === 0) {
        return (
            <p className="text-center text-gray-500">
                Nenhuma vacina encontrada.
            </p>
        );
    }

    return (
        <>
            <p className="text-sm text-gray-500 mb-4">
                {vaccines.length} vacinas encontradas
            </p>
            <div className="space-y-2">
                {vaccines.map((vaccine) => (
                    <VaccinesListItem key={vaccine.id} vaccine={vaccine} />
                ))}
            </div>
        </>
    );
}

export default function VaccinesPage() {
    const [searchName, setSearchName] = useState<string>("");

    return (
        <div className="!bg-slate-100 min-h-screen">
            <main className="container mx-auto p-4 md:p-6">
                <div className="bg-white rounded-xl shadow-md border-2 p-6 mb-4">
                    <SearchFilters
                        searchName={searchName}
                        setSearchName={setSearchName}
                    />
                </div>

                <section className="relative md:bg-white md:rounded-xl md:shadow-md md:border-2 md:p-6">
                    <div className="hidden md:flex justify-between items-center mb-4">
                        <h2 className="text-xl font-bold text-[#003B93]">
                            Vacinas Cadastrados
                        </h2>
                        <Button
                            asChild
                            className="!bg-[#0D4F97] !hover:bg-[#0b427d] text-white"
                        >
                            <Link href="/vacinas/new">Adicionar</Link>
                        </Button>
                    </div>

                    {/* para mobile */}
                    <div className="mb-4 md:hidden">
                        <h2 className="text-xl font-bold text-[#003B93]">
                            Vacinas Cadastrados
                        </h2>
                    </div>

                    <VaccinesList />
                </section>
            </main>

            <Button
                asChild
                className="fixed bottom-6 right-6 h-[53px] w-[53px] rounded-full shadow-lg md:hidden bg-[#0D4F97] hover:bg-[#0b427d]"
            >
                <Link href="/vacinas/new">
                    <Plus className="h-7 w-7" />
                    <span className="sr-only">Adicionar Vacina</span>
                </Link>
            </Button>
        </div>
    );
}
