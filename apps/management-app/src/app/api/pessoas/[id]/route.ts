import { createPersonApi } from "@/lib/axios";
import axios, { AxiosError } from "axios";
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

    const cadastroAnual = p.cadastrosAnuaisResponses && p.cadastrosAnuaisResponses.length > 0 ? p.cadastrosAnuaisResponses[0] : null;
    
    const pessoa = {
        //TUDO QUE FOR LISTA TEM QUE VER A MELHOR FORMA PARA ENVIAR E ACESSAR OS DADOS NO FRONT
        //dados pessoais
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
        //contato
        enderecoAtivo: primeiroContato?.enderecoAtivo || "Não informado",
        comprovanteResidencia: primeiroContato?.comprovanteResidencia || "",
        endereco: primeiroContato?.endereco || "Não informado",
        bairro: primeiroContato?.bairro || "Não informada",
        cidade: primeiroContato?.cidade || "Não informada",
        estado: primeiroContato?.estado || "Não informada",
        cep: primeiroContato?.cep || "Não informada",
        naturalidade: primeiroContato?.naturalidade || "Não informada",
        telefone: primeiroContato?.telefone || "Não informado",
        //responsáveis
        responsaveis: p.responsaveisResponses || [],
        //saúde
        vacinas: p.vacinasResponses ?? [],
        possuiBpc: cadastroAnual?.beneficioDePrestacaoContinuada ? "Sim" : "Não",
        doencas: cadastroAnual?.historicoDoencas ?? [],
        alergias: cadastroAnual?.historicosAlergias ?? [],
        medicacao: cadastroAnual?.medicacoesContinuas ?? "Não informado",
        rendaFamiliar: cadastroAnual?.rendaFamiliar ?? "Não informado",
        deficiencias: p.deficienciasResponses ?? [],
        atendimentos: p.atendimentosResponses ?? [],
    }

    return NextResponse.json(pessoa, {status: 200});

  } catch (error) {
    console.error("Erro na API Route (/api/pessoas/[id]):", error);

    if (error instanceof AxiosError && error.response) {
      return NextResponse.json(
        { message: error.response.data?.message || "Erro ao buscar pessoa" },
        { status: error.response.status }
      );
    }

    return NextResponse.json(
      { message: "Erro inesperado no servidor" },
      { status: 500 }
    );
  }
}
