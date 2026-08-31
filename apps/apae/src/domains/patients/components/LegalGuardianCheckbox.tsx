import { useEffect } from "react";
import { FormItem, FormControl, FormLabel, FormDescription } from "@/components/ui/form";
import { Checkbox } from "@/components/ui/checkbox";
import { UseFormReturn } from "react-hook-form";
import z from "zod";
import { Kinships } from "@/domains/patients/schemas/member-schemas";

export interface LegalGuardianCheckboxProps {
  form: UseFormReturn<z.infer<typeof Kinships>>;
  index: number;
  setGuardianData: (data: {
    name: string;
    kinship: string;
    contact: string;
    address: {
      cep: string;
      state: string;
      city: string;
      neighborhood: string;
      district: string;
      street: string;
      number: string;
      complement: string;
      noNumber: boolean;
    };
  }) => void;
}

export function LegalGuardianCheckbox({
  form,
  index,
  setGuardianData,
}: LegalGuardianCheckboxProps) {
  const isAlive = form.watch(`kinships.${index}.alive`);
  const isLegalGuardian = form.watch(`kinships.${index}.isLegalGuardian`);

  const fieldOnChange = (value: boolean) => {
    form.setValue(`kinships.${index}.isLegalGuardian`, value);
  };

  useEffect(() => {
    if (!isAlive && isLegalGuardian) {
      fieldOnChange(false);
      setGuardianData({
        name: "",
        kinship: "",
        contact: "",
        address: {
          cep: "",
          state: "",
          city: "",
          neighborhood: "",
          district: "",
          street: "",
          number: "",
          complement: "",
          noNumber: false,
        },
      });
    }
  }, [isAlive, isLegalGuardian, setGuardianData]);

  return (
    <FormItem
      className={`flex flex-row items-center space-x-3 space-y-0 rounded-md border p-4 shadow-sm ${
        !isAlive ? "border-gray-200/40 opacity-50" : "border-gray-300/60"
      }`}
    >
      <FormControl>
        <Checkbox
          className="border-zinc-300"
          checked={isLegalGuardian}
          disabled={!isAlive}
          onCheckedChange={(checked) => {
            if (checked) {
              const currentKinships = form.getValues("kinships");
              currentKinships.forEach((_, i) => {
                if (i !== index) {
                  form.setValue(`kinships.${i}.isLegalGuardian`, false);
                }
              });
              fieldOnChange(checked as boolean);
            } else {
              fieldOnChange(checked);
              setGuardianData({
                name: "",
                kinship: "",
                contact: "",
                address: {
                  cep: "",
                  state: "",
                  city: "",
                  neighborhood: "",
                  district: "",
                  street: "",
                  number: "",
                  complement: "",
                  noNumber: false,
                },
              });
            }
          }}
        />
      </FormControl>
      <div className="space-y-1 leading-none">
        <FormLabel className={!isAlive ? "text-gray-400" : ""}>
          Este parente é o Responsável Legal do paciente?
        </FormLabel>
        <FormDescription>
          {!isAlive
            ? "Não é possível definir um parente falecido como responsável legal."
            : "Apenas uma pessoa pode ser marcada como o contato principal e responsável legal."}
        </FormDescription>
      </div>
    </FormItem>
  );
}
