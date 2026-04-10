"use client";

import React from "react";
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
import { createBaseApi } from "@/lib/axios";

export default function RecoveryPage() {
  const router = useRouter();

  const form = useForm<FormRecovery>({
    resolver: zodResolver(recoverySchema),
    defaultValues: {
      email: "",
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
      const api = await createBaseApi();

      await api.post("/auth/password-recovery", {
        email: email,
      });

      toast.success("Se o e-mail existir, um link de recuperação foi enviado.");
    } catch {
      toast.error("Erro ao solicitar recuperação de senha.");
    }
  };

  const onSubmit = async () => {
    await handleSendCode();
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
                      <Input
                        {...field}
                        type="email"
                        placeholder="Digite seu email"
                        className="h-12"
                      />
                    </FormControl>
                    <FormMessage />
                  </FormItem>
                )}
              />

              <p className="text-xs text-gray-500 text-center">
                Um link de recuperação será enviado para seu email.
              </p>
            </div>
          </CardContent>

          <CardFooter className="flex flex-col gap-3 py-4">
            <PrimaryButton
              type="submit"
              loading={form.formState.isSubmitting}
              disabled={form.formState.isSubmitting}
              className="cursor-pointer w-full text-sm px-4 py-3 whitespace-nowrap"
            >
              Enviar link de recuperação
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