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
import { formatCEP, capitalizeFirst } from "@/lib/formats";
import { Address } from "@/domains/patients/schemas/member-schemas";
import { EditAddress } from "@/schemas/edit-member-schemas";
import { zodResolver } from "@hookform/resolvers/zod";
import { useForm } from "react-hook-form";
import { useState, useEffect } from "react";
import { usePathname } from "next/navigation";
import { handleBackendValidationErrors } from "@/lib/utils/form-errors";
import { Checkbox } from "@/components/ui/checkbox";

import { DoubleColumn, FormButton, MembersRegisterForm } from "../form";
import z from "zod";

export default function MembersRegisterAddressPage() {
  const {
    state: { address },
    setters: { setAddressData, setStep },
  } = useMembersRegisterContext();

  const [isLoading, setIsLoading] = useState(false);

  const pathname = usePathname();
  const isEditing = pathname.includes("/edit");
  const currentSchema = isEditing ? EditAddress : Address;

  const form = useForm<z.infer<typeof Address>>({
    mode: "onBlur",
    resolver: zodResolver(currentSchema),
    defaultValues: {
      ...address,
      noNumber: address.number === "SN" || false
    },
  });

  const isNoNumber = form.watch("noNumber");

  useEffect(() => {
    if (isNoNumber) {
      form.setValue("number", "SN");
      form.clearErrors("number");
    } else {
      if (form.getValues("number") === "SN") {
        form.setValue("number", "");
      }
    }
  }, [isNoNumber, form]);

  useEffect(() => {
    if (address && Object.keys(address).length > 0) {
      form.reset({
        ...address,
        noNumber: address.number === "SN" || false
      });
    }
  }, [address, form]);

  const onSubmit = async (values: z.infer<typeof Address>) => {
    setIsLoading(true);
    try {
      const dataToSave = {
        ...values,
        number: values.noNumber ? "SN" : values.number
      }
      setAddressData(dataToSave);

      if (isEditing) {
        setStep(MembersRegisterStep.PROFILE);
      } else {
        setStep(MembersRegisterStep.ADDITIONALS);
      }
    } catch (error: unknown) {
      const err = error as { response?: { data?: Record<string, string[]> } };
      if (err.response?.data) {
        handleBackendValidationErrors(err.response.data, form.setError);
      }
    } finally {
      setIsLoading(false);
    }
  };

  useEffect(() => {
    if (address.cep !== "") {
      form.reset(address);
    }
  }, [address, form]);

  return (
    <Form {...form}>
      <MembersRegisterForm
        title={isEditing ? "Editar Endereço" : "Endereço"}
        onSubmit={form.handleSubmit(onSubmit)}
        buttons={
          <>
            <FormButton
              type="button"
              onClick={() => {
                const currentValues = form.getValues();
                setAddressData(currentValues);
                setStep(MembersRegisterStep.GUARDIAN);
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
            name="street"
            render={({ field }) => (
              <FormItem className="md:col-span-2">
                <FormLabel>Rua *</FormLabel>
                <FormControl>
                  <Input
                    placeholder="Adielson Assis Alves"
                    {...field}
                    onChange={(e) => field.onChange(capitalizeFirst(e.target.value))}
                  />
                </FormControl>
                <FormMessage />
              </FormItem>
            )}
          />
          <div className="space-y-2">
            <div className="flex justify-between items-center">
              <FormLabel>Número *</FormLabel>
              <FormField
                control={form.control}
                name="noNumber"
                render={({ field }) => (
                  <div className="flex items-center space-x-2">
                    <Checkbox
                      id="noNumber"
                      checked={field.value}
                      onCheckedChange={(checked) => {
                        field.onChange(checked);
                        if (checked) {
                          form.setValue("number", "SN");
                          form.clearErrors("number");
                        } else {
                          form.setValue("number", "");
                        }
                      }}
                    />
                    <label htmlFor="noNumber" className="text-sm text-gray-600">Sem número? </label>
                  </div>
                )}
              />
            </div>
            <FormField
              control={form.control}
              name="number"
              render={({ field }) => (
                <FormItem>
                  <FormControl>
                    <Input
                      placeholder="Número"
                      {...field}
                      disabled={isNoNumber}
                      aria-placeholder={isNoNumber ? "SN" : "Número"}
                      className={isNoNumber ? "bg-gray-100 cursor-not-allowed" : ""}
                    />
                  </FormControl>
                  <FormMessage />
                </FormItem>
              )}
            />
          </div>
          <FormField
            control={form.control}
            name="complement"
            render={({ field }) => (
              <FormItem>
                <FormLabel>Complemento</FormLabel>
                <FormControl>
                  <Input
                    placeholder="Apartamento 101"
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
            name="cep"
            render={({ field }) => (
              <FormItem>
                <FormLabel>CEP *</FormLabel>
                <FormControl>
                  <Input
                    placeholder="58135-000"
                    maxLength={9}
                    value={field.value}
                    onChange={(e) => {
                      const formated = formatCEP(e.target.value);
                      field.onChange(formated);
                    }}
                  />
                </FormControl>
                <FormMessage />
              </FormItem>
            )}
          />

          <FormField
            control={form.control}
            name="state"
            render={({ field }) => (
              <FormItem>
                <FormLabel>Estado *</FormLabel>
                <FormControl>
                  <Input
                    placeholder="Paraiba"
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
            name="city"
            render={({ field }) => (
              <FormItem>
                <FormLabel>Cidade *</FormLabel>
                <FormControl>
                  <Input
                    placeholder="Esperança"
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
            name="neighborhood"
            render={({ field }) => (
              <FormItem>
                <FormLabel>Bairro *</FormLabel>
                <FormControl>
                  <Input
                    placeholder="Centro"
                    {...field}
                    onChange={(e) => field.onChange(capitalizeFirst(e.target.value))}
                  />
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
