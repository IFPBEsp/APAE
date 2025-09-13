'use client';
import { Avatar, AvatarImage } from "@/components/ui/avatar";
import { Button } from "@/components/ui/button";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import DocumentCategoriesCard from "@/lib/ui/DocumentCategoriesCard";
import { SquarePen } from "lucide-react";
import Link from "next/link";
import { useParams } from "next/navigation";
import { useEffect, useState } from "react";

export default function PersonDetails() {
    const params = useParams();
    const [pessoa, setPessoa] = useState<any>(null);

    useEffect(() => {
        if(!params?.id) { return; }

        fetch(`/api/pessoas/${params.id}`)
            .then((response) => response.json())
            .then((data) => setPessoa(data))
            .catch((err) => console.error(err));
    }, [params?.id]);

    const handleCategoriaClick = (tipo: string) => {
        alert(`Você clicou na categoria: ${tipo}`);
    };

    return (
        <div className="flex flex-col items-center gap-y-4 w-full max-w-full mx-auto px-4 mt-6 mb-6">
            <Avatar className="h-50 w-50">
                {/* Não consegui fazer da mesma forma que está na tela inicial */}
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
                        <Link href="/">
                            <SquarePen className="w-4 h-4 text-primary mb-5" />
                        </Link>
                    </Button>
                    <p className="text-left">Contato: <span className="text-[#000000]">{pessoa?.telefone}</span></p>
                    <p className="text-left">Data de nascimento: <span className="text-[#000000]">{pessoa?.dataNascimento}</span></p>
                    <p className="text-left">Registro de nascimento: <span className="text-[#000000]">{pessoa?.numRegistroNasc}</span></p>
                    <p className="text-left">Fl.s: <span className="text-[#000000]">{pessoa?.fls}</span></p>
                    <p className="text-left">Livro: <span className="text-[#000000]">{pessoa?.livro}</span></p>
                    <p className="text-left">RG: <span className="text-[#000000]">{pessoa?.rg}</span></p>
                    <p className="text-left">Data de emissão: <span className="text-[#000000]">{pessoa?.dataEmissaoRg}</span></p>
                    <p className="text-left">Orgão emissor: <span className="text-[#000000]">{pessoa?.orgaoEmissorRg}</span></p>
                    <p className="text-left">CPF: <span className="text-[#000000]">{pessoa?.cpf}</span></p>
                    <p className="text-left">Naturalidade: <span className="text-[#000000]">{pessoa?.naturalidade}</span></p>
                    <p className="text-left">CNS: <span className="text-[#000000]">{pessoa?.cns}</span></p>
                    <p className="text-left">NIS: <span className="text-[#000000]">{pessoa?.nis}</span></p>
                    <p className="text-left">Data de cadastro: <span className="text-[#000000]">{pessoa?.dataCadastramento}</span></p>
                </CardContent>
            </Card>

            <Card className="w-full relative font-nunito text-[#0D4F97]">
                <CardHeader>
                    <CardTitle className="font-semibold text-[18px] text-center">Dados residenciais</CardTitle>
                </CardHeader>
                <CardContent>
                    <Button variant="ghost" size="icon" className="absolute top-2 right-2 !bg-transparent !hover:bg-muted">
                        <Link href="/">
                            <SquarePen className="w-4 h-4 text-primary mb-5" />
                        </Link>
                    </Button>
                    <p className="text-left">Endereço: <span className="text-[#000000]">{pessoa?.endereco}</span></p>
                    <p className="text-left">Bairro: <span className="text-[#000000]">{pessoa?.bairro}</span></p>
                    <p className="text-left">Cidade: <span className="text-[#000000]">{pessoa?.cidade}</span></p>
                    <p className="text-left">Estado: <span className="text-[#000000]">{pessoa?.estado}</span></p>
                    <p className="text-left">CEP: <span className="text-[#000000]">{pessoa?.cep}</span></p>
                </CardContent>
            </Card>

            <Card className="w-full relative font-nunito text-[#0D4F97]">
                <CardHeader>
                    <CardTitle className="font-semibold text-[18px] text-center">Dados familiares</CardTitle>
                </CardHeader>
                <CardContent>
                    <Button variant="ghost" size="icon" className="absolute top-2 right-2 !bg-transparent !hover:bg-muted">
                        <Link href="/">
                            <SquarePen className="w-4 h-4 text-primary mb-5" />
                        </Link>
                    </Button>
                    {/* No backend não tem o nome do responsável */}
                    <p className="text-left">Nome do pai: <span className="text-[#000000]">{pessoa?.pai?.nome}</span></p>
                    <p className="text-left">Vivo: <span className="text-[#000000]">{pessoa?.pai?.vivo}</span></p>
                    <p className="text-left">Profissão: <span className="text-[#000000]">{pessoa?.pai?.profissao}</span></p>
                    <p className="text-left">RG: <span className="text-[#000000]">{pessoa?.pai?.rg}</span></p>
                    <p className="text-left">CPF: <span className="text-[#000000]">{pessoa?.pai?.cpf}</span></p>
                    {/* No backend não tem o nome do responsável */}
                    <p className="text-left">Nome da mãe: <span className="text-[#000000]">{pessoa?.mae?.nome}</span></p>
                    <p className="text-left">Vivo: <span className="text-[#000000]">{pessoa?.mae?.vivo}</span></p>
                    <p className="text-left">Profissão: <span className="text-[#000000]">{pessoa?.mae?.profissao}</span></p>
                    <p className="text-left">RG: <span className="text-[#000000]">{pessoa?.mae?.rg}</span></p>
                    <p className="text-left">CPF: <span className="text-[#000000]">{pessoa?.mae?.cpf}</span></p>
                    <p className="text-left">Possui BPC: <span className="text-[#000000]">{pessoa?.cadastroAnual?.possuiBpc}</span></p>
                    <p className="text-left">Renda familiar: <span className="text-[#000000]">{pessoa?.cadastroAnual?.rendaFamiliar}</span></p>
                    {/* Precisa adicionar uma lógica para quando tiver mais responsáveis, aqui ainda está estático */}
                    <p className="text-left">Outros responsáveis: <span className="text-[#000000]"></span></p>
                </CardContent>
            </Card>

            <Card className="w-full relative font-nunito text-[#0D4F97]">
                <CardHeader>
                    <CardTitle className="font-semibold text-[18px] text-center">Informações de saúde</CardTitle>
                </CardHeader>
                <CardContent>
                    <Button variant="ghost" size="icon" className="absolute top-2 right-2 !bg-transparent !hover:bg-muted">
                        <Link href="/">
                            <SquarePen className="w-4 h-4 text-primary mb-5" />
                        </Link>
                    </Button>
                    <p>Vacinas: <span className="text-[#000000]">{pessoa?.vacinas?.length ? pessoa.vacinas.map((v: any) => `${v.nome} (${v.dataAplicacao})`).join(", ") : "Não informado"}</span></p>
                    <p>Doenças que já teve: <span className="text-[#000000]">{pessoa?.cadastroAnual?.doencas}</span></p>
                    <p>Alergias: <span className="text-[#000000]">{pessoa?.cadastroAnual?.alergias}</span></p>
                    <p>Tipo de medicação que toma: <span className="text-[#000000]">{pessoa?.cadastroAnual?.medicacao}</span></p>
                    <p>Tipos de deficiências: <span className="text-[#000000]">{pessoa?.deficiencias?.length ? pessoa.deficiencias.map((d: any) => d.descricao).join(", ") : "Não informado"}</span></p>
                    <p>Tipos de atendimento: <span className="text-[#000000]">{pessoa?.atendimentos?.length ? pessoa.atendimentos.map((a: any) => a.descricao).join(", ") : "Não informado"}</span></p>
                </CardContent>
            </Card>

            <Card className="w-full relative font-nunito text-[#0D4F97]">
                <CardHeader>
                    <CardTitle className="font-semibold text-[18px] text-center">Em caso de emergência a quem procurar e onde?</CardTitle>
                </CardHeader>
                <CardContent>
                    <Button variant="ghost" size="icon" className="absolute top-2 right-2 !bg-transparent !hover:bg-muted">
                        <Link href="/">
                            <SquarePen className="w-4 h-4 text-primary mb-5" />
                        </Link>
                    </Button>
                    {/* No backend não tem o nome do responsável */}
                    <p>Quem procurar: <span className="text-[#000000]">{pessoa?.mae?.nome}</span></p>
                    <p>Onde encontrar: <span className="text-[#000000]">{pessoa?.mae?.ondeProcurar}</span></p>
                    <p>Número para contato: <span className="text-[#000000]">{pessoa?.mae?.emergencia}</span></p>
                </CardContent>
            </Card>
        </div>
    );
}