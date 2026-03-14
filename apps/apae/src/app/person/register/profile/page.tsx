"use client";

import {
  Form,
  FormControl,
  FormField,
  FormItem,
  FormLabel,
  FormMessage,
} from "@/components/ui/form";
import {
  MembersRegisterStep,
  useMembersRegisterContext,
} from "@/hooks/use-members-register-context";
import { Profile } from "@/schemas/member-schemas";
import { zodResolver } from "@hookform/resolvers/zod";
import React, { useEffect, useState } from "react";
import { useForm } from "react-hook-form";
import { handleBackendValidationErrors } from "@/utils/form-errors";

import z from "zod";
import { FileInputButton, FormButton, MembersRegisterForm } from "../form";
import { Checkbox } from "@/components/ui/checkbox";
import { useRouter } from "next/navigation";
import { toast } from "react-toastify";

export default function MembersRegisterProfilePage() {
  const {
    state: { profile },
    setters: { setProfileData, setStep },
    register,
  } = useMembersRegisterContext();
  const [submitted, setSubmitted] = useState(false);
  const [isLoading, setIsLoading] = useState(false);
  const router = useRouter();

  const form = useForm<z.infer<typeof Profile>>({
    mode: "onBlur",
    resolver: zodResolver(Profile),
    defaultValues: profile,
  });

  useEffect(() => {
    if (submitted && profile) {
      (async () => {
        setIsLoading(true);
        try {
          const res = await register();

          if (res.status === 201 || res.status === 200) {
            toast.success("Membro cadastrado com sucesso!");
            router.push("/visualization-patients");
          } else if (res.status === 400) {
            const firstError = res.data?.fields?.[0];
            const backendField = firstError?.field || "";
            const errorMessage = firstError?.message || "Erro de validação";

            toast.error(`Erro no campo [${backendField}]: ${errorMessage}`);

            if (backendField) {
              if (
                [
                  "fullName",
                  "cpf",
                  "rg",
                  "contact",
                  "birth",
                  "nationality",
                  "cns",
                  "nis",
                ].some((f) => backendField.includes(f))
              ) {
                setStep(MembersRegisterStep.PERSONAL);
              } else if (
                backendField.includes("parents") ||
                backendField.includes("kinships")
              ) {
                setStep(MembersRegisterStep.KINSHIPS);
              } else if (
                backendField.includes("address") &&
                !backendField.includes("guardian")
              ) {
                setStep(MembersRegisterStep.ADDRESS);
              } else if (
                [
                  "annualRegistry",
                  "vaccine",
                  "allergies",
                  "diseases",
                  "familyIncome",
                ].some((f) => backendField.includes(f))
              ) {
                setStep(MembersRegisterStep.ADDITIONALS);
              } else if (backendField.includes("guardian")) {
                setStep(MembersRegisterStep.GUARDIAN);
              }
            }

            handleBackendValidationErrors(res.data, form.setError);
            setSubmitted(false);
          } else {
            toast.error(res.data?.message || "Erro inesperado no servidor.");
            setSubmitted(false);
          }
        } catch (error) {
          toast.error("Falha na conexão com o servidor.");
          setSubmitted(false);
        } finally {
          setIsLoading(false);
        }
      })();
    }
  }, [submitted, profile, register, router, form.setError, setStep]);

  const onSubmit = async (values: z.infer<typeof Profile>) => {
    setProfileData(values);
    setSubmitted(true);
  };

  return (
    <Form {...form}>
      <MembersRegisterForm
        title="Informações Importantes"
        onSubmit={form.handleSubmit(onSubmit)}
        buttons={
          <>
            <FormButton
              type="button"
              onClick={() => setStep(MembersRegisterStep.GUARDIAN)}
              disabled={isLoading}
            >
              Voltar
            </FormButton>

            <FormButton type="submit" disabled={isLoading}>
              {isLoading ? "Salvando..." : "Salvar"}
            </FormButton>
          </>
        }
      >
        <div className="grid grid-cols-1 gap-6">
          <FormField
            control={form.control}
            name="photo"
            render={({ field }) => (
              <FormItem>
                <FormLabel>Adicione uma foto *</FormLabel>
                {/* [Caso o seletor de arquivos esteja com problemas]
                  
                  <Input
                    id={field.name}
                    type="file"
                    accept="application/pdf"
                    onChange={(e) =>
                      field.onChange(e.target.files?.[0] ?? null)
                    }
                  /> */}
                <FormControl>
                  <FileInputButton
                    id={field.name}
                    className="min-w-3xs"
                    disabled={isLoading}
                    onChange={(e) => {
                      if (e.target.files && e.target.files[0]) {
                        field.onChange(e.target.files[0]);
                      }
                    }}
                  >
                    {field.value ? (
                      <span
                        className="truncate text-left"
                        title={field.value.name}
                      >
                        Arquivo selecionado: {field.value.name}
                      </span>
                    ) : (
                      "Selecionar Foto"
                    )}
                  </FileInputButton>
                </FormControl>
                <FormMessage />
              </FormItem>
            )}
          />

          <FormField
            control={form.control}
            name="role"
            render={({ field }) => (
              <FormItem className="space-y-3">
                <FormLabel>
                  Selecione qual função será ocupada na aplicação? *
                </FormLabel>
                <FormItem className="flex flex-row items-center gap-2">
                  <FormControl>
                    <Checkbox
                      checked={
                        field.value === "patient" || field.value === "student"
                      }
                      disabled={field.value === "student" || isLoading}
                      onCheckedChange={() => field.onChange("patient")}
                    />
                  </FormControl>
                  <FormLabel>Paciente</FormLabel>
                </FormItem>
                <FormItem className="flex flex-row items-center gap-2">
                  <FormControl>
                    <Checkbox
                      checked={field.value === "student"}
                      disabled={isLoading}
                      onCheckedChange={(checked) =>
                        field.onChange(checked ? "student" : "patient")
                      }
                    />
                  </FormControl>
                  <FormLabel>Aluno</FormLabel>
                </FormItem>
              </FormItem>
            )}
          />
        </div>
      </MembersRegisterForm>
    </Form>
  );
}
