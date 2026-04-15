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
import { formatCPF, formatRG } from "@/lib/formats";
import { Kinships } from "@/schemas/member-schemas";
import { zodResolver } from "@hookform/resolvers/zod";
import React, { useState, useEffect } from "react";
import { useFieldArray, useForm } from "react-hook-form";
import { handleBackendValidationErrors } from "@/utils/form-errors";

import z from "zod";
import {
  DoubleCheckboxFormField,
  DoubleColumn,
  FormButton,
  MembersRegisterForm,
} from "../form";
import { Button } from "@/components/ui/button";

export default function MembersRegisterKinshipsPage() {
  const {
    state: { kinships },
    setters: { setKinshipsData, setStep },
  } = useMembersRegisterContext();

  const [isLoading, setIsLoading] = useState(false);

  const form = useForm<z.infer<typeof Kinships>>({
    mode: "onBlur",
    resolver: zodResolver(Kinships),
    defaultValues: {
      kinships,
    },
  });
  useEffect(() => {
    if (kinships.length > 0) {
      form.reset({ kinships }); 
    }
  }, [kinships, form]);

  const { fields, append, remove } = useFieldArray({
    control: form.control,
    name: "kinships",
  });

  const onSubmit = async (values: z.infer<typeof Kinships>) => {
    setIsLoading(true);
    try {
      setKinshipsData(values.kinships);
      setStep(MembersRegisterStep.ADDRESS);
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
        title="Dados dos Parentes"
        onSubmit={form.handleSubmit(onSubmit)}
        buttons={
          <>
            <FormButton
              type="button"
              onClick={() => setStep(MembersRegisterStep.PERSONAL)}
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
        {fields.map((item, index) => (
          <DoubleColumn key={item.id}>
            <FormField
              control={form.control}
              name={`kinships.${index}.name`}
              render={({ field }) => (
                <FormItem>
                  <FormLabel>Nome Completo *</FormLabel>
                  <FormControl>
                    <Input
                      placeholder="Digite o nome completo do parente"
                      {...field}
                    />
                  </FormControl>
                  <FormMessage />
                </FormItem>
              )}
            />

            <FormField
              control={form.control}
              name={`kinships.${index}.rg`}
              render={({ field }) => (
                <FormItem>
                  <FormLabel>RG do parente *</FormLabel>
                  <FormControl>
                    <Input
                      placeholder="1.234.567"
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
              name={`kinships.${index}.occupation`}
              render={({ field }) => (
                <FormItem>
                  <FormLabel>Profissão? *</FormLabel>
                  <FormControl>
                    <Input placeholder="Profissão" {...field} />
                  </FormControl>
                  <FormMessage />
                </FormItem>
              )}
            />

            <FormField
              control={form.control}
              name={`kinships.${index}.cpf`}
              render={({ field }) => (
                <FormItem>
                  <FormLabel>CPF do parente *</FormLabel>
                  <FormControl>
                    <Input
                      placeholder="000.000.000-00"
                      maxLength={14}
                      value={field.value}
                      onChange={(e) =>
                        field.onChange(formatCPF(e.target.value))
                      }
                    />
                  </FormControl>
                  <FormMessage />
                </FormItem>
              )}
            />

            <FormField
              control={form.control}
              name={`kinships.${index}.type`}
              render={({ field }) => (
                <FormItem>
                  <FormLabel>Parentesco *</FormLabel>
                  <FormControl>
                    <Input placeholder="Digite o parentesco" {...field} />
                  </FormControl>
                  <FormMessage />
                </FormItem>
              )}
            />

            <FormItem>
              <FormControl>
                <Button
                  className="bg-transparent mt-5.5"
                  onClick={() => remove(index)}
                  type="button"
                  variant="outline"
                  disabled={isLoading}
                >
                  Remover parente
                </Button>
              </FormControl>
              <FormMessage />
            </FormItem>

            <div className="md:col-span-2">
              <DoubleCheckboxFormField
                control={form.control}
                name={`kinships.${index}.alive`}
                labels={{
                  main: "Vivo? *",
                  true: "Sim",
                  false: "Não",
                }}
              />
            </div>
          </DoubleColumn>
        ))}

        <button
          type="button"
          onClick={() =>
            append({
              name: "",
              rg: "",
              cpf: "",
              alive: true,
              occupation: "",
              type: "",
            })
          }
          disabled={isLoading}
          className="rounded px-3 py-2 border disabled:opacity-50"
        >
          Adicionar Parente
        </button>
      </MembersRegisterForm>
    </Form>
  );
}
