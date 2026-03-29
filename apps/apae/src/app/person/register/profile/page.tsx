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
import { EditProfile } from "@/schemas/edit-member-schemas"; 
import { zodResolver } from "@hookform/resolvers/zod";
import React, { useEffect, useState } from "react";
import { useForm } from "react-hook-form";
import { handleBackendValidationErrors } from "@/utils/form-errors";

import z from "zod";
import { FileInputButton, FormButton, MembersRegisterForm } from "../form";
import { Checkbox } from "@/components/ui/checkbox";
import { useRouter, useParams, usePathname } from "next/navigation"; 
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

  const params = useParams();
  const pathname = usePathname();
  const id = params?.id as string;
  const isEditing = pathname.includes("/edit");

  const currentSchema = isEditing ? EditProfile : Profile;

  const form = useForm<any>({
    mode: "onBlur",
    resolver: zodResolver(currentSchema),
    defaultValues: profile,
  });

  const [isInitialized, setIsInitialized] = useState(false);
  useEffect(() => {
    if (isEditing && profile.role && !isInitialized) {
      form.reset(profile);
      setIsInitialized(true);
    }
  }, [profile, form, isEditing, isInitialized]);

  useEffect(() => {
    if (submitted && profile) {
      (async () => {
        setIsLoading(true);
        try {
          const res = await register(id);

          if (res.status === 201 || res.status === 200 || res.status === 204) {
            toast.success(isEditing ? "Paciente atualizado com sucesso!" : "Membro cadastrado com sucesso!");
            
            router.push(isEditing ? `/person/${id}` : "/visualization-patients");
          }

          else if (res.status === 409) {
            const msg = res.data?.message || "";
            const msgLower = msg.toLowerCase();

            let targetField = "cpf";
            let displayMsg = "CPF ou documento já cadastrado no sistema.";

            if (msgLower.includes("rg") || msgLower.includes("identidade")) {
              targetField = "rg.number";
              displayMsg = "Este RG já está cadastrado no sistema.";
            } else if (msgLower.includes("cns")) {
              targetField = "cns";
              displayMsg = "Este CNS já está cadastrado no sistema.";
            } else if (msgLower.includes("cpf")) {
              targetField = "cpf";
              displayMsg = "Este CPF já está cadastrado no sistema.";
            }

            toast.error(displayMsg);
            setStep(MembersRegisterStep.PERSONAL);

            form.setError(targetField as any, {
              type: "manual",
              message: displayMsg,
            });

            setSubmitted(false);
          }

          else if (res.status === 400) {
            const firstError = res.data?.fields?.[0];
            const backendField = firstError?.field || "";
            const fieldLower = backendField.toLowerCase();
            const errorMessage = firstError?.message || "Erro de validação";

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
                  "phone",
                  "name",
                ].some((f) => fieldLower.includes(f.toLowerCase()))
              ) {
                setStep(MembersRegisterStep.PERSONAL);
              } else if (
                fieldLower.includes("parents") ||
                fieldLower.includes("kinships")
              ) {
                setStep(MembersRegisterStep.KINSHIPS);
              } else if (
                fieldLower.includes("address") &&
                !fieldLower.includes("guardian")
              ) {
                setStep(MembersRegisterStep.ADDRESS);
              } else if (
                [
                  "annualRegistry",
                  "vaccine",
                  "allergies",
                  "diseases",
                  "familyIncome",
                  "householdIncome",
                ].some((f) => fieldLower.includes(f.toLowerCase()))
              ) {
                setStep(MembersRegisterStep.ADDITIONALS);
              } else if (fieldLower.includes("guardian")) {
                setStep(MembersRegisterStep.GUARDIAN);
              }
            }

            toast.error(errorMessage);
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
  }, [submitted, profile, register, router, form.setError, setStep, id, isEditing]);

  const onSubmit = async (values: z.infer<typeof Profile>) => {
    setProfileData(values);
    setSubmitted(true);
  };

  return (
    <Form {...form}>
      <MembersRegisterForm
        title={isEditing ? "Finalizar Edição" : "Informações Importantes"}
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
              {isLoading ? "Salvando..." : (isEditing ? "Salvar Alterações" : "Salvar")}
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
                <FormLabel>Foto {isEditing ? "(Opcional na edição)" : "*"}</FormLabel>
                <FormControl>
                  <FileInputButton
                    id={field.name}
                    className="min-w-3xs !cursor-pointer bg-gradient-to-b hover:bg-zinc-300/55"  
                    disabled={isLoading}
                    onChange={(e) => {
                      if (e.target.files && e.target.files[0]) {
                        field.onChange(e.target.files[0]);
                      }
                    }}
                  >
                    {field.value instanceof File ? (
                      <span
                        className="truncate text-left"
                        title={field.value.name}
                      >
                        Arquivo selecionado: {field.value.name}
                      </span>
                    ) : typeof field.value === "string" && field.value ? (
                      <span className="truncate text-left" title="Foto atual">
                        Clique para alterar
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
                      disabled={isLoading}
                      onCheckedChange={() => field.onChange("patient")}
                    />
                  </FormControl>
                  <FormLabel className="cursor-pointer">Paciente</FormLabel>
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
                  <FormLabel className="cursor-pointer">Aluno</FormLabel>
                </FormItem>
              </FormItem>
            )}
          />
        </div>
      </MembersRegisterForm>
    </Form>
  );
}