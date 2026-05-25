"use client";

import {
  Form,
  FormControl,
  FormDescription,
  FormField,
  FormItem,
  FormLabel,
  FormMessage,
} from "@/components/ui/form";
import { Input } from "@/components/ui/input";
import { Checkbox } from "@/components/ui/checkbox";
import {
  MembersRegisterStep,
  useMembersRegisterContext,
} from "@/hooks/use-members-register-context";
import { formatCPF, formatRG } from "@/lib/formats";
import { Kinships } from "@/schemas/member-schemas";
import { zodResolver } from "@hookform/resolvers/zod";
import { useState, useEffect } from "react";
import { useFieldArray, useForm } from "react-hook-form";
import { handleBackendValidationErrors } from "@/lib/utils/form-errors";

import z from "zod";
import {
  DoubleCheckboxFormField,
  DoubleColumn,
  FormButton,
  MembersRegisterForm,
} from "../form";
import { Button } from "@/components/ui/button";
import { usePathname } from "next/navigation";
import { UseFormReturn } from "react-hook-form";

interface LegalGuardianCheckboxProps {
  form: UseFormReturn<z.infer<typeof Kinships>>;
  index: number;
  setGuardianData: (data: {
    name: string;
    kinship: string;
    contact: string;
    address: {
      cep: string;
      state: string;
      city: string;
      neighborhood: string;
      district: string;
      street: string;
      number: string;
      complement: string;
      noNumber: boolean;
    };
  }) => void;
}

function LegalGuardianCheckbox({
  form,
  index,
  setGuardianData,
}: LegalGuardianCheckboxProps) {
  const isAlive = form.watch(`kinships.${index}.alive`);
  const isLegalGuardian = form.watch(`kinships.${index}.isLegalGuardian`);

  const fieldOnChange = (value: boolean) => {
    form.setValue(`kinships.${index}.isLegalGuardian`, value);
  };

  useEffect(() => {
    if (!isAlive && isLegalGuardian) {
      fieldOnChange(false);
      setGuardianData({
        name: "",
        kinship: "",
        contact: "",
        address: {
          cep: "",
          state: "",
          city: "",
          neighborhood: "",
          district: "",
          street: "",
          number: "",
          complement: "",
          noNumber: false,
        },
      });
    }
  }, [isAlive, isLegalGuardian, setGuardianData]);

  return (
    <FormItem
      className={`flex flex-row items-center space-x-3 space-y-0 rounded-md border p-4 shadow-sm ${
        !isAlive ? "border-gray-200/40 opacity-50" : "border-gray-300/60"
      }`}
    >
      <FormControl>
        <Checkbox
          className="border-zinc-300"
          checked={isLegalGuardian}
          disabled={!isAlive}
          onCheckedChange={(checked) => {
            if (checked) {
              const currentKinships = form.getValues("kinships");
              currentKinships.forEach((_, i) => {
                if (i !== index) {
                  form.setValue(`kinships.${i}.isLegalGuardian`, false);
                }
              });
              fieldOnChange(checked as boolean);
            } else {
              fieldOnChange(checked);
              setGuardianData({
                name: "",
                kinship: "",
                contact: "",
                address: {
                  cep: "",
                  state: "",
                  city: "",
                  neighborhood: "",
                  district: "",
                  street: "",
                  number: "",
                  complement: "",
                  noNumber: false,
                },
              });
            }
          }}
        />
      </FormControl>
      <div className="space-y-1 leading-none">
        <FormLabel className={!isAlive ? "text-gray-400" : ""}>
          Este parente é o Responsável Legal do paciente?
        </FormLabel>
        <FormDescription>
          {!isAlive
            ? "Não é possível definir um parente falecido como responsável legal."
            : "Apenas uma pessoa pode ser marcada como o contato principal e responsável legal."}
        </FormDescription>
      </div>
    </FormItem>
  );
}

export default function MembersRegisterKinshipsPage() {
  const {
    state: { kinships },
    setters: { setKinshipsData, setStep, setGuardianData },
  } = useMembersRegisterContext();

  const [isLoading, setIsLoading] = useState(false);

  const pathname = usePathname();
  const isEditing = pathname.includes("/edit");

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

  useEffect(() => {
    if (!isEditing) return;

    const subscription = form.watch((value) => {
      const kinships = value.kinships || [];
      const legalGuardianKinship = kinships.find(
        (k) => k?.isLegalGuardian,
      );

      if (legalGuardianKinship) {
        setGuardianData({
          name: legalGuardianKinship.name || "",
          kinship: legalGuardianKinship.type || "",
        });
      }
    });

    return () => subscription.unsubscribe();
  }, [form, isEditing, setGuardianData]);

  const onSubmit = async (values: z.infer<typeof Kinships>) => {
    setIsLoading(true);
    try {
      setKinshipsData(values.kinships);

      const legalGuardianKinship = values.kinships.find(
        (k) => k.isLegalGuardian,
      );

      if (legalGuardianKinship) {
        if (isEditing) {
          setGuardianData({
            name: legalGuardianKinship.name,
            kinship: legalGuardianKinship.type,
          });
        } else {
          setGuardianData({
            name: legalGuardianKinship.name,
            kinship: legalGuardianKinship.type,
            contact: "",
            address: {
              cep: "",
              state: "",
              city: "",
              neighborhood: "",
              noNumber: false,
              number: "",
              district: "",
              street: "",
              complement: "",
            },
          });
        }
      }

      setStep(MembersRegisterStep.GUARDIAN);
    } catch (error: unknown) {
      const err = error as { response?: { data?: Record<string, string[]> } };
      if (err.response?.data) {
        handleBackendValidationErrors(err.response.data, form.setError);
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
          <DoubleColumn key={item.id} className="relative pb-6 border-b mb-6">
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

            {/* CHECKBOX DO RESPONSÁVEL LEGAL (ÚNICO) - Não exibe na edição */}
            {!isEditing && (
              <div className="md:col-span-2 mt-1">
                <LegalGuardianCheckbox
                  form={form}
                  index={index}
                  setGuardianData={setGuardianData}
                />
              </div>
            )}
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
              isLegalGuardian: false,
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
