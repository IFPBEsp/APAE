"use client";

import { Check, X } from "lucide-react";
import { Button } from "@/components/ui/button";
import {
  AgendamentoCreateDTO,
  getAgendamentoById,
  saveAgendamento,
} from "@/app/services/agendamentoService";
import { useRouter } from "next/navigation";
import { useEffect, useState } from "react";

export default function ConfirmButton({ id }: { id: string }) {
  const [agendamentoDTO, setAgendamentoDTO] = useState<AgendamentoCreateDTO>();
  const router = useRouter();

  useEffect(() => {
    const fetchAgendamento = async () => {
      const agendamento = await getAgendamentoById(id);
      if (agendamento.paciente.id && agendamento.profissional.id) {
        const { paciente, profissional, ...dto } = {
          ...agendamento,
          idPaciente: agendamento.paciente.id,
          idProfissional: agendamento.profissional.id,
        };
        setAgendamentoDTO(dto);
      }
    };
    fetchAgendamento();
  }, []);

  const confirmarAgendamento = async () => {
    if (agendamentoDTO) {
      agendamentoDTO.confirmado = !agendamentoDTO.confirmado;
      await saveAgendamento(agendamentoDTO, id);
      router.refresh();
    }
  };

  return (
    <Button
      onClick={confirmarAgendamento}
      className={`bg-transparent cursor-pointer text-[${
        agendamentoDTO?.confirmado ? "#970D0D" : "#4bbd35" 
      }] active:text-[${
        agendamentoDTO?.confirmado ? "#c21111" :"#58e03d"
      }] hover:bg-[rgba(0,0,0,0.1)] transition-colors`}
    >
      {agendamentoDTO?.confirmado ? <X /> :<Check />}
    </Button>
  );
}
