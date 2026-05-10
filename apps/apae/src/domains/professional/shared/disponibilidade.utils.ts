import {
  diasDaSemana,
  AvailabilityType,
  turnos,
} from "@/types/profissional";

export function gerarMatrizDisponibilidade(lista: AvailabilityType[]) {
  return diasDaSemana.flatMap((dia) =>
    turnos.map((turno) => {
      const existente = lista.find((d) => {
        return d.dia == dia.id && d.turno == turno.id;
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
