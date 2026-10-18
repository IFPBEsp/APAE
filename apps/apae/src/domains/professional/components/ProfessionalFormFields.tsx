"use client";

import { Controller, useFormContext } from "react-hook-form";
import { InputMask } from "@react-input/mask";
import { Input } from "@/components/ui/input";
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from "@/components/ui/select";
import {
  FormControl,
  FormField,
  FormItem,
  FormLabel,
  FormMessage,
} from "@/components/ui/form";
import Availability from "@/components/forms/AvailabilityForm";
import HealthAreaSelect from "@/components/shared/HealthAreaSelect";
import { STATES } from "@/lib/states";
import type { ProfessionalFormValues } from "@/schemas/profissional.schema";

/**
 * Campos comuns do profissional (fullName ... complement) + disponibilidade.
 * Usado nas páginas de cadastro e de edição.
 *
 * Lê o form via contexto: as duas páginas já envolvem tudo em <Form {...form}>,
 * que é um FormProvider. Assim não há conflito de tipo entre
 * RegisterProfessionalFormValues e UpdateProfessionalFormValues.
 */
export function ProfessionalFormFields() {
  const form = useFormContext<ProfessionalFormValues>();

  return (
    <>
      <FormField
        control={form.control}
        name="fullName"
        render={({ field }) => (
          <FormItem>
            <FormLabel>Nome completo *</FormLabel>
            <FormControl>
              <Input placeholder="Ex: Maria da Silva" {...field} />
            </FormControl>
            <FormMessage />
          </FormItem>
        )}
      />

      <FormField
        control={form.control}
        name="email"
        render={({ field }) => (
          <FormItem>
            <FormLabel>Email *</FormLabel>
            <FormControl>
              <Input type="email" placeholder="profissional@exemplo.com" {...field} />
            </FormControl>
            <FormMessage />
          </FormItem>
        )}
      />

      <Controller
        control={form.control}
        name="cpf"
        render={({ field, fieldState }) => (
          <FormItem>
            <FormLabel>CPF *</FormLabel>
            <FormControl>
              <InputMask
                mask="___.___.___-__"
                replacement={{ _: /\d/ }}
                value={field.value ?? ""}
                onChange={(e) => field.onChange(e.target.value)}
                onBlur={field.onBlur}
                placeholder="000.000.000-00"
                className="w-full rounded-md border px-3 py-2"
              />
            </FormControl>
            <FormMessage>{fieldState.error?.message}</FormMessage>
          </FormItem>
        )}
      />

      <div className="grid grid-cols-2 gap-4">
        <FormField
          control={form.control}
          name="professionalDocument"
          render={({ field }) => (
            <FormItem>
              <FormLabel>Documento profissional</FormLabel>
              <FormControl>
                <Input placeholder="Ex: CRM/SP 123456" {...field} value={field.value || ""} />
              </FormControl>
              <FormMessage />
            </FormItem>
          )}
        />
        <Controller
          control={form.control}
          name="serviceArea"
          render={({ field, fieldState }) => (
            <FormItem>
              <FormLabel>Área de atendimento *</FormLabel>
              <FormControl>
                <HealthAreaSelect value={field.value} onChange={field.onChange} />
              </FormControl>
              <FormMessage>{fieldState.error?.message}</FormMessage>
            </FormItem>
          )}
        />
      </div>

      <div className="grid grid-cols-2 gap-4">
        <FormField
          control={form.control}
          name="rg"
          render={({ field }) => (
            <FormItem>
              <FormLabel>RG *</FormLabel>
              <FormControl>
                <Input placeholder="Ex: 1234567" {...field} />
              </FormControl>
              <FormMessage />
            </FormItem>
          )}
        />
        <Controller
          control={form.control}
          name="phone"
          render={({ field, fieldState }) => (
            <FormItem>
              <FormLabel>Telefone *</FormLabel>
              <FormControl>
                <InputMask
                  mask="(__) _____-____"
                  replacement={{ _: /\d/ }}
                  value={field.value ?? ""}
                  onChange={(e) => field.onChange(e.target.value)}
                  onBlur={field.onBlur}
                  placeholder="(xx) xxxxx-xxxx"
                  className="w-full rounded-md border px-3 py-2"
                />
              </FormControl>
              <FormMessage>{fieldState.error?.message}</FormMessage>
            </FormItem>
          )}
        />
      </div>

      <div className="grid grid-cols-2 gap-4">
        <Controller
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
              <FormMessage>{fieldState.error?.message}</FormMessage>
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
                <Input placeholder="Ex: João Pessoa" {...field} />
              </FormControl>
              <FormMessage />
            </FormItem>
          )}
        />
      </div>

      <FormField
        control={form.control}
        name="street"
        render={({ field }) => (
          <FormItem>
            <FormLabel>Endereço *</FormLabel>
            <FormControl>
              <Input placeholder="Ex: Rua das Flores" {...field} />
            </FormControl>
            <FormMessage />
          </FormItem>
        )}
      />

      <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
        <FormField
          control={form.control}
          name="neighborhood"
          render={({ field }) => (
            <FormItem>
              <FormLabel>Bairro *</FormLabel>
              <FormControl>
                <Input placeholder="Ex: Centro" {...field} />
              </FormControl>
              <FormMessage />
            </FormItem>
          )}
        />
        <Controller
          control={form.control}
          name="cep"
          render={({ field, fieldState }) => (
            <FormItem>
              <FormLabel>CEP *</FormLabel>
              <FormControl>
                <InputMask
                  mask="_____-___"
                  replacement={{ _: /\d/ }}
                  value={field.value ?? ""}
                  onChange={(e) => field.onChange(e.target.value)}
                  onBlur={field.onBlur}
                  placeholder="12345-678"
                  className="w-full rounded-md border px-3 py-2"
                />
              </FormControl>
              <FormMessage>{fieldState.error?.message}</FormMessage>
            </FormItem>
          )}
        />
      </div>

      <div className="grid grid-cols-2 gap-4">
        <FormField
          control={form.control}
          name="number"
          render={({ field }) => (
            <FormItem>
              <FormLabel>Número *</FormLabel>
              <FormControl>
                <Input placeholder="Ex: 123" {...field} />
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
                <Input placeholder="Ex: Apt 101" {...field} />
              </FormControl>
              <FormMessage />
            </FormItem>
          )}
        />
      </div>

      <Availability control={form.control} watch={form.watch} />
    </>
  );
}
