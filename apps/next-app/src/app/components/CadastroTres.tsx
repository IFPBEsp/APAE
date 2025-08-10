import React, { useState, useEffect } from "react";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";
import { Checkbox } from "@/components/ui/checkbox";
import { PlusCircle, Trash2, ArrowRight } from "lucide-react";
import apae from "../images/apae.png";
import {
  criarPessoa,
  PessoaRequest,
  PessoaResponsavelRequest,
} from "../service/pessoaService";
import { FullFormData } from "./CadastroDois";
import { anexarDocumento, criarBucket, verificarBucket } from "../service/documentoService";

interface CadastroTresProps {
  onBack: () => void;
  formData: FullFormData;
}

interface FormData {
  responsaveisRequests: PessoaResponsavelRequest[];
}

const Modal = ({
  message,
  onClose,
  isError,
}: {
  message: string;
  onClose: () => void;
  isError?: boolean;
}) => (
  <div className="fixed inset-0 bg-gray-900 bg-opacity-75 flex justify-center items-center z-50">
    <div className="bg-white p-8 rounded-lg shadow-2xl max-w-sm w-full text-center">
      <h3
        className={`text-2xl font-bold mb-4 ${
          isError ? "text-red-600" : "text-blue-800"
        }`}
      >
        {isError ? "Erro" : "Sucesso"}
      </h3>
      <p className="text-gray-700 mb-6">{message}</p>
      <button
        onClick={onClose}
        className={`w-full py-2 px-4 rounded-lg text-white font-semibold transition-colors ${
          isError
            ? "bg-red-500 hover:bg-red-600"
            : "bg-blue-800 hover:bg-blue-900"
        }`}
      >
        Fechar
      </button>
    </div>
  </div>
);

