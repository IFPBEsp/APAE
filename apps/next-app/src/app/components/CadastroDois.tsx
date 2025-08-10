import React, { useRef } from "react";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";
import { Checkbox } from "@/components/ui/checkbox";
import {
  PlusCircle,
  Trash2,
  ArrowRight,
  FileCheck2,
  Paperclip,
} from "lucide-react";

import apae from "../images/apae.png";
import { PessoaResponsavelRequest } from "../service/pessoaService";

export interface VacinaRequest {
  nome: string;
  dataAplicacao: string;
}
export interface DeficienciaComArquivo {
  descricao: string;
  file?: File;
}
export interface AtendimentoComArquivo {
  descricao: string;
  file?: File;
}
export interface CadastroAnualRequest {
  beneficioDePrestacaoContinuada: boolean;
  historicosAlergias: string;
  medicacoesContinuas: string;
  historicoDoencas: string;
  rendaFamiliar: number;
}

export interface FullFormData {
  responsaveisRequests: PessoaResponsavelRequest[];
  vacinacoesRequests: VacinaRequest[];
  deficienciasRequests: DeficienciaComArquivo[];
  atendimentosRequests: AtendimentoComArquivo[];
  cadastrosAnuaisRequests: CadastroAnualRequest[];
}

interface CadastroDoisProps {
  onNext: () => void;
  onBack: () => void;
  formData: FullFormData;
  setFormData: React.Dispatch<React.SetStateAction<FullFormData>>;
}

