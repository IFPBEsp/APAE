import { Button } from "@/components/ui/button";
import {
  Card,
  CardContent,
  CardDescription,
  CardHeader,
  CardTitle,
} from "@/components/ui/card";
import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from "@/components/ui/select";
import apae from "../images/apae.png";

import React from "react";
import { PessoaRequest } from "../service/pessoaService";

interface Props {
  data: PessoaRequest;
  setData: React.Dispatch<React.SetStateAction<PessoaRequest>>;
  addFile: (key: string, file: File, category: string, type: string) => void;
  handleSubmit: () => void;
  prevStep: () => void;
}

export default function CadastroQuatro({
  data,
  setData,
  addFile,
  handleSubmit,
  prevStep,
}: Props) {
  const handleFuncaoSelectChange = (value: string) => {
    setData((prev) => ({
      ...prev,
      funcao: value as "Aluno" | "Paciente" | "Ambos",
    }));
  };

  const handleDataCadastroChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setData((prev) => ({ ...prev, dataCadastramento: e.target.value }));
  };

  const today = new Date().toISOString().split("T")[0];

  return (
    <div className="flex min-h-screen w-full items-center justify-center p-4">
      <Card className="w-full min-h-screen max-w-6xl overflow-hidden shadow-2xl md:grid md:grid-cols-5 p-0">
        {/* Coluna da Imagem */}
        <div className="hidden md:col-span-2 md:block">
          <img
            src={apae.src}
            alt="Imagem de fundo do formulário"
            className="h-full w-full object-cover"
          />
        </div>

        {/* Conteúdo */}
        <div className="p-8 md:col-span-3">
          <CardHeader className="px-0 pt-0">
            <CardTitle>Passo 4: Informações Adicionais e Conclusão</CardTitle>
            <CardDescription>
              Revise os últimos detalhes e finalize o cadastro.
            </CardDescription>
          </CardHeader>

          <CardContent className="px-0 pb-0 space-y-8">
            {/* Campos do Formulário */}
            <div className="space-y-6">
              <div className="grid w-full items-center gap-1.5">
                <Label htmlFor="dataCadastramento">Data de Cadastro</Label>
                <Input
                  id="dataCadastramento"
                  name="dataCadastramento"
                  type="date"
                  value={data.dataCadastramento || today}
                  onChange={handleDataCadastroChange}
                />
              </div>

              <div className="grid w-full items-center gap-1.5">
                <Label htmlFor="funcao">Função na Instituição</Label>
                <Select
                  value={data.funcao || ""}
                  onValueChange={handleFuncaoSelectChange}
                >
                  <SelectTrigger id="funcao">
                    <SelectValue placeholder="Selecione uma função..." />
                  </SelectTrigger>
                  <SelectContent>
                    <SelectItem value="Aluno">Aluno</SelectItem>
                    <SelectItem value="Paciente">Paciente</SelectItem>
                    <SelectItem value="Ambos">Ambos</SelectItem>
                  </SelectContent>
                </Select>
              </div>

              <div className="grid w-full items-center gap-1.5">
                <Label htmlFor="foto">Adicionar uma Foto 3x4</Label>
                <Input
                  id="foto"
                  type="file"
                  accept="image/*"
                  onChange={(e) =>
                    e.target.files &&
                    addFile("foto", e.target.files[0], "PESSOAL", "FOTO")
                  }
                />
              </div>
            </div>

            {/* Navegação */}
            <div className="flex justify-between pt-6">
              <Button variant="outline" onClick={prevStep}>
                Anterior
              </Button>
              <Button onClick={handleSubmit}>Concluir e Enviar Cadastro</Button>
            </div>
          </CardContent>
        </div>
      </Card>
    </div>
  );
}
