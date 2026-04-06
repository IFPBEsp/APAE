"use client";

import {
  FormField,
  FormItem,
  FormControl,
  FormLabel,
  FormDescription,
} from "@/components/ui/form";
import {
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableHeader,
  TableRow,
} from "@/components/ui/table";
import { Checkbox } from "@/components/ui/checkbox";
import { diasDaSemana, turnos } from "@/types/profissional";
import { Control, UseFormWatch, FieldValues, Path } from "react-hook-form";


interface DisponibilidadeItem {
  dia: string;
  turno: string;
  checked?: boolean;
}

type Props<T extends FieldValues> = {
  control: Control<T>;
  watch: UseFormWatch<T>;
};

export default function Disponibilidade<T extends FieldValues>({ control, watch }: Readonly<Props<T>>) {
  const disponibilidade = (watch("disponibilidade" as Path<T>) || []) as DisponibilidadeItem[];

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
                  const index = disponibilidade.findIndex(
                    (d: DisponibilidadeItem) => d.dia === dia.id && d.turno === turno.id
                  );

                  return (
                    <TableCell key={dia.id} className="text-center">
                      <FormField
                        control={control}
                        name={`disponibilidade.${index}.checked` as Path<T>}
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
