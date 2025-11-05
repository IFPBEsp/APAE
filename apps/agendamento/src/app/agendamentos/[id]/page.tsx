import { Avatar, AvatarFallback, AvatarImage } from "@/components/ui/avatar";
import { Badge } from "@/components/ui/badge";
import {
  Card,
  CardAction,
  CardContent,
  CardHeader,
  CardTitle,
} from "@/components/ui/card";
import {
  Dialog,
  DialogContent,
  DialogHeader,
  DialogTitle,
  DialogTrigger,
  DialogDescription,
} from "@/components/ui/dialog";
import { Pencil } from "lucide-react";
import { Button } from "@/components/ui/button";

import {
  getAppointmentById,
  Appointment,
} from "@/app/services/AppointmentService";
import { AppointmentForm } from "@/components/forms/AppointmentForm";
import TrashButton from "@/components/buttons/trashButton";
// import ConfirmaRealizacaoButton from "@/components/buttons/ConfirmaRealizacaoButton";
import { separaETransformaEmNumero } from "@/lib/utils";

interface PageProps {
  params: {
    id: string;
  };
}

export default async function viewAppointment({ params }: PageProps) {
  const { id } = await params;
  const appointment: Appointment = await getAppointmentById(id);

  // prox consulta -> data inicial
  const [year, month, day] = separaETransformaEmNumero(
    appointment.initialDate,
    "-"
  );
  // hora da prox consulta -> hora do agendamento
  const [hour, minute, second] = separaETransformaEmNumero(
    appointment.hour,
    ":"
  );
  const dataHoraDate =
    !isNaN(year) &&
    !isNaN(month) &&
    !isNaN(day) &&
    !isNaN(hour) &&
    !isNaN(minute) &&
    !isNaN(second)
      ? new Date(year, month-1, day, hour, minute, second)
      : null;

  const getStatusStyle = (status: boolean | undefined, data: Date | null) => {
    if (!data) {
      return { class: "bg-gray-400", text: "Invalid date" };
    }


    const dataCurrent = new Date();
    if (status) {
      return { class: "bg-[#0D4F97]", text: "Confirmed Consultation" };
    } else if (data > dataCurrent) {
      return { class: "bg-[#f0bc1f]", text: "Pending Consultation" };
    } else {
      return { class: "bg-[#970D0D]", text: "Consultation Not Performed" };
    }
  };

  return (
    <div className="mt-20 w-full mr-17 ml-10">
      <header className="flex flex-row items-center justify-between">
        <div className="flex items-center">
          <div className="bg-gray-200 rounded-full w-17 h-17 md:w-23 md:h-23">
            <Avatar className="p-5 w-17 h-17 md:w-23 md:h-23">
              <AvatarImage
                src="https://cdn-icons-png.flaticon.com/512/266/266033.png"
                alt="avatar"
              />
              <AvatarFallback>
                {appointment.annualRegistration.patient.fullName.charAt(0) || "?"}
              </AvatarFallback>
            </Avatar>
          </div>
          <h1 className="ml-10 text-[#0D4F97] text-xl md:text-2xl font-bold mr-5">
            {appointment.annualRegistration.patient.fullName}
          </h1>

        </div>

      {/* Borda do status do agendamento */}
       <Badge className={`${appointment.isActive 
                          ? "bg-[#E6F6EC] border-l-4 border-[#16A34A] text-[#166534]"
                          : "bg-[#FEEAEA] border-l-4 border-[#DC2626] text-[#7F1D1D]"
                        }
                px-4 py-2 w-32 flex flex-col items-center justify-center
                rounded-l-md shadow-sm font-bold text-sm text-center
              `}>
          <p className="text-sm font-bold">
            {appointment.isActive ? "Ativa" : "Não ativa"}
          </p>
        </Badge>

      </header>

      <main className="mt-7 mb-15">

        {/* Card Agendamento */}
        <Card className="text-[#0D4F97] mb-5">
          <CardHeader className="relative">
            <CardTitle className="absolute left-1/2 top-1/2 -translate-x-1/2 -translate-y-1/2 font-bold text-center text-lg md:text-xl">
              Agendamento
            </CardTitle>
            <CardAction className="flex gap-1">
              <Dialog>
                <DialogTrigger asChild>
                  <Button className="bg-transparent cursor-pointer border-1 border-[#0D4F97] text-[#0D4F97] hover:text-[#0d4f55] active:text-[#0d4ffe] hover:bg-[rgba(0,0,0,0.1)] transition-colors rounded-full overflow-hidden">
                    <Pencil />
                  </Button>
                </DialogTrigger>
                <DialogContent className="w-full sm:max-w-[425px]">
                  <DialogHeader>
                    <DialogTitle>Editar Agendamento</DialogTitle>
                    <DialogDescription>
                      Edite os detalhes abaixo para agendar uma consulta.
                    </DialogDescription>
                  </DialogHeader>
                  <AppointmentForm editAppointment={appointment} />
                </DialogContent>
              </Dialog>
              <div className="rounded-full  overflow-hidden border-1 border-[#0D4F97]">
                <TrashButton id={id} realizado={false} />
              </div>
              
              {/* <ConfirmaRealizacaoButton id={id} /> */}
            </CardAction>
          </CardHeader>
          <CardContent>
            <div className="flex justify-between">
              <div className="flex">
                <p className="font-medium mr-2">Data: </p>
                <p>
                  {dataHoraDate
                    ? new Intl.DateTimeFormat("pt-BR").format(dataHoraDate)
                    : "—"}
                </p>
              </div>
              <div className="flex items-center w-1/3">
                <p className="font-medium mr-2">Período: </p>
                <p>
                  {appointment.frequencyDays !== undefined
                    ? `${appointment.frequencyDays} dias`
                    : "—"}
                </p>
              </div>
            </div>
            <div className="flex">
              <p className="font-medium mr-2">Horário: </p>
              <p>
                {dataHoraDate
                  ? dataHoraDate.toLocaleTimeString("pt-BR", {
                      hour: "2-digit",
                      minute: "2-digit",
                    })
                  : "—"}
              </p>
            </div>
            <div className="flex">
              <p className="font-medium mr-2">Área de atendimento: </p>
              <p>{appointment.annualRegistration.professional.healthArea || "—"}</p>
            </div>
            <div className="flex">
              <p className="font-medium mr-2">Status: </p>
              <p className={`font-bold ${ 
                  appointment.isActive ? "text-green-700" 
                  : "text-red-700"}`}
              >
                {appointment.isActive ? "Ativo" : "Não ativo"}</p>
            </div> 


            
          </CardContent>
        </Card>

        {/* Card Profissional da Saúde */}
        <Card className="text-[#0D4F97] mb-4">
          <CardHeader className="relative">
            <CardTitle className="absolute left-1/2 top-1/2 -translate-x-1/2 -translate-y-1/2 font-bold text-center text-lg md:text-xl">
              Profissional da Saúde
            </CardTitle>
          </CardHeader>
          <CardContent>
            <div className="flex justify-between">
                {/* Lado esquerdo */}
              <div className="flex flex-col gap-2">
                  <div className="flex">
                  <p className="font-medium mr-2">Nome: </p>
                  <p>{appointment.annualRegistration.professional.name || "—"}</p>
                </div>
                <div className="flex">
                  <p className="font-medium mr-2">Email: </p>
                  <p>{appointment.annualRegistration.professional.email || "—"}</p>
                </div>
                <div className="flex">
                  <p className="font-medium mr-2">Telefone: </p>
                  <p>{appointment.annualRegistration.professional.phone || "—"}</p>
                </div>
              </div>

                {/* Lado direito */}
              <div className="flex flex-col gap-2 w-1/3">
                <div className="flex">
                  <p className="font-medium mr-2">Documento médico: </p>
                  <p>{appointment.annualRegistration.professional.professionalDoc || "—"}</p>
                </div>
                <div className="flex">
                  <p className="font-medium mr-2">RG: </p>
                  <p>{appointment.annualRegistration.professional.rg || "—"}</p>
                </div>
                <div className="flex">
                  <p className="font-medium mr-2">Cidade: </p>
                  <p>{appointment.annualRegistration.professional.address.city || "—"}</p>
                </div>
              </div>
            </div>
          </CardContent>
        </Card>

        {/* Card Dados Pessoais do Paciente */}
        <Card className="text-[#0D4F97] mb-4">
          <CardHeader className="relative">
            <CardTitle className="absolute left-1/2 top-1/2 -translate-x-1/2 -translate-y-1/2 font-bold text-center text-lg md:text-xl">
              Dados Pessoais do Paciente
            </CardTitle>
          </CardHeader>
          <CardContent>
            <div className="flex justify-between">
              {/* Lado esquerdo */}
              <div className="flex flex-col gap-2">
              <div className="flex">
                  <p className="font-medium mr-2">Data de Nascimento: </p>
                  <p> {separaETransformaEmNumero(appointment.annualRegistration.patient.birthDate,"-")
                      .map((n, i) => (i == 0 ? n : n.toString().padStart(2, "0")))
                      .reverse()
                      .join("/") || "—"}
                  </p>
                </div>
                <div className="flex">
                  <p className="font-medium mr-2">CPF: </p>
                  <p>{appointment.annualRegistration.patient.cpf || "—"}</p>
                </div>
                <div className="flex">
                  <p className="font-medium mr-2">RG: </p>
                  <p>{appointment.annualRegistration.patient.rg || "—"}</p>
                </div>
                
                <div className="flex">
                  <p className="font-medium mr-2">Contato: </p>
                  <p>{appointment.annualRegistration.patient.contact || "—"}</p>
                </div>
            </div>

            {/* Lado direito */}
            <div className="flex flex-col gap-2 w-1/3">
                <div className="flex">
                  <p className="font-medium mr-2">NIS: </p>
                  <p>{appointment.annualRegistration.patient.nis || "—"}</p>
                </div>
                <div className="flex">
                  <p className="font-medium mr-2">Alergias: </p>
                  <p>{appointment.annualRegistration.patient.allergies || "—"}</p>
                </div>
                <div className="flex">
                  <p className="font-medium mr-2">Estudante: </p>
                  <p>{appointment.annualRegistration.patient.isStudent ? "Sim" : "Não"}</p>
                </div>
                <div className="flex">
                  <p className="font-medium mr-2">Guardião: </p>
                  <p>{appointment.annualRegistration.patient.guardian?.name || "Não possui"}</p>
                </div>
            </div>
          </div>
        </CardContent>
      </Card>

          {/* Card dados residenciais */}
        <Card className="text-[#0D4F97] mb-4">
            <CardHeader className="relative">
              <CardTitle className="absolute left-1/2 top-1/2 -translate-x-1/2 -translate-y-1/2 font-bold text-center text-lg md:text-xl">
                  Dados Residenciais
                </CardTitle>
                </CardHeader>
                <CardContent>
                  <div className="flex justify-between">
                    {/* Lado esquerdo */}
                    <div className="flex flex-col gap-2">
                        <div className="flex">
                          <p className="font-medium mr-2">Endereço: </p>
                          <p>{appointment.annualRegistration.patient.address?.street || "—"}</p>
                        </div>
                        <div className="flex">
                          <p className="font-medium mr-2">Número da residência: </p>
                          <p>{appointment.annualRegistration.patient.address?.number || "—"}</p>
                          
                        </div>
                        <div className="flex">
                          <p className="font-medium mr-2">Bairro: </p>
                          <p>{appointment.annualRegistration.patient.address?.neighborhood || "—"}</p>
                        </div>
                        <div className="flex">
                          <p className="font-medium mr-2">Cidade: </p>
                          <p>{appointment.annualRegistration.patient.address?.city || "—"}</p>
                        </div>
                    </div>
                    {/* Lado direito */}
                    <div className="flex flex-col gap-2 w-1/3">
                          <div className="flex">
                            <p className="font-medium mr-2">Complemento: </p>
                            <p>{appointment.annualRegistration.patient.address?.complement || "—"}</p>
                          </div>

                          <div className="flex">
                            <p className="font-medium mr-2">CEP: </p>
                            <p>{appointment.annualRegistration.patient.address?.cep || "—"}</p>
                          </div>

                          <div className="flex">
                            <p className="font-medium mr-2">Estado: </p>
                            <p>{appointment.annualRegistration.patient.address?.state || "—"}</p>
                          </div>
                    </div>
                  </div>
                </CardContent>
        </Card>
      </main>
    </div>
  );
}
