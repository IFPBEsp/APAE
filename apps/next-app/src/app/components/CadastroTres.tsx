import { Button } from "@/components/ui/button";
import {
  Card,
  CardContent,
  CardDescription,
  CardHeader,
  CardTitle,
} from "@/components/ui/card";
import { Checkbox } from "@/components/ui/checkbox";
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
  nextStep: () => void;
  prevStep: () => void;
}

export default function CadastroTres({
  data,
  setData,
  nextStep,
  prevStep,
}: Props) {
  const handleAddResponsavel = () => {
    setData((prev) => ({
      ...prev,
      responsaveisRequests: [
        ...prev.responsaveisRequests,
        {
          nome: "",
          ondeProcurar: "",
          vivo: true,
          profissao: "",
          rg: "",
          cpf: "",
          emergencia: "",
          tipoResponsavel: "",
          bpc: false,
        },
      ],
    }));
  };

  const handleRemoveResponsavel = (indexToRemove: number) => {
    setData((prev) => ({
      ...prev,
      responsaveisRequests: prev.responsaveisRequests.filter(
        (_, index) => index !== indexToRemove
      ),
    }));
  };

  const handleResponsavelChange = (
    index: number,
    e: React.ChangeEvent<HTMLInputElement>
  ) => {
    const { name, value } = e.target;
    const updatedResponsaveis = [...data.responsaveisRequests];
    updatedResponsaveis[index] = {
      ...updatedResponsaveis[index],
      [name]: value,
    };
    setData((prev) => ({ ...prev, responsaveisRequests: updatedResponsaveis }));
  };

  const handleResponsavelCheckboxChange = (
    index: number,
    name: string,
    checked: boolean
  ) => {
    const updatedResponsaveis = [...data.responsaveisRequests];
    updatedResponsaveis[index] = {
      ...updatedResponsaveis[index],
      [name]: checked,
    };
    setData((prev) => ({ ...prev, responsaveisRequests: updatedResponsaveis }));
  };

  const handleResponsavelSelectChange = (index: number, value: string) => {
    const updatedResponsaveis = [...data.responsaveisRequests];
    updatedResponsaveis[index] = {
      ...updatedResponsaveis[index],
      tipoResponsavel: value,
    };
    setData((prev) => ({ ...prev, responsaveisRequests: updatedResponsaveis }));
  };

  const handleRendaFamiliarChange = (
    e: React.ChangeEvent<HTMLInputElement>
  ) => {
    const { value } = e.target;
    setData((prev) => {
      const updatedCadastros = [...(prev.cadastrosAnuaisRequests || [])];
      if (updatedCadastros.length === 0) {
        updatedCadastros.push({
          beneficioDePrestacaoContinuada: false,
          historicosAlergias: "",
          medicacoesContinuas: "",
          historicoDoencas: "",
          rendaFamiliar: 0,
        });
      }
      updatedCadastros[0].rendaFamiliar = parseFloat(value) || 0;
      return { ...prev, cadastrosAnuaisRequests: updatedCadastros };
    });
  };

  const rendaFamiliar = data.cadastrosAnuaisRequests?.[0]?.rendaFamiliar || 0;

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
            <CardTitle>
              Passo 3: Responsáveis e Renda Familiar
            </CardTitle>
            <CardDescription>
              Adicione os responsáveis pelo assistido e informe a renda
              familiar.
            </CardDescription>
          </CardHeader>

          <CardContent className="px-0 pb-0 space-y-8">
            {/* Renda Familiar */}
            <div className="grid w-full max-w-sm items-center gap-1.5">
              <Label htmlFor="rendaFamiliar">Renda Familiar (R$)</Label>
              <Input
                type="number"
                id="rendaFamiliar"
                name="rendaFamiliar"
                value={rendaFamiliar}
                onChange={handleRendaFamiliarChange}
              />
            </div>

            {/* Seção de Responsáveis */}
            <div className="space-y-6">
              <h3 className="text-lg font-medium">Dados dos Responsáveis</h3>
              {data.responsaveisRequests.map((resp, index) => (
                <div key={index} className="border-t pt-6 space-y-4">
                  <div className="flex justify-between items-center">
                    <h4 className="font-semibold">Responsável {index + 1}</h4>
                    <Button
                      variant="ghost"
                      size="sm"
                      onClick={() => handleRemoveResponsavel(index)}
                    >
                      Remover
                    </Button>
                  </div>

                  <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
                    <div className="grid w-full items-center gap-1.5">
                      <Label htmlFor={`nome-${index}`}>Nome</Label>
                      <Input
                        id={`nome-${index}`}
                        name="nome"
                        value={resp.nome}
                        onChange={(e) => handleResponsavelChange(index, e)}
                      />
                    </div>
                    <div className="grid w-full items-center gap-1.5">
                      <Label htmlFor={`cpf-${index}`}>CPF</Label>
                      <Input
                        id={`cpf-${index}`}
                        name="cpf"
                        value={resp.cpf}
                        onChange={(e) => handleResponsavelChange(index, e)}
                      />
                    </div>
                    <div className="grid w-full items-center gap-1.5">
                      <Label htmlFor={`rg-${index}`}>RG</Label>
                      <Input
                        id={`rg-${index}`}
                        name="rg"
                        value={resp.rg}
                        onChange={(e) => handleResponsavelChange(index, e)}
                      />
                    </div>
                    <div className="grid w-full items-center gap-1.5">
                      <Label htmlFor={`profissao-${index}`}>Profissão</Label>
                      <Input
                        id={`profissao-${index}`}
                        name="profissao"
                        value={resp.profissao}
                        onChange={(e) => handleResponsavelChange(index, e)}
                      />
                    </div>
                    <div className="grid w-full items-center gap-1.5">
                      <Label htmlFor={`tipoResponsavel-${index}`}>
                        Tipo de Responsável
                      </Label>
                      <Select
                        value={resp.tipoResponsavel}
                        onValueChange={(value) =>
                          handleResponsavelSelectChange(index, value)
                        }
                      >
                        <SelectTrigger id={`tipoResponsavel-${index}`}>
                          <SelectValue placeholder="Selecione..." />
                        </SelectTrigger>
                        <SelectContent>
                          <SelectItem value="PAI">Pai</SelectItem>
                          <SelectItem value="MAE">Mãe</SelectItem>
                          <SelectItem value="AVO_A">Avô/Avó</SelectItem>
                          <SelectItem value="TIO_A">Tio/Tia</SelectItem>
                          <SelectItem value="IRMAO_A">Irmão/Irmã</SelectItem>
                          <SelectItem value="OUTRO">Outro</SelectItem>
                        </SelectContent>
                      </Select>
                    </div>
                    <div className="flex flex-row items-center justify-start space-x-2 pt-6">
                      <Checkbox
                        id={`vivo-${index}`}
                        name="vivo"
                        checked={resp.vivo}
                        onCheckedChange={(checked) =>
                          handleResponsavelCheckboxChange(
                            index,
                            "vivo",
                            Boolean(checked)
                          )
                        }
                      />
                      <Label htmlFor={`vivo-${index}`}>Vivo(a)</Label>
                    </div>
                  </div>
                </div>
              ))}
              <Button variant="outline" onClick={handleAddResponsavel}>
                Adicionar Responsável
              </Button>
            </div>

            {/* Navegação */}
            <div className="flex justify-between mt-8">
              <Button variant="outline" onClick={prevStep}>
                Anterior
              </Button>
              <Button onClick={nextStep}>Próximo</Button>
            </div>
          </CardContent>
        </div>
      </Card>
    </div>
  );
}
