import {Avatar, AvatarFallback, AvatarImage} from "@/components/ui/avatar";
import { Badge } from "@/components/ui/badge"

// export default function Avatar() {
//     return(
//         <div className={"bg-gray-200 rounded-full p-5 flex items-center justify-center"}>
//             <svg className={"w-12 h-12 md:w-25 md:h-25"} viewBox="0 0 16 16" xmlns="http://www.w3.org/2000/svg">
//                 <path
//                     d="m 8 1 c -1.65625 0 -3 1.34375 -3 3 s 1.34375 3 3 3 s 3 -1.34375 3 -3 s -1.34375 -3 -3 -3 z m -1.5 7 c -2.492188 0 -4.5 2.007812 -4.5 4.5 v 0.5 c 0 1.109375 0.890625 2 2 2 h 8 c 1.109375 0 2 -0.890625 2 -2 v -0.5 c 0 -2.492188 -2.007812 -4.5 -4.5 -4.5 z m 0 0"
//                     fill="#2e3436"/>
//             </svg>
//         </div>
//     );
// }

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
        default:
            return {class: 'bg-gray-200', text: "Sem Status"}
    }
}

export default async function VisualizarAgendamento({ params } : PageProps) {
    const {id} = params;

    const agendamento = {
        id,
        imagem: "adfaf",
        nome: "Lucas Matheus Gomes de Lima",
        consulta: "realizada",
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
                    <div className={"bg-gray-200 rounded-full w-17 h-17 md:w-23 md:h-23"}>
                        <Avatar className={"p-5 w-17 h-17 md:w-23 md:h-23"}>
                            <AvatarImage src="https://cdn-icons-png.flaticon.com/512/266/266033.png" alt="avatar"/>
                            <AvatarFallback>{agendamento.nome.at(0)}</AvatarFallback>
                        </Avatar>
                    </div>
                    <h1 className={"ml-10 text-[#0D4F97] text-xl md:text-2xl font-bold mr-5"}>{agendamento.nome}</h1>
                </div>
                <Badge className={`${statusInfo.class} text-white py-1 px-2 rounded-md text-xs cursor-default font-medium text-center mr-5`}>
                    {statusInfo.text}
                </Badge>
            </div>
        </div>
    );
}