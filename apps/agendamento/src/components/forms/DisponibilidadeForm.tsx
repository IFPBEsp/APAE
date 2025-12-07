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
import { Control, UseFormWatch } from "react-hook-form";

type Props = {
  control: Control<any>;
  watch: UseFormWatch<any>;
};

export default function Disponibilidade({ control, watch }: Readonly<Props>) {
  const disponibilidade = watch("disponibilidade") || [];

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
                    (d: any) => d.dia === dia.id && d.turno === turno.id
                  );

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