export default function CadastroTres({ onBack, formData: initialFormData  }: CadastroTresProps) {
  const [formData, setFormData] = useState<FormData>({
    responsaveisRequests: initialFormData.responsaveisRequests || [
      {
        nome: "",
        ondeProcurar: "",
        vivo: true,
        profissao: "",
        rg: "",
        cpf: "",
        emergencia: "",
        tipoResponsavel: "",
      },
    ],
  });
  const [isLoading, setIsLoading] = useState(false);
  const [modalInfo, setModalInfo] = useState<{
    message: string;
    isError?: boolean;
  } | null>(null);

  useEffect(() => {
    const savedData = localStorage.getItem("cadastroData");
    if (savedData) {
      const fullData = JSON.parse(savedData);
      if (
        fullData.responsaveisRequests &&
        fullData.responsaveisRequests.length > 0
      ) {
        setFormData({
          responsaveisRequests: fullData.responsaveisRequests,
        });
      }
    }
  }, []);

  const handleChange = (
    index: number,
    field: keyof PessoaResponsavelRequest,
    value: string | boolean
  ) => {
    setFormData((prev) => {
      const updatedResponsaveis = [...prev.responsaveisRequests];
      const responsavel = { ...updatedResponsaveis[index], [field]: value };
      updatedResponsaveis[index] = responsavel;
      return { ...prev, responsaveisRequests: updatedResponsaveis };
    });
  };

  const addResponsavel = () => {
    setFormData((prev) => ({
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
        },
      ],
    }));
  };

  const removeResponsavel = (index: number) => {
    setFormData((prev) => {
      if (prev.responsaveisRequests.length <= 1) return prev;
      const updatedResponsaveis = prev.responsaveisRequests.filter(
        (_, i) => i !== index
      );
      return { ...prev, responsaveisRequests: updatedResponsaveis };
    });
  };

  const handleFinalSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setIsLoading(true);

    try {
      console.log("[submit] starting final submit...");

      // metadata stored in localStorage (no files here)
      const storedData = JSON.parse(localStorage.getItem("cadastroData") || "{}");
      console.log("[submit] loaded storedData from localStorage:", storedData);

      // Build the person payload without File objects (files handled separately)
      const finalData: PessoaRequest = {
        ...storedData,
        responsaveisRequests: formData.responsaveisRequests, // local state for responsibles
        vacinacoesRequests: storedData.vacinacoesRequests || [],
        deficienciasRequests: storedData.deficienciasRequests || [],
        atendimentosRequests: storedData.atendimentosRequests || [],
        cadastrosAnuaisRequests: storedData.cadastrosAnuaisRequests || [],
      };

      console.log("[submit] creating person with payload:", finalData);
      const response = await criarPessoa(finalData);
      console.log("[submit] pessoa criada com sucesso:", response);

      const userId = response.id;
      if (!userId) {
        throw new Error("Created user has no id");
      }
      console.log("[submit] created userId =", userId);

      // 1) Verify bucket
      console.log(`[submit] checking if bucket exists for user ${userId}...`);
      const bucketExists = await verificarBucket(userId);
      console.log(`[submit] bucketExists = ${bucketExists}`);

      // 2) Create bucket if missing
      if (!bucketExists) {
        console.log(`[submit] creating bucket for user ${userId}...`);
        try {
          await criarBucket(userId);
          console.log(`[submit] bucket created for user ${userId}`);
        } catch (err) {
          console.error("[submit] failed to create bucket:", err);
          setModalInfo({
            message: "Erro ao criar bucket. Tente novamente.",
            isError: true,
          });
          setIsLoading(false);
          return; // stop here, cannot upload files without bucket
        }
      }

      // 3) Upload files from the in-memory initialFormData (not localStorage)
      // NOTE: initialFormData is the prop that should contain real File objects
      const defList = initialFormData.deficienciasRequests || [];
      const atList = initialFormData.atendimentosRequests || [];

      console.log(`[submit] will upload ${defList.length} deficiencia files and ${atList.length} atendimento files`);

      // Upload deficiencias
      for (let i = 0; i < defList.length; i++) {
        const def = defList[i];
        if (def?.file instanceof File) {
          try {
            console.log(`[upload] uploading LAUDO file #${i} name="${def.file.name}"`);
            await anexarDocumento(userId, "LAUDO", def.file);
            console.log(`[upload] LAUDO file #${i} uploaded successfully`);
          } catch (err) {
            console.error(`[upload] failed to upload LAUDO file #${i}:`, err);
            // continue with other files
          }
        } else {
          console.log(`[upload] no file for LAUDO item #${i}, skipping`);
        }
      }

      // Upload atendimentos
      for (let i = 0; i < atList.length; i++) {
        const at = atList[i];
        if (at?.file instanceof File) {
          try {
            console.log(`[upload] uploading ENCAMINHAMENTO file #${i} name="${at.file.name}"`);
            await anexarDocumento(userId, "ENCAMINHAMENTO", at.file);
            console.log(`[upload] ENCAMINHAMENTO file #${i} uploaded successfully`);
          } catch (err) {
            console.error(`[upload] failed to upload ENCAMINHAMENTO file #${i}:`, err);
          }
        } else {
          console.log(`[upload] no file for ENCAMINHAMENTO item #${i}, skipping`);
        }
      }

      // cleanup
      localStorage.removeItem("cadastroData");
      localStorage.removeItem("cadastroStep1");

      console.log("[submit] finished all uploads and cleanup");
      setModalInfo({ message: "Cadastro finalizado com sucesso!" });
    } catch (error) {
      console.error("Erro ao criar pessoa:", error);
      const errorMessage = "Não foi possível finalizar o cadastro. Verifique os dados e tente novamente.";
      setModalInfo({ message: errorMessage, isError: true });
    } finally {
      setIsLoading(false);
    }
  };


  return (
    <div className="min-h-screen w-full lg:grid lg:grid-cols-12 font-sans overflow-hidden">
      {modalInfo && <Modal {...modalInfo} onClose={() => setModalInfo(null)} />}
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
            Cadastro de pessoas - Etapa Final
          </h2>
          <form onSubmit={handleFinalSubmit} className="space-y-8">
            <div>
              <h3 className="text-lg font-semibold text-blue-600 mb-4">
                Responsáveis
              </h3>
              <div className="space-y-6">
                {formData.responsaveisRequests.map((resp, idx) => (
                  <div
                    key={idx}
                    className="p-4 border rounded-lg space-y-6 relative bg-white"
                  >
                    {formData.responsaveisRequests.length > 1 && (
                      <Button
                        variant="ghost"
                        size="icon"
                        className="absolute top-2 right-2 text-gray-400 hover:text-red-500"
                        onClick={() => removeResponsavel(idx)}
                      >
                        <Trash2 className="h-4 w-4" />
                      </Button>
                    )}
                    <div className="grid grid-cols-1 md:grid-cols-2 gap-x-8 gap-y-6">
                      <div className="space-y-2">
                        <Label htmlFor={`resp-nome-${idx}`}>
                          Nome do Responsável
                        </Label>
                        <Input
                          id={`resp-nome-${idx}`}
                          placeholder="Ex: João da Silva"
                          value={resp.nome || ""}
                          onChange={(e) =>
                            handleChange(idx, "nome", e.target.value)
                          }
                          required
                        />
                      </div>
                      <div className="space-y-2">
                        <Label htmlFor={`resp-cpf-${idx}`}>CPF</Label>
                        <Input
                          id={`resp-cpf-${idx}`}
                          placeholder="Ex: 123.456.789-00"
                          value={resp.cpf || ""}
                          onChange={(e) =>
                            handleChange(idx, "cpf", e.target.value)
                          }
                          required
                        />
                      </div>
                      <div className="space-y-2">
                        <Label htmlFor={`resp-rg-${idx}`}>RG</Label>
                        <Input
                          id={`resp-rg-${idx}`}
                          placeholder="Ex: 12.345.678-9"
                          value={resp.rg || ""}
                          onChange={(e) =>
                            handleChange(idx, "rg", e.target.value)
                          }
                          required
                        />
                      </div>
                      <div className="space-y-2">
                        <Label htmlFor={`resp-profissao-${idx}`}>
                          Profissão
                        </Label>
                        <Input
                          id={`resp-profissao-${idx}`}
                          placeholder="Ex: Professor"
                          value={resp.profissao || ""}
                          onChange={(e) =>
                            handleChange(idx, "profissao", e.target.value)
                          }
                        />
                      </div>
                      <div className="space-y-2">
                        <Label htmlFor={`resp-emergencia-${idx}`}>
                          Contato de Emergência
                        </Label>
                        <Input
                          id={`resp-emergencia-${idx}`}
                          placeholder="Ex: (83) 99999-9999"
                          value={resp.emergencia || ""}
                          onChange={(e) =>
                            handleChange(idx, "emergencia", e.target.value)
                          }
                          required
                        />
                      </div>
                      <div className="space-y-2">
                        <Label htmlFor={`resp-tipo-${idx}`}>
                          Tipo de Responsável
                        </Label>
                        <Input
                          id={`resp-tipo-${idx}`}
                          placeholder="Ex: Pai, Mãe"
                          value={resp.tipoResponsavel || ""}
                          onChange={(e) =>
                            handleChange(idx, "tipoResponsavel", e.target.value)
                          }
                          required
                        />
                      </div>
                      <div className="space-y-2 md:col-span-2">
                        <Label htmlFor={`resp-onde-${idx}`}>
                          Onde procurar (endereço, etc.)
                        </Label>
                        <Input
                          id={`resp-onde-${idx}`}
                          placeholder="Ex: Rua das Flores, 123"
                          value={resp.ondeProcurar || ""}
                          onChange={(e) =>
                            handleChange(idx, "ondeProcurar", e.target.value)
                          }
                        />
                      </div>
                      <div className="flex items-center space-x-2 pt-2">
                        <Checkbox
                          id={`resp-vivo-${idx}`}
                          checked={resp.vivo}
                          onCheckedChange={(checked) =>
                            handleChange(idx, "vivo", !!checked)
                          }
                        />
                        <Label
                          htmlFor={`resp-vivo-${idx}`}
                          className="font-medium"
                        >
                          Vivo
                        </Label>
                      </div>
                    </div>
                  </div>
                ))}
                <Button
                  type="button"
                  variant="outline"
                  onClick={addResponsavel}
                >
                  <PlusCircle className="mr-2 h-4 w-4" /> Adicionar Responsável
                </Button>
              </div>
            </div>

            {/* --- Navigation Buttons --- */}
            <div className="flex justify-between items-center pt-6">
              <Button
                type="button"
                variant="outline"
                className="px-8 py-6 text-base"
                onClick={onBack}
                disabled={isLoading}
              >
                Voltar
              </Button>
              <Button
                type="submit"
                className="bg-yellow-400 text-black font-bold hover:bg-yellow-500 px-8 py-6 text-base"
                disabled={isLoading}
              >
                {isLoading ? "Salvando..." : "Salvar Cadastro"}
              </Button>
            </div>
          </form>
        </div>
      </div>
    </div>
  );
}
