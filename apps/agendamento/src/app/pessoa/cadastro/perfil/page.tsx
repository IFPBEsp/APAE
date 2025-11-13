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
} from "@/../../management-app/src/hooks/use-members-register-context";
import { Profile } from "@/app/schemas/member-schemas";
import { zodResolver } from "@hookform/resolvers/zod";
import React, { useEffect, useState } from "react";
import { useForm } from "react-hook-form";

import z from "zod";
import { FileInputButton, FormButton, MembersRegisterForm } from "../form";
import { Checkbox } from "@/components/ui/checkbox";
import { useRouter } from "next/navigation";

export default function MembersRegisterProfilePage() {
  const {
    state: { profile },
    setters: { setProfileData, setStep },
    register,
  } = useMembersRegisterContext();
  const [submitted, setSubmitted] = useState(false);
  const router = useRouter();

  const form = useForm<z.infer<typeof Profile>>({
    mode: "onBlur",
    resolver: zodResolver(Profile),
    defaultValues: profile,
  });

  useEffect(() => {
    if (submitted && profile) {
      (async () => {
        const res = await register();
        if (res.status === 201) {
          router.push("/home");
        }
      })();
    }
  }, [submitted, profile]);

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
              onClick={() => setStep(MembersRegisterStep.GUARDIANS)}
            >
              Voltar
            </FormButton>

            <FormButton type="submit">Salvar</FormButton>
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
                <FormControl>
                  <FileInputButton
                    id={field.name}
                    className="min-w-3xs"
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
                      disabled={field.value === "student"}
                      onCheckedChange={() => field.onChange("patient")}
                    />
                  </FormControl>
                  <FormLabel>Paciente</FormLabel>
                </FormItem>
                <FormItem className="flex flex-row items-center gap-2">
                  <FormControl>
                    <Checkbox
                      checked={field.value === "student"}
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
