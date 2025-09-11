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
import { Additionals } from "@/schemas/member-schemas";
import { zodResolver } from "@hookform/resolvers/zod";
import React from "react";
import { useForm } from "react-hook-form";

import z from "zod";
import {
  DoubleColumn,
  FileInputButton,
  FormButton,
  MembersRegisterForm,
} from "../form";

export default function MembersRegisterAdditionalsPage() {
  const {
    state: { additionals },
    setters: { setAdditionalsData, setStep },
  } = useMembersRegisterContext();

  const form = useForm<z.infer<typeof Additionals>>({
    mode: "onBlur",
    resolver: zodResolver(Additionals),
    defaultValues: additionals,
  });

  const onSubmit = (values: z.infer<typeof Additionals>) => {
    setAdditionalsData(values);
    setStep(MembersRegisterStep.GUARDIANS);
  };

  return (
    <Form {...form}>
      <MembersRegisterForm
        title="Informações Adicionais"
        onSubmit={form.handleSubmit(onSubmit)}
        buttons={
          <>
            <FormButton
              type="button"
              onClick={() => setStep(MembersRegisterStep.ADDRESS)}
            >
              Voltar
            </FormButton>

            <FormButton type="submit">Próximo</FormButton>
          </>
        }
      >
        <DoubleColumn>
          <FormField
            control={form.control}
            name="diseases"
            render={({ field }) => (
              <FormItem>
                <FormLabel>Doenças que já teve *</FormLabel>
                <FormControl>
                  <Input
                    placeholder="Catapora, Gripe H1N1, Pneumonia"
                    {...field}
                  />
                </FormControl>
                <FormMessage />
              </FormItem>
            )}
          />

          <FormField
            control={form.control}
            name="vaccines"
            render={({ field }) => (
              <FormItem>
                <FormLabel>Vacinas Tomadas *</FormLabel>
                <FormControl>
                  <Input
                    placeholder="DTP, Antipólio, BCG, Antitetânica, COVID"
                    {...field}
                  />
                </FormControl>
                <FormMessage />
              </FormItem>
            )}
          />

          <FormField
            control={form.control}
            name="medications"
            render={({ field }) => (
              <FormItem>
                <FormLabel>Tipo de medicação que toma *</FormLabel>
                <FormControl>
                  <Input placeholder="Losartana, paracetamol" {...field} />
                </FormControl>
                <FormMessage />
              </FormItem>
            )}
          />

          <FormField
            control={form.control}
            name="allergies"
            render={({ field }) => (
              <FormItem>
                <FormLabel>Tem alergias? Quais? *</FormLabel>
                <FormControl>
                  <Input placeholder="Alergia a abacaxi" {...field} />
                </FormControl>
                <FormMessage />
              </FormItem>
            )}
          />

          <FormField
            control={form.control}
            name="disability.type"
            render={({ field }) => (
              <FormItem>
                <FormLabel>Tipo de Deficiência *</FormLabel>
                <FormControl>
                  <Input placeholder="Deficiência auditiva severa" {...field} />
                </FormControl>
                <FormMessage />
              </FormItem>
            )}
          />

          <FormField
            control={form.control}
            name="disability.report"
            render={({ field }) => (
              <FormItem>
                <FormLabel>Laudo da Deficiência *</FormLabel>
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
                      "Selecionar Laudo"
                    )}
                  </FileInputButton>
                </FormControl>
                <FormMessage />
              </FormItem>
            )}
          />

          <FormField
            control={form.control}
            name="care.type"
            render={({ field }) => (
              <FormItem>
                <FormLabel>Tipo de Atendimento *</FormLabel>
                <FormControl>
                  <Input placeholder="Otorrinolaringologia" {...field} />
                </FormControl>
                <FormMessage />
              </FormItem>
            )}
          />

          <FormField
            control={form.control}
            name="care.referral"
            render={({ field }) => (
              <FormItem>
                <FormLabel>Encaminhamento *</FormLabel>
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
                      "Selecionar Encaminhamento"
                    )}
                  </FileInputButton>
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
