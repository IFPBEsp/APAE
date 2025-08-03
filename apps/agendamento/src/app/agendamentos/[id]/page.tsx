import Avatar from "@/components/ui/avatar";

interface PageProps {
    params: {
        id: string;
    };
}

function getStatusStyle(status: string) {
    switch(status) {
        case 'realizada':
            return { class: 'bg-[#0D9767]', text: "Consulta Realizada" };
        case 'nao-realizada':
            return { class: 'bg-[#970D0D]', text: "Consulta Não Realizada" };
        case 'pendente':
            return { class: 'bg-[#0D4F97]', text: "Consulta Pendente" };
    }
}

export default async function VisualizarAgendamento({ params } : PageProps) {
    const {id} = params;

    const agendamento = {
        id,
        nome: "Lucas Matheus Gomes de Lima",
        consulta: "pendente",
        agendamentoMarcado : {
            data: "03-08-2025",
            periodo: "asdasd",
            horario: "10:30",
            areaDeAtendimento: "teste",
            confirmado: true,
            descricao: "asfafafaf",
            justificativa: "afasfafafa"
        },
        profissionalDaSaude : {
            nome: "asasdfa",
            email: "afaa@gmail.com",
            telefone: "0123456789"
        },
        dadosPaciente: {
            contato: "0123456789",
            dataDeNascimento: "12-12-0000",
            cpf: "01234567890",
            rg: "01234-5"
        },
        dadosResidenciais: {
            endereco: "rua tal",
            bairro: "centro",
            cidade: "esperança",
            estado: "paraiba",
            cep: "58135-000",
        },
        informacoesSaudePaciente: {
            vacinas: ["DTP", "ANTIPÓLIO", "BCG", "ANTISARAMPO", "ANTITÉTANO", "COVID"],
            doencas: ["A", "B", "C"],
            alergias: ["D", "E"],
            medicacoes: ["F", "G", "H"],
            deficiencia: "ISADASDADAS",
            atendimento: "GAGAAGSDAGSDA",
        }
    }

    const statusInfo = getStatusStyle(agendamento.consulta);

    return (
        <div className={"mt-20 px-4 w-full"}>
            <div className={"flex flex-row items-center justify-between"}>
                <div className={"flex items-center"}>
                    <Avatar />
                    <h1 className={"ml-10 text-[#0D4F97] text-xl md:text-2xl font-bold mr-5"}>{agendamento.nome}</h1>
                </div>
                <div>
                    <p className={`${statusInfo.class} text-white py-1 px-2 rounded-md text-xs cursor-pointer font-medium text-center mr-3`}>{statusInfo.text}</p>
                </div>
            </div>
        </div>
    );
}