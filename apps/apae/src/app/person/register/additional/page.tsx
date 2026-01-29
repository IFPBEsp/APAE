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
import React, { useState } from "react";
import { useForm } from "react-hook-form";

import z from "zod";
import {
  DoubleCheckboxFormField,
  DoubleColumn,
  FileInputButton,
  FormButton,
  MembersRegisterForm,
} from "../form";
import { CreatableMultiSelect } from "@/components/creatable-multi-select";
import { useVaccinesContext } from "@/hooks/use-vaccines";
import {
  Dialog,
  DialogContent,
  DialogFooter,
  DialogHeader,
  DialogTitle,
} from "@/components/ui/dialog";
import { CreateVaccine } from "@/schemas/vaccine-schemas";
import { Button } from "@/components/ui/button";
import { useDisordersContext } from "@/hooks/use-disorders";
import { CreateDisorder } from "@/schemas/disorder-schemas";
import { formatCurrency } from "@/lib/formats";

type DialogProps = Readonly<{
  open: boolean;
  onOpenChange: (value: boolean) => void;
}>;

function CreateVaccineDialog({ open, onOpenChange }: DialogProps) {
  const { createVaccine } = useVaccinesContext();

  const form = useForm<z.infer<typeof CreateVaccine>>({
    resolver: zodResolver(CreateVaccine),
    defaultValues: { name: "" },
  });

  const onSubmit = async (data: z.infer<typeof CreateVaccine>) => {
    await createVaccine(data);
    onOpenChange(false);
    form.reset();
  };

  return (
    <Dialog open={open} onOpenChange={onOpenChange}>
      <DialogContent>
        <DialogHeader>
          <DialogTitle>Nova Vacina</DialogTitle>
        </DialogHeader>

        <Form {...form}>
          <form onSubmit={form.handleSubmit(onSubmit)} className="space-y-4">
            <FormField
              control={form.control}
              name="name"
              render={({ field }) => (
                <FormItem>
                  <FormLabel>Nome da Vacina</FormLabel>
                  <FormControl>
                    <Input placeholder="Ex: Hepatite B" {...field} />
                  </FormControl>
                  <FormMessage />
                </FormItem>
              )}
            />

            <DialogFooter>
              <Button
                variant="outline"
                type="button"
                onClick={() => onOpenChange(false)}
              >
                Cancelar
              </Button>
              <Button type="submit" disabled={form.formState.isSubmitting}>
                {form.formState.isSubmitting ? "Salvando..." : "Salvar"}
              </Button>
            </DialogFooter>
          </form>
        </Form>
      </DialogContent>
    </Dialog>
  );
}

function CreateDisorderDialog({ open, onOpenChange }: DialogProps) {
  const { createDisorder } = useDisordersContext();

  const form = useForm<z.infer<typeof CreateDisorder>>({
    resolver: zodResolver(CreateDisorder),
    defaultValues: { name: "" },
  });

  const onSubmit = async (data: z.infer<typeof CreateDisorder>) => {
    await createDisorder(data);
    onOpenChange(false);
    form.reset();
  };

  return (
    <Dialog open={open} onOpenChange={onOpenChange}>
      <DialogContent>
        <DialogHeader>
          <DialogTitle>Novo Transtorno</DialogTitle>
        </DialogHeader>

        <Form {...form}>
          <form onSubmit={form.handleSubmit(onSubmit)} className="space-y-4">
            <FormField
              control={form.control}
              name="name"
              render={({ field }) => (
                <FormItem>
                  <FormLabel>Nome do Transtorno</FormLabel>
                  <FormControl>
                    <Input placeholder="Ex: TDAH" {...field} />
                  </FormControl>
                  <FormMessage />
                </FormItem>
              )}
            />

            <DialogFooter>
              <Button
                variant="outline"
                type="button"
                onClick={() => onOpenChange(false)}
              >
                Cancelar
              </Button>
              <Button type="submit" disabled={form.formState.isSubmitting}>
                {form.formState.isSubmitting ? "Salvando..." : "Salvar"}
              </Button>
            </DialogFooter>
          </form>
        </Form>
      </DialogContent>
    </Dialog>
  );
}

export default function MembersRegisterAdditionalsPage() {
  const [modal, setModal] = useState<"disorder" | "vaccine" | null>(null);
  const [cares, setCares] = useState<string[]>(["Oftamologista"]);
  const { vaccines } = useVaccinesContext();
  const { disorders } = useDisordersContext();
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
    setStep(MembersRegisterStep.GUARDIAN);
  };

  return (
    <>
      <CreateDisorderDialog
        open={modal === "disorder"}
        onOpenChange={(value) => setModal(value ? "disorder" : null)}
      />
      <CreateVaccineDialog
        open={modal === "vaccine"}
        onOpenChange={(value) => setModal(value ? "vaccine" : null)}
      />
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
                    <CreatableMultiSelect
                      options={vaccines.map((vac) => ({
                        label: vac.name,
                        value: vac.name,
                      }))}
                      onValueChange={field.onChange}
                      placeholder="Selecione as vacinas"
                      hideSelectAll={true}
                      onCreate={() => setModal("vaccine")}
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
              name="disability.types"
              render={({ field }) => (
                <FormItem>
                  <FormLabel>Tipos de Deficiências *</FormLabel>
                  <FormControl>
                    <CreatableMultiSelect
                      options={disorders.map((dis) => ({
                        label: dis.name,
                        value: dis.name,
                      }))}
                      onValueChange={field.onChange}
                      onCreate={() => setModal("disorder")}
                      placeholder="Selecione as deficiências"
                      hideSelectAll={true}
                    />
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
              name="care.types"
              render={({ field }) => (
                <FormItem>
                  <FormLabel>Tipos de Atendimentos *</FormLabel>
                  <FormControl>
                    <CreatableMultiSelect
                      options={cares.map((care) => ({
                        label: care,
                        value: care,
                      }))}
                      onValueChange={field.onChange}
                      onCreate={async () => alert("TESTE")}
                      placeholder="Selecione os atendimentos necessários"
                      hideSelectAll={true}
                    />
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

            <DoubleCheckboxFormField
              control={form.control}
              name="bpc"
              labels={{
                main: "Possui BPC? *",
                true: "Sim",
                false: "Não",
              }}
            />
          </DoubleColumn>
        </MembersRegisterForm>
      </Form>
    </>
  );
}
