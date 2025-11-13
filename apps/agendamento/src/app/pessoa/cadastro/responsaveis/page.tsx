"use client";

import {
  Form,
  FormControl,
  FormField,
  FormItem,
  FormLabel,
  FormMessage,
} from "@/components/ui/form";
import { Input } from "@/components/ui/input";
import {
  MembersRegisterStep,
  useMembersRegisterContext,
} from "@/../../management-app/src/hooks/use-members-register-context";
import { formatCPF, formatCurrency, formatRG } from "@/lib/formats";
import { Guardians } from "@/app/schemas/member-schemas";
import { zodResolver } from "@hookform/resolvers/zod";
import React from "react";
import { useForm, UseFormReturn } from "react-hook-form";

import z from "zod";
import {
  DoubleCheckboxFormField,
  DoubleColumn,
  FormButton,
  MembersRegisterForm,
} from "../form";

function GuardianFormFields({
  form,
  name,
  labels,
  placeholders,
}: {
  form: UseFormReturn<z.infer<typeof Guardians>>;
  name: "father" | "mother";
  labels?: {
    name?: string;
    rg?: string;
    cpf?: string;
    occupation?: string;
  };
  placeholders?: {
    name?: string;
    rg?: string;
    cpf?: string;
    occupation?: string;
    whereToFind?: string;
  };
}) {
  return (
    <>
      <FormField
        control={form.control}
        name={`${name}.name`}
        render={({ field }) => (
          <FormItem>
            <FormLabel>{labels?.name ?? "Nome Completo"} *</FormLabel>
            <FormControl>
              <Input
                placeholder={placeholders?.name ?? "Digite o nome completo"}
                {...field}
              />
            </FormControl>
            <FormMessage />
          </FormItem>
        )}
      />

      <FormField
        control={form.control}
        name={`${name}.rg`}
        render={({ field }) => (
          <FormItem>
            <FormLabel>{labels?.rg ?? "RG"} *</FormLabel>
            <FormControl>
              <Input
                placeholder={placeholders?.rg ?? "1.234.567"}
                maxLength={9}
                value={field.value}
                onChange={(e) => field.onChange(formatRG(e.target.value))}
              />
            </FormControl>
            <FormMessage />
          </FormItem>
        )}
      />

      <FormField
        control={form.control}
        name={`${name}.occupation`}
        render={({ field }) => (
          <FormItem>
            <FormLabel>{labels?.occupation ?? "Profissão?"} *</FormLabel>
            <FormControl>
              <Input
                placeholder={placeholders?.occupation ?? "Profissão"}
                {...field}
              />
            </FormControl>
            <FormMessage />
          </FormItem>
        )}
      />

      <FormField
        control={form.control}
        name={`${name}.cpf`}
        render={({ field }) => (
          <FormItem>
            <FormLabel>{labels?.cpf ?? "CPF"} *</FormLabel>
            <FormControl>
              <Input
                placeholder={placeholders?.cpf ?? "000.000.000-00"}
                maxLength={14}
                value={field.value}
                onChange={(e) => field.onChange(formatCPF(e.target.value))}
              />
            </FormControl>
            <FormMessage />
          </FormItem>
        )}
      />

      <FormField
        control={form.control}
        name={`${name}.whereToFind`}
        render={({ field }) => (
          <FormItem>
            <FormLabel>Onde procurar em caso de emergência? *</FormLabel>
            <FormControl>
              <Input
                placeholder={placeholders?.whereToFind ?? "Casa."}
                {...field}
              />
            </FormControl>
            <FormMessage />
          </FormItem>
        )}
      />

      <FormField
        control={form.control}
        name={`${name}.emergencyContact`}
        render={({ field }) => (
          <FormItem>
            <FormLabel>Contato de Emergência *</FormLabel>
            <FormControl>
              <Input
                placeholder="Número de telefone, email e etc."
                {...field}
              />
            </FormControl>
            <FormMessage />
          </FormItem>
        )}
      />

      <div className="md:col-span-2">
        <DoubleCheckboxFormField
          control={form.control}
          name={`${name}.alive`}
          labels={{
            main: "Vivo? *",
            true: "Sim",
            false: "Não",
          }}
        />
      </div>
    </>
  );
}

export default function MembersRegisterGuardiansPage() {
  const {
    state: { guardians },
    setters: { setGuardiansData, setStep },
  } = useMembersRegisterContext();

  const form = useForm<z.infer<typeof Guardians>>({
    mode: "onBlur",
    resolver: zodResolver(Guardians),
    defaultValues: guardians,
  });

  const onSubmit = (values: z.infer<typeof Guardians>) => {
    setGuardiansData(values);
    setStep(MembersRegisterStep.PROFILE);
  };

  return (
    <Form {...form}>
      <MembersRegisterForm
        title="Dados dos Responsáveis"
        onSubmit={form.handleSubmit(onSubmit)}
        buttons={
          <>
            <FormButton
              type="button"
              onClick={() => setStep(MembersRegisterStep.ADDITIONALS)}
            >
              Voltar
            </FormButton>

            <FormButton type="submit">Próximo</FormButton>
          </>
        }
      >
        <DoubleColumn>
          <GuardianFormFields
            form={form}
            name="father"
            labels={{
              name: "Nome Completo do Pai",
              rg: "RG do Pai",
              cpf: "CPF do Pai",
            }}
            placeholders={{
              name: "Davi Firmino Silva",
              rg: "6.135.878",
              cpf: "704.780.123-06",
              occupation: "Professor",
              whereToFind: "Escola.",
            }}
          />
          <GuardianFormFields
            form={form}
            name="mother"
            labels={{
              name: "Nome Completo da Mãe",
              rg: "RG da Mãe",
              cpf: "CPF da Mãe",
            }}
            placeholders={{
              name: "Karla Firmino Silva",
              rg: "7.436.456",
              cpf: "804.680.103-02",
              occupation: "Advogada",
              whereToFind: "Consultório.",
            }}
          />

          <FormField
            control={form.control}
            name="householdIncome"
            render={({ field }) => (
              <FormItem>
                <FormLabel>Renda Familiar *:</FormLabel>
                <FormControl>
                  <Input
                    placeholder="R$ 2.100,00"
                    maxLength={15}
                    value={field.value}
                    onChange={(e) =>
                      field.onChange(formatCurrency(e.target.value))
                    }
                  />
                </FormControl>
                <FormMessage />
              </FormItem>
            )}
          />

          <FormField
            control={form.control}
            name="others"
            render={({ field }) => (
              <FormItem>
                <FormLabel>Outros Responsáveis:</FormLabel>
                <FormControl>
                  <Input placeholder="Irmã" {...field} />
                </FormControl>
                <FormMessage />
              </FormItem>
            )}
          />
        </DoubleColumn>
      </MembersRegisterForm>
    </Form>
  );
}
