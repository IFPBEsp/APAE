'use client';
import { Avatar, AvatarImage } from "@/components/ui/avatar";
import { Button } from "@/components/ui/button";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import DocumentCategoriesCard from "@/lib/ui/DocumentCategoriesCard";
import { SquarePen } from "lucide-react";
import { useEffect, useState } from "react";

export default function PersonDetails({ id }: { id: string }) {
    const [pessoa, setPessoa] = useState<any>(null);

    useEffect(() => {
        fetch(`/api/pessoas/${id}`)
            .then((response) => response.json())
            .then((data) => setPessoa(data))
            .catch((err) => console.error(err));
    }, [id]);

    const handleCategoriaClick = (tipo: string) => {
        alert(`Você clicou na categoria: ${tipo}`);
    };

    return (
        <div className="flex flex-col items-center gap-y-4 w-full max-w-full mx-auto px-4 mt-6 mb-6">
            <Avatar className="h-50 w-50">
                <AvatarImage src="https://www.inspirali.com/app/uploads/elementor/thumbs/carreira-medica-qfi1h8l88d4mqwz9hh5787b2rmfbc72p06p4ro4d8g.jpeg" />
            </Avatar>
            <h3 className="font-baloo font-bold text-[#0D4F97] text-[24px]">{pessoa?.nomeCompleto}</h3>

            <DocumentCategoriesCard onClickCategoria={handleCategoriaClick} />

            <Card className="w-full relative font-nunito text-[#0D4F97]">
                <CardHeader>
                    <CardTitle className="font-semibold text-[18px] text-center">Dados pessoais</CardTitle>
                </CardHeader>
                <CardContent>
                    <Button variant="ghost" size="icon" className="absolute top-2 right-2 !bg-transparent !hover:bg-muted">
                        <SquarePen className="w-4 h-4 text-primary mb-5" />
                    </Button>
                    <p className="text-left">Contato: </p>
                    <p className="text-left">Data de nascimento: </p>
                    <p className="text-left">Registro de nascimento: </p>
                    <p className="text-left">Fl.s: </p>
                    <p className="text-left">Livro: </p>
                    <p className="text-left">RG: </p>
                    <p className="text-left">Data de emissão: </p>
                    <p className="text-left">Orgão emissor: </p>
                    <p className="text-left">CPF: </p>
                    <p className="text-left">Naturalidade: </p>
                    <p className="text-left">CNS: </p>
                    <p className="text-left">NIS: </p>
                    <p className="text-left">Data de cadastro: </p>
                </CardContent>
            </Card>

            <Card className="w-full relative font-nunito text-[#0D4F97]">
                <CardHeader>
                    <CardTitle className="font-semibold text-[18px] text-center">Dados residenciais</CardTitle>
                </CardHeader>
                <CardContent>
                    <Button variant="ghost" size="icon" className="absolute top-2 right-2 !bg-transparent !hover:bg-muted">
                        <SquarePen className="w-4 h-4 text-primary mb-5" />
                    </Button>
                    <p className="text-left">Endereço: </p>
                    <p className="text-left">Bairro: </p>
                    <p className="text-left">Cidade: </p>
                    <p className="text-left">Estado: </p>
                    <p className="text-left">CEP: </p>
                </CardContent>
            </Card>

            <Card className="w-full relative font-nunito text-[#0D4F97]">
                <CardHeader>
                    <CardTitle className="font-semibold text-[18px] text-center">Dados familiares</CardTitle>
                </CardHeader>
                <CardContent>
                    <Button variant="ghost" size="icon" className="absolute top-2 right-2 !bg-transparent !hover:bg-muted">
                        <SquarePen className="w-4 h-4 text-primary mb-5" />
                    </Button>
                    <p className="text-left">Nome do pai: </p>
                    <p className="text-left">Vivo: </p>
                    <p className="text-left">Profissão: </p>
                    <p className="text-left">RG: </p>
                    <p className="text-left">CPF: </p>
                    <p className="text-left">Nome da mãe: </p>
                    <p className="text-left">Profissão: </p>
                    <p className="text-left">RG: </p>
                    <p className="text-left">CPF: </p>
                    <p className="text-left">Possui BPC: </p>
                    <p className="text-left">Renda familiar: </p>
                    <p className="text-left">Outros responsáveis: </p>
                </CardContent>
            </Card>

            <Card className="w-full relative font-nunito text-[#0D4F97]">
                <CardHeader>
                    <CardTitle className="font-semibold text-[18px] text-center">Informações de saúde</CardTitle>
                </CardHeader>
                <CardContent>
                    <Button variant="ghost" size="icon" className="absolute top-2 right-2 !bg-transparent !hover:bg-muted">
                        <SquarePen className="w-4 h-4 text-primary mb-5" />
                    </Button>
                    <ul>Vacinas: </ul>
                    <ul>Doenças que já teve: </ul>
                    <ul>Alergias: </ul>
                    <p>Tipo de medicação que toma: </p>
                    <ul>Tipos de deficiências: </ul>
                    <ul>Tipos de atendimento: </ul>
                </CardContent>
            </Card>

            <Card className="w-full relative font-nunito text-[#0D4F97]">
                <CardHeader>
                    <CardTitle className="font-semibold text-[18px] text-center">Em caso de emergência a quem procurar e onde?</CardTitle>
                </CardHeader>
                <CardContent>
                    <Button variant="ghost" size="icon" className="absolute top-2 right-2 !bg-transparent !hover:bg-muted">
                        <SquarePen className="w-4 h-4 text-primary mb-5" />
                    </Button>
                </CardContent>
            </Card>
        </div>
    );
}