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
import { formatCEP } from "@/lib/formats";
import { Guardian } from "@/schemas/member-schemas";
import { zodResolver } from "@hookform/resolvers/zod";
import React, { useState } from "react";
import { useForm } from "react-hook-form";
import { handleBackendValidationErrors } from "@/utils/form-errors";

import z from "zod";
import { DoubleColumn, FormButton, MembersRegisterForm } from "../form";

export default function MembersRegisterGuardianPage() {
  const {
    state: { guardian },
    setters: { setGuardianData, setStep },
  } = useMembersRegisterContext();

  const [isLoading, setIsLoading] = useState(false);

  const form = useForm<z.infer<typeof Guardian>>({
    mode: "onBlur",
    resolver: zodResolver(Guardian),
    defaultValues: guardian,
  });

  const onSubmit = async (values: z.infer<typeof Guardian>) => {
    setIsLoading(true);
    try {
      setGuardianData(values);
      setStep(MembersRegisterStep.PROFILE);
    } catch (error: any) {
      if (error.response?.data) {
        handleBackendValidationErrors(error.response.data, form.setError);
      }
    } finally {
      setIsLoading(false);
    }
  };

  return (
    <Form {...form}>
      <MembersRegisterForm
        title="Dados do Responsável"
        onSubmit={form.handleSubmit(onSubmit)}
        buttons={
          <>
            <FormButton
              type="button"
              onClick={() => setStep(MembersRegisterStep.ADDITIONALS)}
              disabled={isLoading}
            >
              Voltar
            </FormButton>

            <FormButton type="submit" disabled={isLoading}>
              {isLoading ? "Validando..." : "Próximo"}
            </FormButton>
          </>
        }
      >
        <DoubleColumn>
          <FormField
            control={form.control}
            name="name"
            render={({ field }) => (
              <FormItem>
                <FormLabel>Nome Completo *</FormLabel>
                <FormControl>
                  <Input
                    placeholder="Digite o nome completo do responsável"
                    {...field}
                  />
                </FormControl>
                <FormMessage />
              </FormItem>
            )}
          />

          <FormField
            control={form.control}
            name="contact"
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

          <FormField
            control={form.control}
            name="address.street"
            render={({ field }) => (
              <FormItem>
                <FormLabel>Rua *</FormLabel>
                <FormControl>
                  <Input placeholder="Adielson Assis Alves, 49" {...field} />
                </FormControl>
                <FormMessage />
              </FormItem>
            )}
          />

          <FormField
            control={form.control}
            name="address.cep"
            render={({ field }) => (
              <FormItem>
                <FormLabel>CEP *</FormLabel>
                <FormControl>
                  <Input
                    placeholder="58135-000"
                    maxLength={9}
                    value={field.value}
                    onChange={(e) => {
                      const formated = formatCEP(e.target.value);
                      field.onChange(formated);
                    }}
                  />
                </FormControl>
                <FormMessage />
              </FormItem>
            )}
          />

          <FormField
            control={form.control}
            name="address.state"
            render={({ field }) => (
              <FormItem>
                <FormLabel>Estado *</FormLabel>
                <FormControl>
                  <Input placeholder="Paraiba" {...field} />
                </FormControl>
                <FormMessage />
              </FormItem>
            )}
          />

          <FormField
            control={form.control}
            name="address.city"
            render={({ field }) => (
              <FormItem>
                <FormLabel>Cidade *</FormLabel>
                <FormControl>
                  <Input placeholder="Esperança" {...field} />
                </FormControl>
                <FormMessage />
              </FormItem>
            )}
          />

          <FormField
            control={form.control}
            name="address.district"
            render={({ field }) => (
              <FormItem>
                <FormLabel>Bairro *</FormLabel>
                <FormControl>
                  <Input placeholder="Centro" {...field} />
                </FormControl>
                <FormMessage />
              </FormItem>
            )}
          />

          <FormField
            control={form.control}
            name="kinship"
            render={({ field }) => (
              <FormItem>
                <FormLabel>Parentesco *</FormLabel>
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
