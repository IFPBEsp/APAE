"use client";

import { useForm, Controller, type SubmitHandler } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import * as z from "zod";
import { InputMask } from "@react-input/mask";
import { User } from "lucide-react";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from "@/components/ui/select";
import {
  Form,
  FormControl,
  FormField,
  FormItem,
  FormLabel,
  FormMessage,
} from "@/components/ui/form";
import { useRouter } from "next/navigation";
import { useCreateProfessional } from "@/hooks/profissional/use-create-profissional";
import Disponibilidade from "@/components/forms/DisponibilidadeForm";
import { cadastroSchema } from "@/schemas/profissional.schema";
import { STATES } from "@/lib/states";
import { useRef, useState, useEffect, JSX } from "react";
import HealthAreaSelect from "@/components/shared/HealthAreaSelect";
import { generateAvailabilityMatrix } from "@/domains/professional/shared/disponibilidade.utils";
import { Avatar, AvatarFallback, AvatarImage } from "@/components/ui/avatar";

type CadastroFormValues = z.infer<typeof cadastroSchema>;

export default function CadastroProfessional(): JSX.Element {
  const router = useRouter();
  const fileInputRef = useRef<HTMLInputElement>(null);
  const [previewUrl, setPreviewUrl] = useState<string | null>(null);
  const { create, loading, error, success } = useCreateProfessional();

  const defaultValues: Partial<CadastroFormValues> = {
    nomeCompleto: "",
    email: "",
    professionalDocument: "",
    serviceArea: "",
    telefone: "",
    rg: "",
    state: "",
    city: "",
    bairro: "",
    rua: "",
    numero: "",
    complemento: "",
    cep: "",
    disponibilidade: generateAvailabilityMatrix([]),
  };

  const form = useForm<CadastroFormValues>({
    resolver: zodResolver(cadastroSchema),
    defaultValues,
  });

  const photoFile = form.watch("photo");

  useEffect(() => {
    if (photoFile instanceof File) {
      const url = URL.createObjectURL(photoFile);
      setPreviewUrl(url);
      return () => URL.revokeObjectURL(url);
    }
  }, [photoFile]);

  const onCancel = () => {
    router.push("/professionals");
  };

  const onSubmit: SubmitHandler<CadastroFormValues> = async (values) => {
    const formData = new FormData();

    const availabilities = values.disponibilidade
      .filter((d) => d?.checked)
      .map((d) => ({
        day: d?.dia,
        shift: d?.turno,
      }));

    const payload = {
      serviceArea: { area: values.serviceArea },
      phoneNumber: values.telefone,
      professionalDocument: values.professionalDocument?.trim() || null,
      email: values.email.trim(),
      name: values.nomeCompleto.trim(),
      identityDocument: values.rg.trim(),
      address: {
        state: values.state,
        city: values.city.trim(),
        neighborhood: values.bairro.trim(),
        street: values.rua.trim(),
        number: values.numero?.trim(),
        complement: values.complemento?.trim() ?? "",
        cep: values.cep,
      },
      availabilities,
    };

    formData.append(
      "professional",
      new Blob([JSON.stringify(payload)], { type: "application/json" })
    );
    
    // profilePhoto faz referência ao componente do back, deve estar alinhado quando for fazer a integração
    if (values.photo) {
      formData.append("profilePhoto", values.photo);
    }

    formData.append("volunteerAgreement", values.termoVoluntariado);
    formData.append("curriculum", values.curriculo);
    if (values.anexoQualquer) {
      formData.append("attachmentAny", values.anexoQualquer);
    }
    await create(formData);
  };

  return (
    <div className="p-0">
      <Form {...form}>
        <form
          onSubmit={form.handleSubmit(onSubmit)}
          className="space-y-6 w-full max-w-2xl"
        >
          <FormField
            control={form.control}
            name="nomeCompleto"
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
                  <Input
                    type="email"
                    placeholder="profissional@exemplo.com"
                    {...field}
                  />
                </FormControl>
                <FormMessage />
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
                    <HealthAreaSelect
                      value={field.value}
                      onChange={field.onChange}
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
              name="telefone"
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
                        className={`w-full ${
                          fieldState.invalid
                            ? "border-red-500"
                            : "border-gray-300"
                        }`}
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
            name="rua"
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
              name="bairro"
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
              name="numero"
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
              name="complemento"
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

          <FormField
            control={form.control}
            name="photo"
            render={({ field }) => (
              <FormItem>
                <FormLabel className="text-sm font-medium">
                  Selecione uma foto*
                </FormLabel>
                <FormControl>
                  <div className="flex flex-col items-start gap-4 w-full">
                    <input
                      ref={fileInputRef}
                      type="file"
                      id={`${field.name}-upload`}
                      className="hidden"
                      accept="image/png, image/jpeg"
                      onChange={(e) => {
                        const file = e.target.files?.[0];
                        if (file) {
                          field.onChange(file);
                        }
                      }}
                    />

                    <button
                      type="button"
                      onClick={() => fileInputRef.current?.click()}
                      className="relative group mr-auto rounded-full transition-transform hover:scale-105"
                    >
                      <Avatar className="w-32 h-32 border-2 border-dashed border-gray-300 bg-gray-50 cursor-pointer flex items-center justify-center">
                        <AvatarImage src={previewUrl || ""} alt="Foto do profissional" />
                        <AvatarFallback className="bg-transparent">
                          <User className="w-12 h-12 text-gray-400" />
                        </AvatarFallback>
                      </Avatar>

                      <div className="absolute inset-0 flex items-center justify-center opacity-0 group-hover:opacity-100 transition-opacity bg-black/20 rounded-full">
                        <span className="bg-white text-black text-[10px] font-bold px-2 py-1 rounded shadow-sm">
                          Escolher foto
                        </span>
                      </div>
                    </button>
                  </div>
                </FormControl>
                <FormMessage />
              </FormItem>
            )}
          />

          <FormField
            control={form.control}
            name="termoVoluntariado"
            render={({ field }) => (
              <FormItem>
                <FormLabel>Termo do Voluntário *</FormLabel>
                <FormControl>
                  <Input
                    type="file"
                    accept="image/*, application/pdf"
                    onChange={(e) =>
                      field.onChange(e.target.files?.[0] ?? null)
                    }
                  />
                </FormControl>
                <FormMessage />
              </FormItem>
            )}
          />

          <FormField
            control={form.control}
            name="curriculo"
            render={({ field }) => (
              <FormItem>
                <FormLabel>Currículo *</FormLabel>
                <FormControl>
                  <Input
                    type="file"
                    accept="image/*, application/pdf"
                    onChange={(e) =>
                      field.onChange(e.target.files?.[0] ?? null)
                    }
                  />
                </FormControl>
                <FormMessage />
              </FormItem>
            )}
          />
          
          <FormField
            control={form.control}
            name="anexoQualquer"
            render={({ field }) => (
              <FormItem>
                <FormLabel>Anexo qualquer</FormLabel>
                <FormControl>
                  <Input
                    type="file"
                    accept="image/*, application/pdf"
                    onChange={(e) =>
                      field.onChange(e.target.files?.[0] ?? null)
                    }
                  />
                </FormControl>
                <FormMessage />
              </FormItem>
            )}
          />

          <Disponibilidade control={form.control} watch={form.watch} />

          {loading && <p className="text-blue-500">Salvando...</p>}
          {error && <p className="text-red-500">{error}</p>}
          {success && (
            <p className="text-green-600">Profissional criado com sucesso!</p>
          )}
          <div className="flex justify-end gap-4">
            <Button type="button" variant="outline" onClick={onCancel}>
              Cancelar
            </Button>
            <Button
              type="submit"
              className="bg-[#0D4F97] hover:bg-blue-900"
              disabled={loading}
            >
              Cadastrar
            </Button>
          </div>
        </form>
      </Form>
    </div>
  );
}
