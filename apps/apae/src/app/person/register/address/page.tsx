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
import { formatCEP } from "@/lib/formats";
import { Address } from "@/schemas/member-schemas";
import { EditAddress } from "@/schemas/edit-member-schemas";  
import { zodResolver } from "@hookform/resolvers/zod";
import { useForm } from "react-hook-form";
import { useState, useEffect } from "react";
import { usePathname } from "next/navigation"; 
import { handleBackendValidationErrors } from "@/utils/form-errors";
import z from "zod";

import { DoubleColumn, FormButton, MembersRegisterForm } from "../form";

export default function MembersRegisterAddressPage() {
  const {
    state: { address },
    setters: { setAddressData, setStep },
  } = useMembersRegisterContext();

  const [isLoading, setIsLoading] = useState(false);


  const pathname = usePathname();
  const isEditing = pathname.includes("/edit");
  const currentSchema = isEditing ? EditAddress : Address;

  type AddressFormValues = z.infer<typeof Address> | z.infer<typeof EditAddress>;

  const form = useForm<AddressFormValues>({
    mode: "onBlur",
    resolver: zodResolver(currentSchema),
    defaultValues: address,
  });

  
  const [isInitialized, setIsInitialized] = useState(false);

  useEffect(() => {
    if (isEditing && address.cep && !isInitialized) {
      form.reset(address);
      setIsInitialized(true);
    }
  }, [address, form, isEditing, isInitialized]);

  const onSubmit = async (values: AddressFormValues) => {
    setIsLoading(true);
    try {
      setAddressData(values);
        if (isEditing) {
        setStep(MembersRegisterStep.GUARDIAN); 
      } else {
        setStep(MembersRegisterStep.ADDITIONALS);
      }
    } catch (error: unknown) {
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
    <Form {...form}>
      <MembersRegisterForm
        title={isEditing ? "Editar Endereço" : "Endereço"}
        onSubmit={form.handleSubmit(onSubmit)}
        buttons={
          <>
            <FormButton
              type="button"
              onClick={() => setStep(MembersRegisterStep.KINSHIPS)}
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
                  <Input placeholder="Adielson Assis Alves, 49" {...field} />
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
                  <Input placeholder="Paraiba" {...field} />
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
                  <Input placeholder="Esperança" {...field} />
                </FormControl>
                <FormMessage />
              </FormItem>
            )}
          />

          <FormField
            control={form.control}
            name="district"
            render={({ field }) => (
              <FormItem>
                <FormLabel>Bairro *</FormLabel>
                <FormControl>
                  <Input placeholder="Centro" {...field} />
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