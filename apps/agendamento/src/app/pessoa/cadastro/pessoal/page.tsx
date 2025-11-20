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
} from "@/./../management-app/src/hooks/use-members-register-context";
import {
  formatBirthCertificate,
  formatCNS,
  formatCPF,
  formatIssuingBody,
  formatPhone,
  formatRG,
} from "@/lib/formats";
import { Personal } from "@/schemas/member-schemas";
import { zodResolver } from "@hookform/resolvers/zod";
import { useForm } from "react-hook-form";
import z from "zod";

import { DoubleColumn, FormButton, MembersRegisterForm } from "../form";

export default function MembersRegisterPersonalPage() {
  const {
    state: { personal },
    setters: { setPersonalData, setStep },
  } = useMembersRegisterContext();

  const form = useForm<z.infer<typeof Personal>>({
    mode: "onBlur",
    resolver: zodResolver(Personal),
    defaultValues: personal,
  });

  const onSubmit = (values: z.infer<typeof Personal>) => {
    setPersonalData(values);
    setStep(MembersRegisterStep.ADDRESS);
  };

  return (
    <Form {...form}>
      <MembersRegisterForm
        title="Dados Pessoais"
        buttons={<FormButton type="submit">Próximo</FormButton>}
        onSubmit={form.handleSubmit(onSubmit)}
      >
        <DoubleColumn>
          <FormField
            control={form.control}
            name="name"
            render={({ field }) => (
              <FormItem className="md:col-span-2">
                <FormLabel>Nome Completo *</FormLabel>
                <FormControl>
                  <Input placeholder="Digite o nome completo" {...field} />
                </FormControl>
                <FormMessage />
              </FormItem>
            )}
          />

          <FormField
            control={form.control}
            name="cpf"
            render={({ field }) => (
              <FormItem>
                <FormLabel>CPF *</FormLabel>
                <FormControl>
                  <Input
                    placeholder="000.000.000-00"
                    maxLength={14}
                    value={field.value}
                    onChange={(e) => {
                      const formatted = formatCPF(e.target.value);
                      field.onChange(formatted);
                    }}
                  />
                </FormControl>
                <FormMessage />
              </FormItem>
            )}
          />

          <FormField
            control={form.control}
            name="cns"
            render={({ field }) => (
              <FormItem>
                <FormLabel>CNS *</FormLabel>
                <FormControl>
                  <Input
                    placeholder="Apenas números"
                    maxLength={18}
                    value={field.value}
                    onChange={(e) => {
                      const formatted = formatCNS(e.target.value);
                      field.onChange(formatted);
                    }}
                  />
                </FormControl>
                <FormMessage />
              </FormItem>
            )}
          />

          <FormField
            control={form.control}
            name="nis"
            render={({ field }) => (
              <FormItem>
                <FormLabel>NIS *</FormLabel>
                <FormControl>
                  <Input placeholder="Digite o NIS" {...field} />
                </FormControl>
                <FormMessage />
              </FormItem>
            )}
          />

          <FormField
            control={form.control}
            name="rg.number"
            render={({ field }) => (
              <FormItem>
                <FormLabel>RG *</FormLabel>
                <FormControl>
                  <Input
                    placeholder="1.234.567"
                    maxLength={9}
                    value={field.value}
                    onChange={(e) => {
                      const formatted = formatRG(e.target.value);
                      field.onChange(formatted);
                    }}
                  />
                </FormControl>
                <FormMessage />
              </FormItem>
            )}
          />

          <FormField
            control={form.control}
            name="rg.issuing.body"
            render={({ field }) => (
              <FormItem>
                <FormLabel>Órgão Emissor *</FormLabel>
                <FormControl>
                  <Input
                    placeholder="SSP/SP"
                    maxLength={7}
                    value={field.value}
                    onChange={(e) => {
                      const formatted = formatIssuingBody(e.target.value);
                      field.onChange(formatted);
                    }}
                  />
                </FormControl>
                <FormMessage />
              </FormItem>
            )}
          />

          <FormField
            control={form.control}
            name="rg.issuing.date"
            render={({ field }) => (
              <FormItem>
                <FormLabel>Data de Emissão *</FormLabel>
                <FormControl>
                  <Input
                    type="date"
                    {...field}
                    value={
                      field.value instanceof Date &&
                      !isNaN(field.value.getTime())
                        ? field.value.toISOString().split("T")[0]
                        : ""
                    }
                    onChange={(e) => field.onChange(new Date(e.target.value))}
                  />
                </FormControl>
                <FormMessage />
              </FormItem>
            )}
          />

          <FormField
            control={form.control}
            name="birth.certificate"
            render={({ field }) => (
              <FormItem>
                <FormLabel>Certidão de Nascimento *</FormLabel>
                <FormControl>
                  <Input
                    placeholder="Apenas números"
                    value={field.value}
                    onChange={(e) => {
                      const formatted = formatBirthCertificate(e.target.value);
                      field.onChange(formatted);
                    }}
                  />
                </FormControl>
                <FormMessage />
              </FormItem>
            )}
          />

          <FormField
            control={form.control}
            name="birth.date"
            render={({ field }) => (
              <FormItem>
                <FormLabel>Data de Nascimento *</FormLabel>
                <FormControl>
                  <Input
                    type="date"
                    {...field}
                    value={
                      field.value instanceof Date &&
                      !isNaN(field.value.getTime())
                        ? field.value.toISOString().split("T")[0]
                        : ""
                    }
                    onChange={(e) => field.onChange(new Date(e.target.value))}
                  />
                </FormControl>
                <FormMessage />
              </FormItem>
            )}
          />

          <FormField
            control={form.control}
            name="birth.place"
            render={({ field }) => (
              <FormItem>
                <FormLabel>Naturalidade *</FormLabel>
                <FormControl>
                  <Input placeholder="Brasil" {...field} />
                </FormControl>
                <FormMessage />
              </FormItem>
            )}
          />

          <FormField
            control={form.control}
            name="phone"
            render={({ field }) => (
              <FormItem>
                <FormLabel>Telefone *</FormLabel>
                <FormControl>
                  <Input
                    placeholder="(00) 00000-0000"
                    type="tel"
                    maxLength={15}
                    value={field.value}
                    onChange={(e) => {
                      const formatted = formatPhone(e.target.value);
                      field.onChange(formatted);
                    }}
                  />
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
