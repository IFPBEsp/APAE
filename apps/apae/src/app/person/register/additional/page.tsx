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
import { EditAdditionals } from "@/schemas/edit-member-schemas"; 
import { zodResolver } from "@hookform/resolvers/zod";
import React, { useState, useEffect } from "react"; 
import { useForm } from "react-hook-form";
import { handleBackendValidationErrors } from "@/utils/form-errors";
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
import { useCreateServiceArea } from "@/hooks/service-area/use-create-service-area";
import { useFetchServiceAreas } from "@/hooks/service-area/use-fetch-service-areas";
import { CreateCare } from "@/schemas/care-schemas";

type DialogProps = Readonly<{
  open: boolean;
  onOpenChange: (value: boolean) => void;
  onSuccess?: (name: string) => void; 
}>;

function CreateVaccineDialog({ open, onOpenChange, onSuccess }: DialogProps) {
  const { createVaccine } = useVaccinesContext();
  const form = useForm<z.infer<typeof CreateVaccine>>({
    resolver: zodResolver(CreateVaccine),
    defaultValues: { name: "" },
  });

  const onSubmit = async (data: z.infer<typeof CreateVaccine>) => {
    await createVaccine(data);
    onOpenChange(false);
    onSuccess?.(data.name); 
    form.reset();
  };

  return (
    <Dialog open={open} onOpenChange={onOpenChange}>
      <DialogContent>
        <DialogHeader><DialogTitle>Nova Vacina</DialogTitle></DialogHeader>
        <Form {...form}>
          <form onSubmit={form.handleSubmit(onSubmit)} className="space-y-4">
            <FormField
              control={form.control}
              name="name"
              render={({ field }) => (
                <FormItem>
                  <FormLabel>Nome da Vacina</FormLabel>
                  <FormControl><Input placeholder="Ex: Hepatite B" {...field} /></FormControl>
                  <FormMessage />
                </FormItem>
              )}
            />
            <DialogFooter>
              <Button variant="outline" type="button" onClick={() => onOpenChange(false)}>Cancelar</Button>
              <Button type="submit" disabled={form.formState.isSubmitting}>Salvar</Button>
            </DialogFooter>
          </form>
        </Form>
      </DialogContent>
    </Dialog>
  );
}

function CreateCareDialog({ open, onOpenChange, onSuccess, onConfirm }: DialogProps & { onConfirm?: () => Promise<void> }) {
  const { create } = useCreateServiceArea();
  const form = useForm<z.infer<typeof CreateCare>>({
    resolver: zodResolver(CreateCare),
    defaultValues: { name: "" },
  });

  const onSubmit = async (data: z.infer<typeof CreateCare>) => {
    await create(data.name);
    onOpenChange(false);
    await onConfirm?.();
    onSuccess?.(data.name); 
    form.reset();
  };

  return (
    <Dialog open={open} onOpenChange={onOpenChange}>
      <DialogContent>
        <DialogHeader><DialogTitle>Novo Atendimento</DialogTitle></DialogHeader>
        <Form {...form}>
          <form onSubmit={form.handleSubmit(onSubmit)} className="space-y-4">
            <FormField
              control={form.control}
              name="name"
              render={({ field }) => (
                <FormItem>
                  <FormLabel>Nome do Atendimento</FormLabel>
                  <FormControl><Input placeholder="Ex: Oftamologista" {...field} /></FormControl>
                  <FormMessage />
                </FormItem>
              )}
            />
            <DialogFooter>
              <Button variant="outline" type="button" onClick={() => onOpenChange(false)}>Cancelar</Button>
              <Button type="submit" disabled={form.formState.isSubmitting}>Salvar</Button>
            </DialogFooter>
          </form>
        </Form>
      </DialogContent>
    </Dialog>
  );
}

function CreateDisorderDialog({ open, onOpenChange, onSuccess }: DialogProps) {
  const { createDisorder } = useDisordersContext();
  const form = useForm<z.infer<typeof CreateDisorder>>({
    resolver: zodResolver(CreateDisorder),
    defaultValues: { name: "" },
  });

  const onSubmit = async (data: z.infer<typeof CreateDisorder>) => {
    await createDisorder(data);
    onOpenChange(false);
    onSuccess?.(data.name); 
    form.reset();
  };

  return (
    <Dialog open={open} onOpenChange={onOpenChange}>
      <DialogContent>
        <DialogHeader><DialogTitle>Novo Transtorno</DialogTitle></DialogHeader>
        <Form {...form}>
          <form onSubmit={form.handleSubmit(onSubmit)} className="space-y-4">
            <FormField
              control={form.control}
              name="name"
              render={({ field }) => (
                <FormItem>
                  <FormLabel>Nome do Transtorno</FormLabel>
                  <FormControl><Input placeholder="Ex: TDAH" {...field} /></FormControl>
                  <FormMessage />
                </FormItem>
              )}
            />
            <DialogFooter>
              <Button variant="outline" type="button" onClick={() => onOpenChange(false)}>Cancelar</Button>
              <Button type="submit" disabled={form.formState.isSubmitting}>Salvar</Button>
            </DialogFooter>
          </form>
        </Form>
      </DialogContent>
    </Dialog>
  );
}

