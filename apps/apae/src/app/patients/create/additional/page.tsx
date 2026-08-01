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
} from "@/domains/patients/hooks/use-members-register-context";
import { Additionals, AdditionalsData } from "@/domains/patients/schemas/member-schemas";
import { EditAdditionals } from "@/schemas/edit-member-schemas";
import { zodResolver } from "@hookform/resolvers/zod";
import React, { useState, useEffect } from "react";
import { useForm } from "react-hook-form";
import { handleBackendValidationErrors } from "@/lib/utils/form-errors";
import { usePathname } from "next/navigation";

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
import { useDisordersContext } from "@/hooks/use-disorders";
import { formatCurrency, capitalizeFirst } from "@/lib/formats";
import { useFetchServiceAreas } from "@/hooks/service-area/use-fetch-service-areas";

import { CreateCareDialog } from "@/domains/patients/components/dialogs/CreateCareDialog";
import { CreateDisorderDialog } from "@/domains/patients/components/dialogs/CreateDisorderDialog";

export default function MembersRegisterAdditionalsPage() {
  const [modal, setModal] = useState<"disorder" | "care" | null>(
    null,
  );
  const [refreshKey, setRefreshKey] = useState(0);

  const { areas: cares, fetchCares } = useFetchServiceAreas();
  const { vaccines } = useVaccinesContext();
  const { disorders } = useDisordersContext();

  const {
    state: { additionals },
    setters: { setAdditionalsData, setStep },
  } = useMembersRegisterContext();

  const [isLoading, setIsLoading] = useState(false);

  const pathname = usePathname();
  const isEditing = pathname.includes("/edit");
  const currentSchema = isEditing ? EditAdditionals : Additionals;

  const form = useForm<z.infer<typeof Additionals>>({
    mode: "onBlur",
    resolver: zodResolver(currentSchema as typeof Additionals),
    defaultValues: additionals as AdditionalsData,
  });

  const [isInitialized, setIsInitialized] = useState(false);

  useEffect(() => {
    if (
      isEditing &&
      !isInitialized &&
      (additionals.diseases || additionals.vaccines.length > 0)
    ) {
      form.reset(additionals as AdditionalsData);
      setIsInitialized(true);
      setRefreshKey(prev => prev + 1);
    }
  }, [additionals, form, isEditing, isInitialized]);

  useEffect(() => {
    const hasData =
      additionals.diseases !== "" ||
      additionals.vaccines.length > 0 ||
      additionals.disability.report instanceof File ||
      additionals.care.referral instanceof File;

    if (hasData) {
      form.reset(additionals as AdditionalsData);
    }
  }, [additionals, form]);

  const onSubmit = async (values: z.infer<typeof Additionals>) => {
    setIsLoading(true);
    try {
      setAdditionalsData(values);
      setStep(MembersRegisterStep.PROFILE);
    } catch (error: unknown) {
      const err = error as { response?: { data?: Record<string, string[]> } };
      if (err.response?.data) {
        handleBackendValidationErrors(err.response.data, form.setError);
      };
    } finally {
      setIsLoading(false);
    }
  };

  return (
    <>
      <CreateCareDialog
        open={modal === "care"}
        onConfirm={fetchCares}
        onOpenChange={(value: boolean) => setModal(value ? "care" : null)}
        onSuccess={(newName: string) => {
          const currentValues = form.getValues("care.types") || [];
          if (!currentValues.includes(newName)) {
            form.setValue("care.types", [...currentValues, newName], {
              shouldDirty: true,
              shouldValidate: true,
            });
            setRefreshKey((k) => k + 1);
          }
        }}
      />
      <CreateDisorderDialog
        open={modal === "disorder"}
        onOpenChange={(value: boolean) => setModal(value ? "disorder" : null)}
        onSuccess={(newName: string) => {
          const currentValues = form.getValues("disability.types") || [];
          if (!currentValues.includes(newName)) {
            form.setValue("disability.types", [...currentValues, newName], {
              shouldDirty: true,
              shouldValidate: true,
            });
            setRefreshKey((k) => k + 1);
          }
        }}
      />
      <Form {...form}>
        <MembersRegisterForm
          title={
            isEditing
              ? "Editar Informações Adicionais"
              : "Informações Adicionais"
          }
          onSubmit={form.handleSubmit(onSubmit)}
          buttons={
            <>
              <FormButton
                type="button"
                onClick={() => setStep(MembersRegisterStep.ADDRESS)}
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
              name="diseases"
              render={({ field }) => (
                <FormItem>
                  <FormLabel>Doenças que já teve *</FormLabel>
                  <FormControl>
                    <Input
                      placeholder="Catapora, Gripe H1N1, Pneumonia"
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
              name="vaccines"
              render={({ field }) => (
                <FormItem>
                  <FormLabel>Vacinas Tomadas *</FormLabel>
                  <FormControl>
                    <CreatableMultiSelect
                      key={`vaccines-select-${refreshKey}`}
                      defaultValue={field.value || []}
                      value={field.value || []}
                      options={[
                        ...vaccines.map((vac) => ({
                          label: vac.name,
                          value: vac.name,
                        })),
                        ...(field.value || [])
                          .filter(
                            (val: string) =>
                              !vaccines.some((v) => v.name === val),
                          )
                          .map((val: string) => ({ label: val, value: val })),
                      ]}
                      onValueChange={field.onChange}
                      placeholder="Selecione as vacinas"
                      hideSelectAll={true}
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
                    <Input
                      placeholder="Losartana, paracetamol"
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
              name="allergies"
              render={({ field }) => (
                <FormItem>
                  <FormLabel>Tem alergias? Quais? *</FormLabel>
                  <FormControl>
                    <Input
                      placeholder="Alergia a abacaxi"
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
              name="disability.types"
              render={({ field }) => (
                <FormItem>
                  <FormLabel>Tipos de Deficiências *</FormLabel>
                  <FormControl>
                    <CreatableMultiSelect
                      key={`disorders-select-${refreshKey}`}
                      defaultValue={field.value || []}
                      value={field.value || []}
                      options={[
                        ...disorders.map((dis) => ({
                          label: dis.name,
                          value: dis.name,
                        })),
                        ...(field.value || [])
                          .filter(
                            (val: string) =>
                              !disorders.some((d) => d.name === val),
                          )
                          .map((val: string) => ({ label: val, value: val })),
                      ]}
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
                  <FormLabel>
                    Laudo da Deficiência{" "}
                    {isEditing ? "(Opcional na edição)" : "*"}
                  </FormLabel>
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
                      {field.value instanceof File ? (
                        <span
                          className="truncate text-left"
                          title={field.value.name}
                        >
                          Arquivo: {field.value.name}
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
                      key={`cares-select-${refreshKey}`}
                      defaultValue={field.value || []}
                      value={field.value || []}
                      options={[
                        ...cares.map((care: { area: string }) => ({
                          label: care.area,
                          value: care.area,
                        })),
                        ...(field.value || [])
                          .filter(
                            (val: string) =>
                              !cares.some(
                                (c: { area: string }) => c.area === val,
                              ),
                          )
                          .map((val: string) => ({ label: val, value: val })),
                      ]}
                      onValueChange={field.onChange}
                      placeholder="Selecione os atendimentos necessários"
                      hideSelectAll={true}
                      onCreate={() => setModal("care")}
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
                  <FormLabel>
                    Encaminhamento {isEditing ? "(Opcional na edição)" : "*"}
                  </FormLabel>
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
                      {field.value instanceof File ? (
                        <span
                          className="truncate text-left"
                          title={field.value.name}
                        >
                          Arquivo: {field.value.name}
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
