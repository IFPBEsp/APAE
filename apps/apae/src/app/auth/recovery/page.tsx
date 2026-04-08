"use client";

import React from "react";
import { Send } from "lucide-react";
import { useRouter } from "next/navigation";
import { toast } from "react-toastify";

import {
  Card,
  CardContent,
  CardFooter,
  CardHeader,
} from "@/components/ui/card";

import {
  Form,
  FormField,
  FormItem,
  FormLabel,
  FormControl,
  FormMessage,
} from "@/components/ui/form";

import { Input } from "@/components/ui/input";
import { PrimaryButton } from "@/components/buttons/ButtonPrimary";

import { useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";

import { recoverySchema, FormRecovery } from "@/schemas/authSchema";

export default function RecoveryPage() {
  const router = useRouter();

  const form = useForm<FormRecovery>({
    resolver: zodResolver(recoverySchema),
    defaultValues: {
      email: "",
      code: "",
    },
    mode: "all",
  });

  const handleSendCode = async () => {
    const email = form.getValues("email");

    if (!email) {
      form.setError("email", {
        message: "E-mail é obrigatório",
      });
      return;
    }

    try {
      //lógica para enviar o código de recuperação para o email (integração com o back-end)
      toast.info("Código enviado para o e-mail informado.");
    } catch {
      toast.error("Erro ao enviar o código.");
    }
  };

  const onSubmit = async (data: FormRecovery) => {
    try {
      //lógica para validar o código e permitir a redefinição de senha
      sessionStorage.setItem("reset_email", data.email);
      sessionStorage.setItem("reset_code", data.code);

      toast.success("Código validado com sucesso!");

      router.push("/auth/new-password");
    } catch {
      toast.error("Código inválido ou expirado.");
    }
  };

  return (
      <Form {...form}>
        <form
          onSubmit={form.handleSubmit(onSubmit)}
          className="w-full h-full flex justify-center"
        >
          <Card
            className="min-w-[326px] w-[30vw] max-h-[90vh]
            bg-white rounded-[20px] overflow-hidden flex flex-col gap-y-1"
            style={{ boxShadow: "4px 4px 10px rgba(0, 0, 0, 0.25)" }}
          >
            <CardHeader className="flex-shrink-0 pt-10 pb-4">
              <div className="w-full flex justify-center">
                <span className="font-baloo2 font-semibold text-[2.25rem] text-center text-blue-900 mt-10">
                  Recuperar Senha
                </span>
              </div>
            </CardHeader>

            <CardContent className="flex-grow overflow-y-auto px-6">
              <div className="flex flex-col space-y-4 max-w-sm mx-auto mt-4">
                {/* EMAIL */}
                <FormField
                  control={form.control}
                  name="email"
                  render={({ field }) => (
                    <FormItem>
                      <FormLabel>Usuário</FormLabel>
                      <FormControl>
                        <div className="flex">
                          <Input
                            {...field}
                            type="email"
                            placeholder="Digite seu email"
                            className="rounded-r-none h-12"
                          />
                          <button
                            type="button"
                            onClick={handleSendCode}
                            className="bg-[#0D4F97]! text-white px-4 flex items-center justify-center rounded-r-md hover:bg-[#0D4F97]/90! cursor-pointer"
                          >
                            <Send size={18} />
                          </button>
                        </div>
                      </FormControl>
                      <FormMessage />
                    </FormItem>
                  )}
                />

                {/* CODE */}
                <FormField
                  control={form.control}
                  name="code"
                  render={({ field }) => (
                    <FormItem>
                      <FormLabel>Código de verificação</FormLabel>
                      <FormControl>
                        <Input
                          {...field}
                          placeholder="Digite o código"
                          className="h-12"
                        />
                      </FormControl>
                      <FormMessage />
                    </FormItem>
                  )}
                />

                <p className="text-xs text-gray-500 text-center">
                  Um código será enviado para seu email.
                </p>
              </div>
            </CardContent>

            <CardFooter className="flex flex-col gap-3 py-4">
              <PrimaryButton
                type="submit"
                loading={form.formState.isSubmitting}
                disabled={form.formState.isSubmitting}
                className="cursor-pointer"
              >
                Validar código
              </PrimaryButton>

              <button
                type="button"
                onClick={() => router.push("/auth/login")}
                className="text-orange-500 text-sm hover:underline cursor-pointer"
              >
                Voltar para tela de login
              </button>
            </CardFooter>
          </Card>
        </form>
      </Form>
  );
}