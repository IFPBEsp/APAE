import { createPersonApi } from "@/lib/axios";
import { AxiosError } from "axios";
import { NextResponse } from "next/server";

interface Params {
  params: { id: string };
}

export async function GET(request: Request, { params }: Params) {
  try {
    const api = await createPersonApi();
    const response = await api.get(`/${params.id}`);
    const p = response.data;

    const primeiroContato = p.contatoResponse && p.contatoResponse.length > 0 ? p.contatoResponse[0] : null;

    const responsaveis = p.responsaveisResponses?.map((r: any) => ({
      id: r.id,
      nome: r.nome || "Não informado",
      ondeProcurar: r.ondeProcurar || "Não informado",
      vivo: r.vivo ? "Sim" : "Não",
      profissao: r.profissao || "Não informado",
      rg: r.rg || "Não informado",
      cpf: r.cpf || "Não informado",
      emergencia: r.emergencia || "Não informado",
      tipoResponsavel: r.tipoResponsavel,
    })) ?? [];

    const vacinas = p.vacinasResponses?.map((v: any) => ({
      id: v.id,
      nome: v.nome || "Não informado",
      dataAplicacao: v.dataAplicacao || "Não informado",
    })) ?? [];
    
    const deficiencias = p.deficienciasResponses?.map((d: any) => ({
      id: d.id,
      descricao: d.descricao || "Não informado",
    })) ?? [];

    const atendimentos = p.atendimentosResponses?.map((a: any) => ({
      id: a.id,
      descricao: a.descricao || "Não informado",
    })) ?? [];

    const cadastroAnual = p.cadastrosAnuaisResponses?.[0] ? {
      possuiBpc: p.cadastrosAnuaisResponses[0].beneficioDePrestacaoContinuada ? "Sim" : "Não",
      doencas: p.cadastrosAnuaisResponses[0].historicoDoencas || "Não informado",
      alergias: p.cadastrosAnuaisResponses[0].historicosAlergias || "Não informado",
      medicacao: p.cadastrosAnuaisResponses[0].medicacoesContinuas || "Não informado",
      rendaFamiliar: p.cadastrosAnuaisResponses[0].rendaFamiliar || "Não informado",
    } : null;

    const pessoa = {
      // Dados pessoais
      id: p.id || "",
      nomeCompleto: p.nomeCompleto || "Não informado",
      dataNascimento: p.dataNascimento || "Não informado",
      numRegistroNasc: p.numRegistroNasc || "Não informado",
      fls: p.fls || "Não informado",
      livro: p.livro || "Não informado",
      cartorio: p.cartorio || "Não informado",
      cpf: p.cpf || "Não informado",
      rg: p.rg || "Não informado",
      dataEmissaoRg: p.dataEmissaoRg || "Não informado",
      orgaoEmissorRg: p.orgaoEmissorRg || "Não informado",
      cns: p.cns || "Não informado",
      nis: p.nis || "Não informado",
      dataCadastramento: p.dataCadastramento || "Não informado",
      // Contato
      enderecoAtivo: primeiroContato?.enderecoAtivo || "Não informado",
      comprovanteResidencia: primeiroContato?.comprovanteResidencia || "",
      endereco: primeiroContato?.endereco || "Não informado",
      bairro: primeiroContato?.bairro || "Não informada",
      cidade: primeiroContato?.cidade || "Não informada",
      estado: primeiroContato?.estado || "Não informada",
      cep: primeiroContato?.cep || "Não informada",
      naturalidade: primeiroContato?.naturalidade || "Não informada",
      telefone: primeiroContato?.telefone || "Não informado",
      // Responsáveis
      responsaveis,
      // Vacinas
      vacinas,
      // Cadastro Anual
      cadastroAnual,
      // Deficiências
      deficiencias,
      // Atendimentos
      atendimentos,
    };

    return NextResponse.json(pessoa, { status: 200 });
  } catch (error) {
    console.error("Erro na API Route (/api/pessoas/[id]):", error);

    if (error instanceof AxiosError && error.response) {
      return NextResponse.json(
        { message: error.response.data?.message || "Erro ao buscar pessoa" },
        { status: error.response.status }
      );
    }

    return NextResponse.json({ message: "Erro inesperado no servidor" }, { status: 500 });
  }
}
