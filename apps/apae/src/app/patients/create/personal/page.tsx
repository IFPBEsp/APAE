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
} from "@/hooks/use-members-register-context";
import {
  formatBirthCertificate,
  formatCNS,
  formatCPF,
  formatIssuingBody,
  formatPhone,
  formatRG,
  capitalizeFirst,
} from "@/lib/formats";
import { Personal, PersonalData } from "@/schemas/member-schemas";
import { zodResolver } from "@hookform/resolvers/zod";
import { useForm } from "react-hook-form";
import { useState, useEffect } from "react";
import { usePathname, useRouter  } from "next/navigation";
import { handleBackendValidationErrors } from "@/lib/utils/form-errors";
import { formatCivilDateDisplayValue } from "@/lib/date";
import { InputMask } from "@react-input/mask";

import { DoubleColumn, FormButton, MembersRegisterForm } from "../form";
import z from "zod";

export default function MembersRegisterPersonalPage() {
  const {
    state: { personal },
    setters: { setPersonalData, setStep },
  } = useMembersRegisterContext();

  const [isLoading, setIsLoading] = useState(false);

  const pathname = usePathname();
  const router = useRouter();
  const isEditing = pathname.includes("/edit");
  const form = useForm<z.infer<typeof Personal>>({
    mode: "onBlur",
    resolver: zodResolver(Personal),
    defaultValues: personal,
  });

  const [isInitialized, setIsInitialized] = useState(false);

  useEffect(() => {
    if (personal.name && !isInitialized) {
      form.reset(personal);
      setIsInitialized(true);
    }
  }, [personal, form, isInitialized]);

  useEffect(() => {
    if (personal.name !== "") {
      form.reset(personal);
    }
  }, [personal, form]);

  const onSubmit = async (values: z.infer<typeof Personal>) => {
    setIsLoading(true);
    try {
      setPersonalData(values as PersonalData);
      setStep(MembersRegisterStep.KINSHIPS);
    } catch (error: unknown) {
      const err = error as { response?: { data?: Record<string, string[]> } };
      if (err.response?.data) {
        handleBackendValidationErrors(err.response.data, form.setError);
      }
    } finally {
      setIsLoading(false);
    }
  };

  return (
    <Form {...form}>
      <MembersRegisterForm
        title={isEditing ? "Editar Dados Pessoais" : "Dados Pessoais"}
        onSubmit={form.handleSubmit(onSubmit)}
        buttons={
          <div className="flex gap-4">
            <FormButton
              type="button"
              disabled={isLoading}
              onClick={() => router.push("/patients")}
            >
              Voltar
            </FormButton>

            <FormButton type="submit" disabled={isLoading}>
              {isLoading ? "Validando..." : "Próximo"}
            </FormButton>
          </div>
        }
      >
        <DoubleColumn>
          <FormField
            control={form.control}
            name="name"
            render={({ field }) => (
              <FormItem className="md:col-span-2">
                <FormLabel>Nome Completo *</FormLabel>
                <FormControl>
                  <Input
                    placeholder="Digite o nome completo"
                    {...field}
                    onChange={(e) => field.onChange(capitalizeFirst(e.target.value))}
                  />
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
                  <Input
                    placeholder="Apenas 11 números"
                    {...field}
                    maxLength={11}
                    onChange={(e) => {
                      const value = e.target.value.replace(/\D/g, "");
                      field.onChange(value);
                    }}
                  />
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
                  <InputMask
                    mask="__/__/____"
                    replacement={{ _: /\d/ }}
                    inputMode="numeric"
                    placeholder="dd/mm/aaaa"
                    value={
                      typeof field.value === "string"
                        ? field.value
                        : formatCivilDateDisplayValue(field.value)
                    }
                    onChange={(e) => field.onChange(e.target.value)}
                    onBlur={field.onBlur}
                    className="file:text-foreground placeholder:text-muted-foreground selection:bg-primary selection:text-primary-foreground dark:bg-input/30 border-input flex h-9 w-full min-w-0 rounded-md border bg-transparent px-3 py-1 text-base font-sans shadow-xs transition-[color,box-shadow] outline-none file:inline-flex file:h-7 file:border-0 file:bg-transparent file:text-sm file:font-medium disabled:pointer-events-none disabled:cursor-not-allowed disabled:opacity-50 md:text-sm focus-visible:border-ring focus-visible:ring-ring/50 focus-visible:ring-[3px] aria-invalid:ring-destructive/20 dark:aria-invalid:ring-destructive/40 aria-invalid:border-destructive"
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
                  <InputMask
                    mask="__/__/____"
                    replacement={{ _: /\d/ }}
                    inputMode="numeric"
                    placeholder="dd/mm/aaaa"
                    value={
                      typeof field.value === "string"
                        ? field.value
                        : formatCivilDateDisplayValue(field.value)
                    }
                    onChange={(e) => field.onChange(e.target.value)}
                    onBlur={field.onBlur}
                    className="file:text-foreground placeholder:text-muted-foreground selection:bg-primary selection:text-primary-foreground dark:bg-input/30 border-input flex h-9 w-full min-w-0 rounded-md border bg-transparent px-3 py-1 text-base font-sans shadow-xs transition-[color,box-shadow] outline-none file:inline-flex file:h-7 file:border-0 file:bg-transparent file:text-sm file:font-medium disabled:pointer-events-none disabled:cursor-not-allowed disabled:opacity-50 md:text-sm focus-visible:border-ring focus-visible:ring-ring/50 focus-visible:ring-[3px] aria-invalid:ring-destructive/20 dark:aria-invalid:ring-destructive/40 aria-invalid:border-destructive"
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
                  <Input
                    placeholder="Brasil"
                    {...field}
                    onChange={(e) => field.onChange(capitalizeFirst(e.target.value))}
                  />
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
