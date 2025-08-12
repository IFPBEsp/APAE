import React from "react";
import { PessoaRequest } from "../service/pessoaService";
import { Label } from "@/components/ui/label";
import { Input } from "@/components/ui/input";
import { Button } from "@/components/ui/button";
import {
  Card,
  CardContent,
  CardDescription,
  CardHeader,
  CardTitle,
} from "@/components/ui/card";
import { Checkbox } from "@/components/ui/checkbox";
import { Textarea } from "@/components/ui/textarea";

interface Props {
  data: PessoaRequest;
  setData: React.Dispatch<React.SetStateAction<PessoaRequest>>;
  addFile: (key: string, file: File, category: string, type: string) => void;
  nextStep: () => void;
  prevStep: () => void;
}

export default function CadastroDois({
  data,
  setData,
  addFile,
  nextStep,
  prevStep,
}: Props) {
  const handleChange = (
    e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement>
  ) => {
    const { name, value } = e.target;
    setData((prev) => ({
      ...prev,
      cadastrosAnuaisRequests: [
        {
          ...prev.cadastrosAnuaisRequests[0],
          [name]: value,
          beneficioDePrestacaoContinuada: false,
        },
      ],
    }));
  };

  const handleBeneficioChange = (checked: boolean) => {
    setData((prev) => {
      const firstCadastro = prev.cadastrosAnuaisRequests?.[0] || {};
      return {
        ...prev,
        cadastrosAnuaisRequests: [
          {
            ...firstCadastro,
            beneficioDePrestacaoContinuada: checked,
          },
        ],
      };
    });
  };

  const handleAddVacina = () => {
    setData((prev) => ({
      ...prev,
      vacinacoesRequests: [
        ...prev.vacinacoesRequests,
        { nome: "", dataAplicacao: "" },
      ],
    }));
  };

  const handleVacinaChange = (
    index: number,
    e: React.ChangeEvent<HTMLInputElement>
  ) => {
    const { name, value } = e.target;
    const updatedVacinas = [...data.vacinacoesRequests];
    updatedVacinas[index] = { ...updatedVacinas[index], [name]: value };
    setData((prev) => ({ ...prev, vacinacoesRequests: updatedVacinas }));
  };

  const handleAddDeficiencia = () => {
    setData((prev) => ({
      ...prev,
      deficienciasRequests: [...prev.deficienciasRequests, { descricao: "" }],
    }));
  };

  const handleDeficienciaChange = (
    index: number,
    e: React.ChangeEvent<HTMLInputElement>
  ) => {
    const { value } = e.target;
    const updatedDeficiencias = [...data.deficienciasRequests];
    updatedDeficiencias[index] = { descricao: value };
    setData((prev) => ({ ...prev, deficienciasRequests: updatedDeficiencias }));
  };

  const handleAddAtendimento = () => {
    setData((prev) => ({
      ...prev,
      atendimentosRequests: [...prev.atendimentosRequests, { descricao: "" }],
    }));
  };

  const handleAtendimentoChange = (
    index: number,
    e: React.ChangeEvent<HTMLInputElement>
  ) => {
    const { value } = e.target;
    const updatedAtendimentos = [...data.atendimentosRequests];
    updatedAtendimentos[index] = { descricao: value };
    setData((prev) => ({ ...prev, atendimentosRequests: updatedAtendimentos }));
  };

  return (
    <Card className="w-full max-w-4xl mx-auto">
      <CardHeader>
        <CardTitle>Passo 2: Saúde e Documentos</CardTitle>
        <CardDescription>
          Informações sobre o histórico de saúde, deficiências e atendimentos.
        </CardDescription>
      </CardHeader>
      <CardContent className="space-y-8">
        {/* Seção de Histórico de Saúde */}
        <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
          <div className="grid w-full gap-1.5">
            <Label htmlFor="historicoDoencas">Doenças que já teve</Label>
            <Textarea
              id="historicoDoencas"
              name="historicoDoencas"
              value={data.cadastrosAnuaisRequests[0]?.historicoDoencas || ""}
              onChange={handleChange}
            />
          </div>
          <div className="grid w-full gap-1.5">
            <Label htmlFor="medicacoesContinuas">Medicação contínua</Label>
            <Textarea
              id="medicacoesContinuas"
              name="medicacoesContinuas"
              value={data.cadastrosAnuaisRequests[0]?.medicacoesContinuas || ""}
              onChange={handleChange}
            />
          </div>
          <div className="grid w-full gap-1.5">
            <Label htmlFor="historicosAlergias">Alergias</Label>
            <Textarea
              id="historicosAlergias"
              name="historicosAlergias"
              value={data.cadastrosAnuaisRequests[0]?.historicosAlergias || ""}
              onChange={handleChange}
            />
          </div>
        </div>

        {/* Seções Dinâmicas */}
        <div className="grid grid-cols-1 lg:grid-cols-3 gap-8">
          {/* Vacinas */}
          <div className="space-y-4">
            <h3 className="font-medium">Vacinas Tomadas</h3>
            {data.vacinacoesRequests.map((vacina, index) => (
              <div key={index} className="flex items-center gap-2">
                <Input
                  name="nome"
                  placeholder="Nome da Vacina"
                  value={vacina.nome}
                  onChange={(e) => handleVacinaChange(index, e)}
                />
                <Input
                  name="dataAplicacao"
                  type="date"
                  value={vacina.dataAplicacao}
                  onChange={(e) => handleVacinaChange(index, e)}
                />
              </div>
            ))}
            <Button
              type="button"
              variant="outline"
              size="sm"
              onClick={handleAddVacina}
            >
              Adicionar Vacina
            </Button>
          </div>

          {/* Deficiências */}
          <div className="space-y-4">
            <h3 className="font-medium">Tipos de Deficiência</h3>
            {data.deficienciasRequests.map((def, index) => (
              <div key={index}>
                <Input
                  placeholder="Descrição da deficiência"
                  value={def.descricao}
                  onChange={(e) => handleDeficienciaChange(index, e)}
                />
              </div>
            ))}
            <Button
              type="button"
              variant="outline"
              size="sm"
              onClick={handleAddDeficiencia}
            >
              Adicionar Deficiência
            </Button>
          </div>

          {/* Atendimentos */}
          <div className="space-y-4">
            <h3 className="font-medium">Tipos de Atendimento</h3>
            {data.atendimentosRequests.map((at, index) => (
              <div key={index}>
                <Input
                  placeholder="Descrição do atendimento"
                  value={at.descricao}
                  onChange={(e) => handleAtendimentoChange(index, e)}
                />
              </div>
            ))}
            <Button
              type="button"
              variant="outline"
              size="sm"
              onClick={handleAddAtendimento}
            >
              Adicionar Atendimento
            </Button>
          </div>
        </div>

        {/* Checkbox BPC */}
        <div className="items-top flex space-x-2">
          <Checkbox
            id="bpc"
            checked={
              data.cadastrosAnuaisRequests[0]?.beneficioDePrestacaoContinuada ||
              false
            }
            onCheckedChange={(checked) =>
              handleBeneficioChange(Boolean(checked))
            }
          />
          <div className="grid gap-1.5 leading-none">
            <label
              htmlFor="bpc"
              className="text-sm font-medium leading-none peer-disabled:cursor-not-allowed peer-disabled:opacity-70"
            >
              Possui benefício de prestação continuada (BPC)?
            </label>
          </div>
        </div>

        {/* Documentos */}
        <div>
          <h3 className="font-medium mb-4">Documentos</h3>
          <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
            <div className="grid w-full max-w-sm items-center gap-1.5">
              <Label htmlFor="laudo">Enviar Laudo Médico</Label>
              <Input
                id="laudo"
                type="file"
                onChange={(e) =>
                  e.target.files &&
                  addFile("laudo", e.target.files[0], "MEDICO", "LAUDO")
                }
              />
            </div>
            <div className="grid w-full max-w-sm items-center gap-1.5">
              <Label htmlFor="encaminhamento">Enviar Encaminhamento</Label>
              <Input
                id="encaminhamento"
                type="file"
                onChange={(e) =>
                  e.target.files &&
                  addFile(
                    "encaminhamento",
                    e.target.files[0],
                    "MEDICO",
                    "ENCAMINHAMENTO"
                  )
                }
              />
            </div>
          </div>
        </div>

        {/* Navegação */}
        <div className="flex justify-between mt-8">
          <Button variant="outline" onClick={prevStep}>
            Anterior
          </Button>
          <Button onClick={nextStep}>Próximo</Button>
        </div>
      </CardContent>
    </Card>
  );
}
