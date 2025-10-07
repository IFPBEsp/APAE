"use client";

import {
  FormField,
  FormItem,
  FormControl,
  FormLabel,
  FormDescription,
} from "@/components/ui/form";
import { Table, TableBody, TableCell, TableHead, TableHeader, TableRow } from "@/components/ui/table";
import { Checkbox } from "@/components/ui/checkbox";
import { Control, UseFormWatch } from "react-hook-form";
import { useEffect } from "react";

export type DisponibilidadeType = {
  dia: string;
  turno: string;
  checked: boolean;
};

type DisponibilidadeProps = {
  control: Control<any>;
  watch: UseFormWatch<any>;
};

export const diasDaSemana = [
  { id: "segunda", label: "Segunda" },
  { id: "terca", label: "Terça" },
  { id: "quarta", label: "Quarta" },
  { id: "quinta", label: "Quinta" },
  { id: "sexta", label: "Sexta" },
];

export const turnos = [
  { id: "manha", label: "Manhã" },
  { id: "tarde", label: "Tarde" },
];

export default function Disponibilidade({ control, watch }: DisponibilidadeProps) {
  useEffect(() => {
    const currentDisponibilidade = watch('disponibilidade') || [];
    
    if (currentDisponibilidade.length === 0) {
      const initialDisponibilidade = diasDaSemana.flatMap((dia) =>
        turnos.map((turno) => ({
          dia: dia.id,
          turno: turno.id,
          checked: false,
        }))
      );
      
      control._formValues.disponibilidade = initialDisponibilidade;
      control._subjects.state.next({});
    }
  }, [control, watch]);

  const currentDisponibilidade = watch('disponibilidade') || [];

  return (
    <div className="space-y-4">
      <FormLabel>Disponibilidade</FormLabel>
      <FormDescription>
        Marque os dias e turnos em que o profissional está disponível.
      </FormDescription>

      <div className="border rounded-md">
        <Table>
          <TableHeader>
            <TableRow>
              <TableHead className="w-[100px]">Turno</TableHead>
              {diasDaSemana.map((dia) => (
                <TableHead key={dia.id} className="text-center">
                  {dia.label}
                </TableHead>
              ))}
            </TableRow>
          </TableHeader>

          <TableBody>
            {turnos.map((turno) => (
              <TableRow key={turno.id}>
                <TableCell className="font-medium">{turno.label}</TableCell>
                {diasDaSemana.map((dia) => {
                  const index = currentDisponibilidade.findIndex(
                    (d: DisponibilidadeType) => d.dia === dia.id && d.turno === turno.id
                  );

                  if (index === -1) {
                    return (
                      <TableCell key={dia.id} className="text-center">
                        <div className="flex items-center justify-center">
                          <Checkbox disabled />
                        </div>
                      </TableCell>
                    );
                  }

                  return (
                    <TableCell key={dia.id} className="text-center">
                      <FormField
                        control={control}
                        name={`disponibilidade.${index}.checked`}
                        render={({ field }) => (
                          <FormItem className="flex items-center justify-center">
                            <FormControl>
                              <Checkbox
                                checked={field.value}
                                onCheckedChange={field.onChange}
                              />
                            </FormControl>
                          </FormItem>
                        )}
                      />
                    </TableCell>
                  );
                })}
              </TableRow>
            ))}
          </TableBody>
        </Table>
      </div>
    </div>
  );
}