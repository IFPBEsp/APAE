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
import { Guardian } from "@/schemas/member-schemas";
import { EditGuardian } from "@/schemas/edit-member-schemas";
import { zodResolver } from "@hookform/resolvers/zod";
import React, { useState, useEffect } from "react"; 
import { useForm } from "react-hook-form";
import { handleBackendValidationErrors } from "@/utils/form-errors";
import { usePathname } from "next/navigation"; 
import { formatPhone } from "@/lib/formats";
import { Checkbox } from "@/components/ui/checkbox";

import z from "zod";
import { DoubleColumn, FormButton, MembersRegisterForm } from "../form";

export default function MembersRegisterGuardianPage() {
  const {
    state: { guardian },
    setters: { setGuardianData, setStep },
  } = useMembersRegisterContext();

  const [isLoading, setIsLoading] = useState(false);

  const pathname = usePathname();
  const isEditing = pathname.includes("/edit");
  const currentSchema = isEditing ? EditGuardian : Guardian; 

  const form = useForm<any>({ 
    mode: "onBlur",
    resolver: zodResolver(currentSchema), 
    defaultValues: {
      ...guardian,
      address: {
        ...guardian.address,
        noNumber: guardian.address?.number === "SN"
      }
    },
  });

  const [isInitialized, setIsInitialized] = useState(false);

  const isNoNumber = form.watch("address.noNumber");

  useEffect(() => {
    if (isNoNumber){
      form.setValue("address.number", "SN");
      form.clearErrors("address.number");
    } else {
      if (form.getValues("address.number") === "SN") {
        form.setValue("address.number", "");
      }
    }
  }, [isNoNumber, form]);

  useEffect(() => {
    if (isEditing && guardian.name && !isInitialized) {
      form.reset(guardian);
      setIsInitialized(true);
    }
  }, [isEditing, guardian, form, isInitialized]);

  const onSubmit = async (values: any) => {
    setIsLoading(true);
    try {
      const { address: { noNumber, ...addressData }, ...rest } = values;
      const dataToSave = { ...rest, address: addressData };
      setGuardianData(dataToSave);
      setStep(MembersRegisterStep.PROFILE);
    } catch (error: any) {
      if (error.response?.data) {
        handleBackendValidationErrors(error.response.data, form.setError);
      }
    } finally {
      setIsLoading(false);
    }
  };

  return (
    <Form {...form}>
      <MembersRegisterForm
        title={isEditing ? "Editar Dados do Responsável" : "Dados do Responsável"}
        onSubmit={form.handleSubmit(onSubmit)}


       buttons={
          <div className="flex justify-end gap-3 w-full">
            {/* Botão Voltar*/}
            <FormButton
              type="button"
              onClick={() => {
                const destino = isEditing 
                  ? MembersRegisterStep.ADDRESS 
                  : MembersRegisterStep.ADDITIONALS;
                setStep(destino);
              }}
              disabled={isLoading}
 
              className="bg-[#FCD511] text-[#0D4F97] hover:bg-[#0D4F97] hover:text-white font-baloo font-medium h-10 px-8 border-none shadow-sm transition-colors">              Voltar
            </FormButton>

            {/* Botão Próximo */}
              <FormButton 
                type="submit" 
                disabled={isLoading}
                className="bg-[#FCD511] text-[#0D4F97] hover:bg-[#0D4F97] hover:text-white font-baloo font-medium h-10 px-8 border-none shadow-sm transition-colors">              {isLoading ? "Validando..." : "Próximo"}
            </FormButton>
          </div>
        }    
      >
        <DoubleColumn>
          <FormField
            control={form.control}
            name="name"
            render={({ field }) => (
              <FormItem>
                <FormLabel>Nome Completo *</FormLabel>
                <FormControl>
                  <Input
                    placeholder="Digite o nome completo do responsável"
                    {...field}
                  />
                </FormControl>
                <FormMessage />
              </FormItem>
            )}
          />

          <FormField
            control={form.control}
            name="contact"
            render={({ field }) => (
              <FormItem>
                <FormLabel>Contato de Emergência *</FormLabel>
                <FormControl>
                  <Input
                    placeholder="(00) 00000-0000"{...field}maxLength={15} onChange={(e) => {
                      const formatted = formatPhone(e.target.value); 
                      field.onChange(formatted);
                  }}
                  />
                </FormControl>
                <FormMessage />
              </FormItem>
            )}
          />

          <FormField
            control={form.control}
            name="address.street"
            render={({ field }) => (
              <FormItem>
                <FormLabel>Rua *</FormLabel>
                <FormControl>
                  <Input placeholder="Rua exemplo" {...field} />
                </FormControl>
                <FormMessage />
              </FormItem>
            )}
          />

          <FormField
            control={form.control}
            name="address.number"
            render={({ field }) => (
              <FormItem>
                <div className="flex justify-between items-center mb-1">
                  <FormLabel>Número *</FormLabel>
                  <FormField
                    control={form.control}
                    name="address.noNumber"
                    render={({ field: checkField }) => (
                      <div className="flex items-center space-x-2">
                        <FormControl>
                          <Checkbox
                            id="noNumber"
                            checked={checkField.value}
                            onCheckedChange={checkField.onChange}
                            disabled={isLoading}
                          />
                        </FormControl>
                        <label 
                          htmlFor="noNumber" 
                          className="text-[10px] font-bold uppercase text-slate-500 cursor-pointer select-none">
                            Sem número?
                        </label>
                      </div>
                    )}
                  />
                </div>
                <FormControl>
                  <Input
                    placeholder={isNoNumber ? "Sem número" : "49"} {...field}
                    disabled={isNoNumber}
                    className={isNoNumber ? "bg-slate-50 italic text-slate-400" : ""}
                  />
                </FormControl>   
                <FormMessage /> 
              </FormItem>
            )}
          />

          <FormField
            control={form.control}
            name="address.complement"
            render={({ field }) => (
              <FormItem>
                <FormLabel>Complemento</FormLabel>
                <FormControl>
                  <Input placeholder="Apartamento 101" {...field} />
                </FormControl>
                <FormMessage />
              </FormItem>
            )}
          />

          <FormField
            control={form.control}
            name="address.cep"
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
            name="address.state"
            render={({ field }) => (
              <FormItem>
                <FormLabel>Estado *</FormLabel>
                <FormControl>
                  <Input placeholder="Paraíba" {...field} />
                </FormControl>
                <FormMessage />
              </FormItem>
            )}
          />

          <FormField
            control={form.control}
            name="address.city"
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
            name="address.district"
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

          <FormField
            control={form.control}
            name="kinship"
            render={({ field }) => (
              <FormItem>
                <FormLabel>Parentesco *</FormLabel>
                <FormControl>
                  <Input placeholder="Irmã" {...field} />
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