export default function MembersRegisterAdditionalsPage() {
  const [modal, setModal] = useState<"disorder" | "vaccine" | "care" | null>(null);
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

  type AdditionalsFormValues = z.infer<typeof Additionals> | z.infer<typeof EditAdditionals>;

  const form = useForm<AdditionalsFormValues>({
    mode: "onBlur",
    resolver: zodResolver(currentSchema),
    defaultValues: additionals,
  });

  const [isInitialized, setIsInitialized] = useState(false);

  useEffect(() => {
    if (isEditing && !isInitialized && (additionals.diseases || additionals.vaccines.length > 0)) {
      form.reset(additionals);
      setIsInitialized(true);
      setRefreshKey(prev => prev + 1);
    }
  }, [additionals, form, isEditing, isInitialized]);

  const onSubmit = async (values: AdditionalsFormValues) => {
    setIsLoading(true);
    try {
      setAdditionalsData(values);
      setStep(MembersRegisterStep.GUARDIAN);
    } catch (error) {
      if (error && typeof error === "object" && "response" in error) {
        const axiosError = error as { response?: { data?: unknown } };
        if (axiosError.response?.data) {
          handleBackendValidationErrors(axiosError.response.data, form.setError);
        }
      }
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
          const currentValues = form.getValues("care.types") ||[];
          if (!currentValues.includes(newName)) {
            form.setValue("care.types",[...currentValues, newName], { shouldDirty: true, shouldValidate: true });
            setRefreshKey(k => k + 1); 
          }
        }}
      />
      <CreateDisorderDialog
        open={modal === "disorder"}
        onOpenChange={(value: boolean) => setModal(value ? "disorder" : null)}
        onSuccess={(newName: string) => {
          const currentValues = form.getValues("disability.types") ||[];
          if (!currentValues.includes(newName)) {
            form.setValue("disability.types", [...currentValues, newName], { shouldDirty: true, shouldValidate: true });
            setRefreshKey(k => k + 1); 
          }
        }}
      />
      <CreateVaccineDialog
        open={modal === "vaccine"}
        onOpenChange={(value: boolean) => setModal(value ? "vaccine" : null)}
        onSuccess={(newName: string) => {
          const currentValues = form.getValues("vaccines") ||[];
          if (!currentValues.includes(newName)) {
             form.setValue("vaccines",[...currentValues, newName], { shouldDirty: true, shouldValidate: true });
             setRefreshKey(k => k + 1); 
          }
        }}
      />
      
      <Form {...form}>
        <MembersRegisterForm
          title={isEditing ? "Editar Informações Adicionais" : "Informações Adicionais"}
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
                    <Input placeholder="Catapora, Gripe H1N1, Pneumonia" {...field} />
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
                        ...vaccines.map((vac) => ({ label: vac.name, value: vac.name })),
                        ...(field.value || []).filter((val: string) => !vaccines.some((v) => v.name === val))
                          .map((val: string) => ({ label: val, value: val }))
                      ]}
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
                      key={`disorders-select-${refreshKey}`} 
                      defaultValue={field.value || []}
                      value={field.value || []}
                      options={[
                        ...disorders.map((dis) => ({ label: dis.name, value: dis.name })),
                        ...(field.value || []).filter((val: string) => !disorders.some((d) => d.name === val))
                          .map((val: string) => ({ label: val, value: val }))
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
                  <FormLabel>Laudo da Deficiência {isEditing ? "(Opcional na edição)" : "*"}</FormLabel>
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
                        <span className="truncate text-left" title={field.value.name}>
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
                        ...cares.map((care: any) => ({ label: care.area, value: care.area })),
                        ...(field.value || []).filter((val: string) => !cares.some((c: any) => c.area === val))
                          .map((val: string) => ({ label: val, value: val }))
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
                  <FormLabel>Encaminhamento {isEditing ? "(Opcional na edição)" : "*"}</FormLabel>
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
                        <span className="truncate text-left" title={field.value.name}>
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
                      onChange={(e) => field.onChange(formatCurrency(e.target.value))}
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