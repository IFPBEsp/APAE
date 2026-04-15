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
import { EditGuardian } from "@/schemas/edit-member-schemas";
import { zodResolver } from "@hookform/resolvers/zod";
import React, { useState, useEffect } from "react";
import { useForm } from "react-hook-form";
import { handleBackendValidationErrors } from "@/utils/form-errors";
import { usePathname } from "next/navigation";
import { formatPhone } from "@/lib/formats";
import { DoubleColumn, FormButton, MembersRegisterForm } from "../form";

export default function MembersRegisterGuardianPage() {
  const {
    state: { guardian },
    setters: { setGuardianData, setStep },
  } = useMembersRegisterContext();

  const [isLoading, setIsLoading] = useState(false);

  const pathname = usePathname();
  const isEditing = pathname.includes("/edit");
  const currentSchema = isEditing ? EditGuardian : Guardian;

  const form = useForm<any>({
    mode: "onBlur",
    resolver: zodResolver(currentSchema),
    defaultValues: guardian,
  });

  const [isInitialized, setIsInitialized] = useState(false);

  useEffect(() => {
    if (isEditing && guardian.name && !isInitialized) {
      form.reset(guardian);
      setIsInitialized(true);
    }
  }, [isEditing, guardian, form, isInitialized]);

  useEffect(() => {
    if (guardian.name !== "") {
      form.reset(guardian);
    }
  }, [guardian, form]);

  const onSubmit = async (values: any) => {
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
        title={isEditing ? "Editar Dados do Responsável" : "Dados do Responsável"}
        onSubmit={form.handleSubmit(onSubmit)}
        buttons={
          <>
            <FormButton
              type="button"
              onClick={() => {
                const destino = isEditing
                  ? MembersRegisterStep.ADDRESS
                  : MembersRegisterStep.ADDITIONALS;
                setStep(destino);
              }}
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
              <FormItem className="md:col-span-2">
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
                    placeholder="(00) 00000-0000"
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

          <FormField
            control={form.control}
            name="kinship"
            render={({ field }) => (
              <FormItem>
                <FormLabel>Parentesco *</FormLabel>
                <FormControl>
                  <Input
                    placeholder="Mãe, Pai, Irmã, etc."
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
              <FormItem className="md:col-span-2">
                <FormLabel>Rua *</FormLabel>
                <FormControl>
                  <Input placeholder="Rua exemplo, 123" {...field} />
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
                      const formatted = formatCEP(e.target.value);
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
            name="address.state"
            render={({ field }) => (
              <FormItem>
                <FormLabel>Estado *</FormLabel>
                <FormControl>
                  <Input placeholder="Paraíba" {...field} />
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
        </DoubleColumn>
      </MembersRegisterForm>
    </Form>
  );
}
