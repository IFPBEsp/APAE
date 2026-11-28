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
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from "@/components/ui/select";
import { STATES, normalizeStateToUF } from "@/lib/states";
import {
  MembersRegisterStep,
  useMembersRegisterContext,
} from "@/domains/patients/hooks/use-members-register-context";
import { formatCEP, capitalizeFirst } from "@/lib/formats";
import { Address } from "@/domains/patients/schemas/member-schemas";
import { EditAddress } from "@/domains/patients/schemas/edit-member-schemas";
import { zodResolver } from "@hookform/resolvers/zod";
import { useForm } from "react-hook-form";
import { useState, useEffect, useRef } from "react";
import { usePathname } from "next/navigation";
import { handleBackendValidationErrors } from "@/lib/utils/form-errors";
import { Checkbox } from "@/components/ui/checkbox";

import { DoubleColumn, FileInputButton, FormButton, MembersRegisterForm } from "@/domains/patients/components/form";
import z from "zod";

export default function MembersRegisterAddressPage() {
  const {
    state: { address, guardian },
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
      state: normalizeStateToUF(address.state) as z.infer<typeof Address>["state"],
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
        state: normalizeStateToUF(address.state) as z.infer<typeof Address>["state"],
        noNumber: address.number === "SN" || false
      });
    }
  }, [address, form]);

  // Copies the guardian's address into the patient's address once per page visit
  // when "reside junto com o Paciente" is checked. Runs regardless of the patient's
  // address being empty or not (a previous visit / "Voltar" may have stored partial
  // values, which used to block the copy). The proofOfAddress file is never touched.
  const hasCopiedGuardianAddress = useRef(false);

  useEffect(() => {
    if (hasCopiedGuardianAddress.current) return;
    if (!guardian.residesWithPatient) return;

    const guardianAddr = guardian.address;
    const hasGuardianAddress = Boolean(
      guardianAddr?.street || guardianAddr?.cep || guardianAddr?.city
    );
    if (!hasGuardianAddress) return;

    hasCopiedGuardianAddress.current = true;

    const copiedAddress = {
      cep: guardianAddr.cep ?? "",
      state: normalizeStateToUF(guardianAddr.state) as z.infer<typeof Address>["state"],
      city: guardianAddr.city ?? "",
      neighborhood: guardianAddr.neighborhood ?? "",
      street: guardianAddr.street ?? "",
      number: guardianAddr.number ?? "",
      complement: guardianAddr.complement ?? "",
      district: guardianAddr.district ?? "",
      noNumber: guardianAddr.number === "SN",
    };

    // Keep the proof file already attached (in the form or in the context)
    const currentProof =
      form.getValues("proofOfAddress") ?? address.proofOfAddress;

    form.reset({ ...copiedAddress, proofOfAddress: currentProof });
    // Partial update: the reducer merges, so proofOfAddress in the context is preserved
    setAddressData({ ...copiedAddress, proofOfAddress: currentProof });
  }, [address.proofOfAddress, guardian.residesWithPatient, guardian.address, form, setAddressData]);

  const onSubmit = async (values: z.infer<typeof Address>) => {
    setIsLoading(true);
    try {
      const dataToSave = {
        ...values,
        number: values.noNumber ? "SN" : values.number
      }
      setAddressData(dataToSave);

      setStep(MembersRegisterStep.ADDITIONALS);
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
      form.reset({
        ...address,
        state: normalizeStateToUF(address.state) as z.infer<typeof Address>["state"],
      });
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
            render={({ field, fieldState }) => (
              <FormItem>
                <FormLabel>Estado *</FormLabel>
                <FormControl>
                  <Select onValueChange={field.onChange} value={field.value}>
                    <SelectTrigger
                      className={`w-full ${fieldState.invalid ? "border-red-500" : "border-gray-300"}`}
                    >
                      <SelectValue placeholder="Selecione um estado" />
                    </SelectTrigger>
                    <SelectContent>
                      {STATES.map((s) => (
                        <SelectItem key={s} value={s}>
                          {s}
                        </SelectItem>
                      ))}
                    </SelectContent>
                  </Select>
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

          {!isEditing && (
            <FormField
              control={form.control}
              name="proofOfAddress"
              render={({ field }) => (
                <FormItem>
                  <FormLabel>Comprovante de Residência</FormLabel>
                  <FormControl>
                    <FileInputButton
                      id={field.name}
                      className="min-w-3xs"
                      onChange={(event) => {
                        const file = event.target.files?.[0];
                        if (file) field.onChange(file);
                      }}
                    >
                      {field.value instanceof File
                        ? `Arquivo: ${field.value.name}`
                        : "Selecionar Comprovante de Residência"}
                    </FileInputButton>
                  </FormControl>
                  <FormMessage />
                </FormItem>
              )}
            />
          )}
        </DoubleColumn>
      </MembersRegisterForm>
    </Form>
  );
}
