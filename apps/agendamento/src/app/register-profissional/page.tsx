"use client";

import {
  useForm,
  Controller,
  type SubmitHandler,
} from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import * as z from "zod";
import { InputMask } from "@react-input/mask";

//import { Button } from "@/components/ui/button";
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
import { useCreateProfissional } from "@/hooks/profissional/use-create-profissional";
import { cadastroSchema } from "@/schemas/profissional.schema";
import { HEALTH_AREAS } from "@/lib/health-areas";
import { STATES } from "@/lib/states";
import { JSX } from "react";

import {
  MembersRegisterForm,
  DoubleColumn,
  FormButton,
} from "@/components/forms/MembersRegisterComponents";

type CadastroFormValues = z.infer<typeof cadastroSchema>;

export default function CadastroProfissional(): JSX.Element {
  const router = useRouter();
  const { create, loading, error, success } = useCreateProfissional();

  const defaultValues: CadastroFormValues = {
    nomeCompleto: "",
    email: "",
    documentoProfissional: "",
    areaSaude: "",
    telefone: "",
    rg: "",
    estado: "",
    cidade: "",
    bairro: "",
    rua: "",
    numero: "",
    complemento: "",
    cep: "",
  };

  const form = useForm<CadastroFormValues>({
    resolver: zodResolver(cadastroSchema),
    defaultValues,
  });

  const onCancel = () => {
    router.push("/visualization-professional");
  };

  const onSubmit: SubmitHandler<CadastroFormValues> = async (values) => {
    const payload = {
      nome: values.nomeCompleto.trim(),
      email: values.email.trim(),
      docProfissional: values.documentoProfissional.trim(),
      areaDaSaude: values.areaSaude,
      telefone: values.telefone,
      rg: values.rg.trim(),
      endereco: {
        estado: values.estado,
        cidade: values.cidade.trim(),
        bairro: values.bairro.trim(),
        rua: values.rua.trim(),
        numero: values.numero?.trim(),
        complemento: values.complemento?.trim(),
        cep: values.cep,
      },
    };

    await create(payload);

    if (success) {
      router.push("/visualization-professional");
    }

  };

  return (
    <Form {...form}>
      <MembersRegisterForm
        title=""
        onSubmit={form.handleSubmit(onSubmit)}
        buttons={
          <>
            <FormButton 
              type="button" 
              onClick={() => router.push("/visualization-professional")} 
              className="border border-input bg-background text-foreground hover:bg-accent hover:text-accent-foreground"
            >
              Cancelar
            </FormButton>

            <FormButton 
              type="submit" 
              disabled={form.formState.isSubmitting || loading}
            >
              Cadastrar
            </FormButton>
          </>
        }
      >
        <DoubleColumn>
          <FormField
            control={form.control}
            name="nomeCompleto"
            render={({ field }) => (
              <FormItem className="md:col-span-2">
                <FormLabel>Nome Completo *</FormLabel>
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
              <FormItem className="md:col-span-2">
                <FormLabel>Email *</FormLabel>
                <FormControl>
                  <Input
                    type="email"
                    placeholder="profissional@gmail.com"
                    {...field}
                  />
                </FormControl>
                <FormMessage />
              </FormItem>
            )}
          />

          <FormField
            control={form.control}
            name="documentoProfissional"
            render={({ field }) => (
              <FormItem>
                <FormLabel>Documento profissional *</FormLabel>
                <FormControl>
                  <Input placeholder="Ex: CRM/SP 123456" {...field} />
                </FormControl>
                <FormMessage />
              </FormItem>
            )}
          />

          <FormField
            control={form.control}
            name="areaSaude"
            render={({ field }) => (
              <FormItem>
                <FormLabel>Área da saúde *</FormLabel>
                <FormControl>
                  <Select onValueChange={field.onChange} value={field.value}>
                    <SelectTrigger>
                      <SelectValue placeholder="Selecione uma opção" />
                    </SelectTrigger>
                    <SelectContent>
                      {HEALTH_AREAS.map((area) => (
                        <SelectItem key={area} value={area}>
                          {area}
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

          <FormField
            control={form.control}
            name="telefone"
            render={({ field }) => (
              <FormItem>
                <FormLabel>Telefone *</FormLabel>
                <FormControl>
                  <InputMask
                    mask="(__) _____-____"
                    replacement={{ _: /\d/ }}
                    placeholder="(00) 00000-0000"
                    value={field.value}
                    onChange={(e) => field.onChange(e.target.value)}
                    className="w-full px-3 py-2 border rounded-md"
                  />
                </FormControl>
                <FormMessage />
              </FormItem>
            )}
          />

          <FormField
            control={form.control}
            name="estado"
            render={({ field }) => (
              <FormItem>
                <FormLabel>Estado *</FormLabel>
                <FormControl>
                  <Select onValueChange={field.onChange} value={field.value}>
                    <SelectTrigger>
                      <SelectValue placeholder="Selecione uma opção" />
                    </SelectTrigger>
                    <SelectContent>
                      {STATES.map((estado) => (
                        <SelectItem key={estado} value={estado}>
                          {estado}
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
            name="cidade"
            render={({ field }) => (
              <FormItem>
                <FormLabel>Cidade *</FormLabel>
                <FormControl>
                  <Input placeholder="Ex: Esperança" {...field}/>
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
                  <Input placeholder="Digite o CEP" {...field}/>
                </FormControl>
                <FormMessage />
              </FormItem>
            )}
        />
        
        <FormField
            control={form.control}
            name="bairro"
            render={({ field }) => (
              <FormItem>
                <FormLabel>Bairro *</FormLabel>
                <FormControl>
                  <Input placeholder="Ex: Centro" {...field}/>
                </FormControl>
                <FormMessage />
              </FormItem>
            )}
          />

          <FormField
            control={form.control}
            name="rua"
            render={({ field }) => (
              <FormItem>
                <FormLabel>Endereço *</FormLabel>
                <FormControl>
                  <Input
                    placeholder="Ex: Rua João Pessoa" {...field}/>
                </FormControl>
                <FormMessage />
              </FormItem>
            )}
          />

          <FormField
            control={form.control}
            name="numero"
            render={({ field }) => (
              <FormItem>
                <FormLabel>Número *</FormLabel>
                <FormControl>
                  <Input placeholder="Ex: 12" {...field}/>
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
                  <Input placeholder="Digite o complemento do endereço" {...field}/>
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

