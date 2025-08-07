'use client';
import { Avatar, AvatarFallback, AvatarImage } from "@/components/ui/avatar";
import { Button } from "@/components/ui/button";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import DocumentCategoriesCard from "@/lib/ui/DocumentCategoriesCard";
import { SquarePen } from "lucide-react";

export default function PersonDetails() {

    const handleCategoriaClick = (tipo: string) => {
        alert(`Você clicou na categoria: ${tipo}`);
    };

    return (
        <div className="flex flex-col items-center gap-y-4 w-full max-w-[500px] mx-auto px-4 mt-6 mb-6">
            <Avatar className="h-50 w-50">
                <AvatarImage src="https://www.inspirali.com/app/uploads/elementor/thumbs/carreira-medica-qfi1h8l88d4mqwz9hh5787b2rmfbc72p06p4ro4d8g.jpeg" />
            </Avatar>
            <h3 className="font-bold text-[#0D4F97]">Maria Eduarda Souto da Costa</h3>

            <DocumentCategoriesCard onClickCategoria={handleCategoriaClick} />

            <Card className="w-full max-w-[500px] relative">
                <CardHeader>
                    <CardTitle className="text-[#0D4F97] text-center">Dados pessoais</CardTitle>
                </CardHeader>
                <CardContent>
                    <Button variant="ghost" size="icon" className="absolute top-2 right-2 !bg-transparent !hover:bg-muted">
                        <SquarePen className="w-4 h-4 text-primary" />
                    </Button>
                </CardContent>
            </Card>

            <Card className="w-full max-w-[500px] relative">
                <CardHeader>
                    <CardTitle className="text-[#0D4F97] text-center">Dados residenciais</CardTitle>
                </CardHeader>
                <CardContent>
                    <Button variant="ghost" size="icon" className="absolute top-2 right-2 !bg-transparent !hover:bg-muted">
                        <SquarePen className="w-4 h-4 text-primary" />
                    </Button>
                </CardContent>
            </Card>

            <Card className="w-full max-w-[500px] relative">
                <CardHeader>
                    <CardTitle className="text-[#0D4F97] text-center">Dados familiares</CardTitle>
                </CardHeader>
                <CardContent>
                    <Button variant="ghost" size="icon" className="absolute top-2 right-2 !bg-transparent !hover:bg-muted">
                        <SquarePen className="w-4 h-4 text-primary" />
                    </Button>
                </CardContent>
            </Card>

            <Card className="w-full max-w-[500px] relative">
                <CardHeader>
                    <CardTitle className="text-[#0D4F97] text-center">Informações de saúde</CardTitle>
                </CardHeader>
                <CardContent>
                    <Button variant="ghost" size="icon" className="absolute top-2 right-2 !bg-transparent !hover:bg-muted">
                        <SquarePen className="w-4 h-4 text-primary" />
                    </Button>
                </CardContent>
            </Card>

            <Card className="w-full max-w-[500px] relative">
                <CardHeader>
                    <CardTitle className="text-[#0D4F97] text-center">Em caso de emergência a quem procurar e onde?</CardTitle>
                </CardHeader>
                <CardContent>
                    <Button variant="ghost" size="icon" className="absolute top-2 right-2 !bg-transparent !hover:bg-muted">
                        <SquarePen className="w-4 h-4 text-primary" />
                    </Button>
                </CardContent>
            </Card>
        </div>
    );
}