export default function CadastroDois({
  onNext,
  onBack,
  formData,
  setFormData,
}: CadastroDoisProps) {
  const deficiencyFileInputRefs = useRef<(HTMLInputElement | null)[]>([]);
  const attendanceFileInputRefs = useRef<(HTMLInputElement | null)[]>([]);

  /**
   * @param category
   * @param index
   * @param field
   * @param value
   */
  const handleArrayChange = (
    category: keyof FullFormData,
    index: number,
    field: string,
    value: string | number | boolean | File | undefined
  ) => {
    const updatedArray = [...formData[category]];

    const updatedItem = { ...updatedArray[index], [field]: value };

    updatedArray[index] = updatedItem;

    setFormData({ ...formData, [category]: updatedArray });
  };

  /**
   * @param category
   */
  const addItem = (
    category:
      | "vacinacoesRequests"
      | "deficienciasRequests"
      | "atendimentosRequests"
  ) => {
    let newItem;
    switch (category) {
      case "vacinacoesRequests":
        newItem = { nome: "", dataAplicacao: "" };
        break;
      case "deficienciasRequests":
        newItem = { descricao: "", file: undefined };
        break;
      case "atendimentosRequests":
        newItem = { descricao: "", file: undefined };
        break;
      default:
        return;
    }

    setFormData((prev) => ({
      ...prev,
      [category]: [...prev[category], newItem],
    }));
  };

  /**
   * @param category
   * @param index
   */
  const removeItem = (
    category:
      | "vacinacoesRequests"
      | "deficienciasRequests"
      | "atendimentosRequests",
    index: number
  ) => {
    setFormData((prev) => {
      const currentArray = prev[category];

      if (currentArray.length <= 1) return prev;

      const updatedArray = currentArray.filter((_, i) => i !== index);
      return { ...prev, [category]: updatedArray };
    });
  };

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    onNext();
  };

  return (
    <div className="min-h-screen w-full lg:grid lg:grid-cols-12 font-sans overflow-hidden">
      <div className="hidden lg:col-span-5 lg:relative lg:flex">
        <img
          src={apae.src}
          alt="Group of people"
          className="absolute inset-0 h-full w-full object-cover"
        />
        <div className="relative z-10 flex w-full flex-col items-center justify-center bg-gradient-to-t from-white/30 to-transparent text-white space-y-6 text-center">
          <h1 className="text-5xl font-bold tracking-tight">BEM-VINDO</h1>
          <p className="text-xl max-w-sm">
            Informe seus dados ao lado para poder fazer parte da nossa
            associação.
          </p>
        </div>
        <div className="absolute z-20 top-1/2 -right-7 -translate-y-1/2">
          <button className="h-14 w-14 bg-blue-700 rounded-full flex items-center justify-center text-white hover:bg-blue-800 transition-colors">
            <ArrowRight className="h-6 w-6" />
          </button>
        </div>
      </div>

      <div className="lg:col-span-7 flex items-center justify-center py-12 px-4 sm:px-6 lg:px-16 bg-gray-50">
        <div className="w-full max-w-3xl space-y-8">
          <h2 className="text-2xl font-bold text-gray-800">
            Cadastro de pessoas - Etapa 2
          </h2>
          <form onSubmit={handleSubmit} className="space-y-8">
            <div>
              <h3 className="text-lg font-semibold text-blue-600 mb-4">
                Vacinas
              </h3>
              <div className="space-y-4">
                {formData.vacinacoesRequests.map((vacina, idx) => (
                  <div
                    key={idx}
                    className="p-4 border rounded-lg space-y-4 relative bg-white"
                  >
                    {formData.vacinacoesRequests.length > 1 && (
                      <Button
                        variant="ghost"
                        size="icon"
                        className="absolute top-2 right-2 text-gray-400 hover:text-red-500"
                        onClick={() => removeItem("vacinacoesRequests", idx)}
                      >
                        <Trash2 className="h-4 w-4" />
                      </Button>
                    )}
                    <div className="grid grid-cols-1 md:grid-cols-2 gap-x-8 gap-y-6">
                      <div className="space-y-2">
                        <Label htmlFor={`vacina-nome-${idx}`}>
                          Nome da Vacina
                        </Label>
                        <Input
                          id={`vacina-nome-${idx}`}
                          type="text"
                          placeholder="Ex: Febre Amarela"
                          value={vacina.nome}
                          onChange={(e) =>
                            handleArrayChange(
                              "vacinacoesRequests",
                              idx,
                              "nome",
                              e.target.value
                            )
                          }
                        />
                      </div>
                      <div className="space-y-2">
                        <Label htmlFor={`vacina-data-${idx}`}>
                          Data de Aplicação
                        </Label>
                        <Input
                          id={`vacina-data-${idx}`}
                          type="date"
                          value={vacina.dataAplicacao}
                          onChange={(e) =>
                            handleArrayChange(
                              "vacinacoesRequests",
                              idx,
                              "dataAplicacao",
                              e.target.value
                            )
                          }
                        />
                      </div>
                    </div>
                  </div>
                ))}
                <Button
                  type="button"
                  variant="outline"
                  onClick={() => addItem("vacinacoesRequests")}
                >
                  <PlusCircle className="mr-2 h-4 w-4" /> Adicionar Vacina
                </Button>
              </div>
            </div>

            <div>
              <h3 className="text-lg font-semibold text-blue-600 mb-4">
                Deficiências
              </h3>
              <div className="space-y-4">
                {formData.deficienciasRequests.map((def, idx) => (
                  <div
                    key={idx}
                    className="p-4 border rounded-lg space-y-2 relative bg-white"
                  >
                    {formData.deficienciasRequests.length > 1 && (
                      <Button
                        variant="ghost"
                        size="icon"
                        className="absolute top-2 right-2 text-gray-400 hover:text-red-500"
                        onClick={() => removeItem("deficienciasRequests", idx)}
                      >
                        <Trash2 className="h-4 w-4" />
                      </Button>
                    )}
                    <Label htmlFor={`deficiencia-desc-${idx}`}>
                      Tipo de Deficiência (Laudo)
                    </Label>
                    <div className="flex flex-wrap items-center gap-4">
                      <Input
                        id={`deficiencia-desc-${idx}`}
                        placeholder="Ex: Laudo de Autismo"
                        value={def.descricao}
                        onChange={(e) =>
                          handleArrayChange(
                            "deficienciasRequests",
                            idx,
                            "descricao",
                            e.target.value
                          )
                        }
                        className="w-full md:flex-1"
                      />
                      <input
                      type="file"
                      onChange={e => {
                        const file = e.target.files?.[0];
                        if (file) {
                          handleArrayChange("deficienciasRequests", idx, "file", file);
                        }
                      }}
                    />
                      <Button
                        type="button"
                        variant={def.file ? "default" : "secondary"}
                        className={`w-full md:w-48 justify-center ${
                          def.file
                            ? "bg-green-600 hover:bg-green-700"
                            : "bg-blue-900 text-white"
                        }`}
                        onClick={() =>
                          deficiencyFileInputRefs.current[idx]?.click()
                        }
                      >
                        {def.file ? (
                          <>
                            <FileCheck2 className="mr-2 h-4 w-4" /> Arquivo
                            Anexado
                          </>
                        ) : (
                          <>
                            <Paperclip className="mr-2 h-4 w-4" /> Anexar Laudo
                          </>
                        )}
                      </Button>
                    </div>
                    {def.file && (
                      <p className="text-xs text-gray-500 mt-1 truncate">
                        Arquivo: {def.file.name}
                      </p>
                    )}
                  </div>
                ))}
                <Button
                  type="button"
                  variant="outline"
                  onClick={() => addItem("deficienciasRequests")}
                >
                  <PlusCircle className="mr-2 h-4 w-4" /> Adicionar Deficiência
                </Button>
              </div>
            </div>

            <div>
              <h3 className="text-lg font-semibold text-blue-600 mb-4">
                Atendimentos
              </h3>
              <div className="space-y-4">
                {formData.atendimentosRequests.map((at, idx) => (
                  <div
                    key={idx}
                    className="p-4 border rounded-lg space-y-2 relative bg-white"
                  >
                    {formData.atendimentosRequests.length > 1 && (
                      <Button
                        variant="ghost"
                        size="icon"
                        className="absolute top-2 right-2 text-gray-400 hover:text-red-500"
                        onClick={() => removeItem("atendimentosRequests", idx)}
                      >
                        <Trash2 className="h-4 w-4" />
                      </Button>
                    )}
                    <Label htmlFor={`atendimento-desc-${idx}`}>
                      Tipo de Atendimento (Encaminhamento)
                    </Label>
                    <div className="flex flex-wrap items-center gap-4">
                      <Input
                        id={`atendimento-desc-${idx}`}
                        type="text"
                        placeholder="Ex: Fisioterapia"
                        value={at.descricao}
                        onChange={(e) =>
                          handleArrayChange(
                            "atendimentosRequests",
                            idx,
                            "descricao",
                            e.target.value
                          )
                        }
                        className="w-full md:flex-1"
                      />
                      <input
                        type="file"
                        ref={(el) => {
                          attendanceFileInputRefs.current[idx] = el;
                        }}
                        onChange={(e) =>
                          handleArrayChange(
                            "atendimentosRequests",
                            idx,
                            "file",
                            e.target.files?.[0]
                          )
                        }
                        className="hidden"
                        accept="image/*,.pdf"
                      />
                      <Button
                        type="button"
                        variant={at.file ? "default" : "secondary"}
                        className={`w-full md:w-48 justify-center ${
                          at.file
                            ? "bg-green-600 hover:bg-green-700"
                            : "bg-blue-900 text-white"
                        }`}
                        onClick={() =>
                          attendanceFileInputRefs.current[idx]?.click()
                        }
                      >
                        {at.file ? (
                          <>
                            <FileCheck2 className="mr-2 h-4 w-4" /> Arquivo
                            Anexado
                          </>
                        ) : (
                          <>
                            <Paperclip className="mr-2 h-4 w-4" /> Anexar
                            Encaminhamento
                          </>
                        )}
                      </Button>
                    </div>
                    {at.file && (
                      <p className="text-xs text-gray-500 mt-1 truncate">
                        Arquivo: {at.file.name}
                      </p>
                    )}
                  </div>
                ))}
                <Button
                  type="button"
                  variant="outline"
                  onClick={() => addItem("atendimentosRequests")}
                >
                  <PlusCircle className="mr-2 h-4 w-4" /> Adicionar Atendimento
                </Button>
              </div>
            </div>

            <div>
              <h3 className="text-lg font-semibold text-blue-600 mb-4">
                Informações Adicionais
              </h3>
              <div className="p-4 border rounded-lg bg-white space-y-6">
                <div className="grid grid-cols-1 md:grid-cols-2 gap-x-8 gap-y-6">
                  <div className="space-y-2">
                    <Label htmlFor="historico-doencas">
                      Doenças que já teve
                    </Label>
                    <Input
                      id="historico-doencas"
                      placeholder="Ex: Catapora"
                      value={
                        formData.cadastrosAnuaisRequests[0]?.historicoDoencas ||
                        ""
                      }
                      onChange={(e) =>
                        handleArrayChange(
                          "cadastrosAnuaisRequests",
                          0,
                          "historicoDoencas",
                          e.target.value
                        )
                      }
                    />
                  </div>
                  <div className="space-y-2">
                    <Label htmlFor="medicacoes-continuas">
                      Medicações de uso contínuo
                    </Label>
                    <Input
                      id="medicacoes-continuas"
                      placeholder="Ex: Losartana"
                      value={
                        formData.cadastrosAnuaisRequests[0]
                          ?.medicacoesContinuas || ""
                      }
                      onChange={(e) =>
                        handleArrayChange(
                          "cadastrosAnuaisRequests",
                          0,
                          "medicacoesContinuas",
                          e.target.value
                        )
                      }
                    />
                  </div>
                  <div className="space-y-2">
                    <Label htmlFor="historico-alergias">Alergias</Label>
                    <Input
                      id="historico-alergias"
                      placeholder="Ex: Alergia a poeira"
                      value={
                        formData.cadastrosAnuaisRequests[0]
                          ?.historicosAlergias || ""
                      }
                      onChange={(e) =>
                        handleArrayChange(
                          "cadastrosAnuaisRequests",
                          0,
                          "historicosAlergias",
                          e.target.value
                        )
                      }
                    />
                  </div>
                  <div className="space-y-2">
                    <Label htmlFor="renda-familiar">Renda Familiar (R$)</Label>
                    <Input
                      id="renda-familiar"
                      type="number"
                      placeholder="1500"
                      value={
                        formData.cadastrosAnuaisRequests[0]?.rendaFamiliar || ""
                      }
                      onChange={(e) =>
                        handleArrayChange(
                          "cadastrosAnuaisRequests",
                          0,
                          "rendaFamiliar",
                          Number(e.target.value)
                        )
                      }
                    />
                  </div>
                </div>
                <div className="flex items-center space-x-2 pt-4 border-t mt-4">
                  <Checkbox
                    id="bpc"
                    checked={
                      formData.cadastrosAnuaisRequests[0]
                        ?.beneficioDePrestacaoContinuada || false
                    }
                    onCheckedChange={(checked) =>
                      handleArrayChange(
                        "cadastrosAnuaisRequests",
                        0,
                        "beneficioDePrestacaoContinuada",
                        !!checked
                      )
                    }
                  />
                  <Label htmlFor="bpc" className="font-medium">
                    Recebe Benefício de Prestação Continuada (BPC)?
                  </Label>
                </div>
              </div>
            </div>

            <div className="flex justify-between items-center pt-6">
              <Button
                type="button"
                variant="outline"
                className="px-8 py-3 text-base"
                onClick={onBack}
              >
                Voltar
              </Button>
              <Button
                type="submit"
                className="bg-yellow-400 text-black font-bold hover:bg-yellow-500 px-8 py-3 text-base"
              >
                Próximo
              </Button>
            </div>
          </form>
        </div>
      </div>
    </div>
  );
}
