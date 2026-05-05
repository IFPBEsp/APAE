"use client";

import { useState, useEffect } from "react";
import { useParams, useRouter } from "next/navigation";
import { Button } from "@/components/ui/button";
import { ArrowLeft, Loader2, FileText, GraduationCap, Search, ChevronLeft, ChevronRight } from "lucide-react";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { Input } from "@/components/ui/input"; 

interface Report {
  id: string;
  alunoId: string;
  alunoNome: string;
  professorId: string;
  professorNome: string;
  turmaId: string;
  turmaNome: string;
  atividades: string;
  habilidades: string;
  estrategias: string;
  recursos: string;
  createdAt: Date;
}

interface Assessment {
  id: string;
  alunoId: string;
  alunoNome: string;
  professorId: string;
  professorNome: string;
  descricao: string;
  dataAvaliacao: Date;
}

export default function EscolaresPage() {
  const params = useParams();
  const router = useRouter();
  const id = params?.id as string;

  const [status, setStatus] = useState<"loading" | "success" | "error" | "empty">("loading");
  const [relatorios, setRelatorios] = useState<Report[]>([]);
  const [avaliacoes, setAvaliacoes] = useState<Assessment[]>([]);
  
  const [searchReports, setSearchReports] = useState("");
  const [searchAssessments, setSearchAssessments] = useState("");
  const [currentPage, setCurrentPage] = useState(1);

  useEffect(() => {
    const fetchDadosEscolares = async () => {
      if(!id || id === "undefined") {
        return
      };

      try {
        setStatus("loading");

        const responseReports = await fetch(`/api/pessoas/${id}/reports`);
        const responseAssessments = await fetch(`/api/pessoas/${id}/assessments`);

        if (!responseReports.ok || !responseAssessments.ok) {
          setStatus("error");
          return;
        }
        const reportData = await responseReports.json();
        const assessmentData = await responseAssessments.json();
        setRelatorios(reportData);
        setAvaliacoes(assessmentData);

        if (reportData.length === 0 && assessmentData.length === 0) {
          setStatus("empty");
        } else {
          setStatus("success");
        }
      } catch (error) {
        setStatus("error");
      }
    };

    if (id) fetchDadosEscolares();
  }, [id]);

  const relatoriosFiltrados = (lista: any[], termo: string) => {
    return lista.filter(item =>
      Object.values(item).some(value =>
        typeof value === "string" && value.toLowerCase().includes(termo.toLowerCase())
      )
    );
  };

  const relatoriosFiltradosList = relatoriosFiltrados(relatorios, searchReports);
  const avaliacoesFiltradasList = relatoriosFiltrados(avaliacoes, searchAssessments);

  return (
    <main className="container mx-auto p-4 md:p-6 font-nunito space-y-6">
      
      <div className="flex flex-col gap-4">
        <Button variant="outline" onClick={() => router.push(`/person/${id}`)} className="w-fit gap-2">
          <ArrowLeft className="h-4 w-4" /> Voltar para Perfil
        </Button>
        <div>
          <h2 className="font-baloo font-bold text-[#0D4F97] text-[28px]">Relatórios e Avaliações Escolares</h2>
          <p className="text-gray-500">Acompanhamento do desenvolvimento educacional do paciente.</p>
        </div>
      </div>

      {status === "loading" && (
        <div className="flex flex-col items-center justify-center py-20">
          <Loader2 className="h-10 w-10 animate-spin text-[#0D4F97] mb-4" />
          <p className="text-gray-500 font-semibold">Carregando dados escolares...</p>
        </div>
      )}

      {status === "error" && (
        <div className="bg-red-50 border border-red-200 text-red-600 p-4 rounded-md text-center py-10">
          <p className="font-bold">Ocorreu um erro ao carregar as informações.</p>
          <Button variant="outline" className="mt-4 border-red-600 text-red-600 hover:bg-red-50" onClick={() => window.location.reload()}>
            Tentar Novamente
          </Button>
        </div>
      )}

      {status === "empty" && (
        <div className="text-center py-16 bg-slate-50 rounded-xl border-2 border-dashed border-slate-200">
          <GraduationCap className="h-12 w-12 text-gray-300 mx-auto mb-3" />
          <p className="text-gray-500 font-semibold">Nenhum relatório ou avaliação cadastrado.</p>
        </div>
      )}

      {status === "success" && (
        <>
          <div className="flex justify-end items-center bg-white p-4 rounded-lg shadow-sm border">
            <div className="relative w-full md:w-72">
              <Search className="absolute left-3 top-1/2 -translate-y-1/2 h-4 w-4 text-gray-400" />
              <Input 
                placeholder="Buscar por título..." 
                className="pl-9"
                value={searchReports}
                onChange={(e) => setSearchReports(e.target.value)}
              />
            </div>
          </div>

          <div className="grid grid-cols-1 gap-6">
            
            <Card className="shadow-md overflow-hidden">
              <CardHeader className="border-b bg-slate-50 pb-4 flex flex-row items-center">
                <CardTitle className="text-[#0D4F97] flex items-center gap-2 text-lg">
                  <FileText className="h-5 w-5" /> Relatórios
                </CardTitle>
                <div className="relative w-full md:w-80">
                  <Search className="absolute left-3 top-1/2 -translate-y-1/2 h-4 w-4 text-gray-400" />
                    <Input 
                      placeholder="Pesquisar em todos os relatórios..." 
                      className="pl-9"
                      value={searchReports}
                      onChange={(e) => setSearchReports(e.target.value)}
                    />
                </div>
              </CardHeader>
              <CardContent className="p-0">
                <div className="overflow-x-auto">
                  <table className="w-full text-sm text-left">
                    <thead className="text-xs text-gray-500 uppercase bg-gray-50 border-b">
                      <tr>
                        <th className="px-6 py-4 font-semibold">Professor</th>
                        <th className="px-6 py-4 font-semibold">Turma</th>
                        <th className="px-6 py-4 font-semibold">Atividades</th>
                        <th className="px-6 py-4 font-semibold text-center">Habilidades</th>
                        <th className="px-6 py-4 font-semibold text-center">Data</th>
                      </tr>
                    </thead>
                    <tbody>
                      {relatoriosFiltradosList.length > 0 ? (
                        relatoriosFiltradosList.map((relatorio) => (
                          <tr key={relatorio.id} className="border-b hover:bg-slate-50 transition-colors">
                            <td className="px-6 py-4 font-medium text-gray-900">{relatorio.professorNome}</td>
                            <td className="px-6 py-4 text-gray-600">{relatorio.turmaNome}</td>
                            <td className="px-6 py-4 text-gray-600">{relatorio.atividades}</td>
                            <td className="px-6 py-4 text-gray-600">{relatorio.habilidades}</td>
                            <td className="px-6 py-4 text-gray-600">{new Date(relatorio.createdAt).toLocaleDateString()}</td>
                            <td className="px-6 py-4 text-center">
                            </td>
                          </tr>
                        ))
                      ) : (
                        <tr>
                          <td colSpan={4} className="text-center py-6 text-gray-500">Nenhum relatório encontrado para a busca.</td>
                        </tr>
                      )}
                    </tbody>
                  </table>
                </div>
                
                <div className="flex items-center justify-between px-6 py-3 border-t bg-gray-50">
                  <span className="text-sm text-gray-600">Página {currentPage} de 1</span>
                  <div className="flex gap-2">
                    <Button variant="outline" size="icon" disabled><ChevronLeft className="h-4 w-4"/></Button>
                    <Button variant="outline" size="icon" disabled><ChevronRight className="h-4 w-4"/></Button>
                  </div>
                </div>
              </CardContent>
            </Card>

            <Card className="shadow-md overflow-hidden">
              <CardHeader className="border-b bg-slate-50 pb-4 flex flex-row items-center">
                <CardTitle className="text-[#0D4F97] flex items-center gap-2 text-lg">
                  <GraduationCap className="h-5 w-5" /> Avaliações
                </CardTitle>
                <div className="relative w-full md:w-80">
                  <Search className="absolute left-3 top-1/2 -translate-y-1/2 h-4 w-4 text-gray-400" />
                    <Input 
                      placeholder="Pesquisar em todas as avaliações..." 
                      className="pl-9"
                      value={searchAssessments}
                      onChange={(e) => setSearchAssessments(e.target.value)}
                    />
                </div>
              </CardHeader>
              <CardContent className="p-0">
                <div className="overflow-x-auto">
                  <table className="w-full text-sm text-left">
                    <thead className="text-xs text-gray-500 uppercase bg-gray-50 border-b">
                      <tr>
                        <th className="px-6 py-4 font-semibold">Professor</th>
                        <th className="px-6 py-4 font-semibold">Descrição</th>
                        <th className="px-6 py-4 font-semibold text-center">Data</th>
                      </tr>
                    </thead>
                    <tbody>
                      {avaliacoesFiltradasList.map((av) => (
                        <tr key={av.id} className="border-b hover:bg-slate-50 transition-colors">
                          <td className="px-6 py-4 font-medium text-gray-900">{av.professorNome}</td>
                          <td className="px-6 py-4 text-gray-600">{av.descricao}</td>
                          <td className="px-6 py-4 font-bold text-[#0D4F97] text-center">{new Date(av.dataAvaliacao).toLocaleDateString()}</td>
                        </tr>
                      ))}
                    </tbody>
                  </table>
                </div>
              </CardContent>
            </Card>

          </div>
        </>
      )}
    </main>
  );
}