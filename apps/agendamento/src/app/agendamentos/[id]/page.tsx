interface PageProps {
    params: {
        id: string;
    };
}

export default async function VisualizarAgendamento({ params } : PageProps) {
    const {id} = params;

    const agendamento = {
        id,
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

    return (
        <div>
            <h1>Hello world</h1>
        </div>
    );
}