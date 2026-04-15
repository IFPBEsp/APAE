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
import { Checkbox } from "@/components/ui/checkbox";

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

  const form = useForm<any>({
    mode: "onBlur",
    resolver: zodResolver(currentSchema),
    defaultValues: {
      ...address,
      noNumber: address.number === "SN" || false 
    },
  });

  const isNoNumber = form.watch("noNumber");

  useEffect(() => {
    if(isNoNumber) {
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

  const onSubmit = async (values: any) => {
    setIsLoading(true);
    try {
      const {noNumber, ...dataToSave} = values;
      if(noNumber) dataToSave.number = "SN";
      setAddressData(dataToSave);
      
        if (isEditing) {
        setStep(MembersRegisterStep.GUARDIAN); 
      } else {
        setStep(MembersRegisterStep.ADDITIONALS);
      }
    } catch (error: any) {
      if (error.response?.data) {
        handleBackendValidationErrors(error.response.data, form.setError);
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
                  <Input placeholder="Adielson Assis Alves" {...field} />
                </FormControl>
                <FormMessage />
              </FormItem>
            )}
          />
          <FormField
            control={form.control}
            name="number"
            render={({ field }) => (
              <FormItem>
                <FormLabel>Número *</FormLabel>

                <FormField 
                  control={form.control}
                  name="noNumber"
                  render={({ field: checkField }) => (
                    <div className="flex items-center space-x-2">
                      <FormControl>
                        <Checkbox
                          id="noNumber"
                          checked={checkField.value}
                          onCheckedChange={checkField.onChange}
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
            name="complement"
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
            name="neighborhood"
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