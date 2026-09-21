import {
  diasDaSemana,
  AvailabilityMatrix,
  turnos,
  AvailabilityDTO,
} from "@/types/profissional";

export function gerarMatrizDisponibilidade(lista: AvailabilityMatrix[]) {
  return diasDaSemana.flatMap((dia) =>
    turnos.map((turno) => {
      const existente = lista.find((d) => {
        return d.dia === dia.id && d.turno === turno.id;
      });

      return (
        existente ?? {
          dia: dia.id,
          turno: turno.id,
          checked: false,
        }
      );
    }),
  );
}

export function gerarMatrizDisponibilidadeFromBackend(
  avs: AvailabilityDTO[] = []
): AvailabilityMatrix[] {
  const lista: AvailabilityMatrix[] = avs.map((a) => ({
    dia: a.day?.toLowerCase() || "",
    turno: a.shift?.toLowerCase() || "",
    checked: true,
  }));
  return gerarMatrizDisponibilidade(lista);
}