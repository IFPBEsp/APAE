"use client";

import React from "react";
import { useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import { z } from "zod";
import { useRouter } from "next/navigation";
import Link from "next/link";
import { Form, FormField, FormItem, FormLabel, FormControl, FormMessage } from "@/components/ui/form";
import { Input } from "@/components/ui/input";
import { PrimaryButton } from "@/components/buttons/ButtonPrimary";
import { Card, CardContent, CardFooter, CardHeader } from "@/components/ui/card";
import { toast } from "react-toastify";
import { forgotPasswordSchema, FormForgotPasswordSchema } from "@/schemas/authSchema";

export default function ResetPasswordPage() {
  const router = useRouter();
  const form = useForm<FormForgotPasswordSchema>({
    resolver: zodResolver(forgotPasswordSchema),
    defaultValues: { 
      email: "",
       code: "" 
      },
      mode: "all",
  });

  const [isLoading, setIsLoading] = React.useState(false);

  const handleSendCode = async () => {
    const isEmailValid = await form.trigger("email");
    if (isEmailValid) {
      try {
        setIsLoading(true);
        const emailDigitado = form.getValues("email");

        // Chamada para o Backend para verificar se o email existe no banco
        // Exemplo de rota: POST /auth/forgot-password
        const response = await fetch("/api/auth/send-reset-code", {
          method: "POST",
          body: JSON.stringify({ email: emailDigitado }),
          headers: { "Content-Type": "application/json" }
        });

        if (response.ok) {
          toast.success("Código de verificação enviado para o seu e-mail.");
        } else {
          const errorData = await response.json();
          toast.error(errorData.message || "Erro ao enviar código. Verifique o e-mail.");
        }
        
      } catch (error) {
        toast.error("Erro de conexão. Tente novamente mais tarde.");
      } finally {
        setIsLoading(false);
      }
    }
  };

  const onSubmit = async (data: FormForgotPasswordSchema) => {
    try {
      // Chama o backend para validar o código
      // Exemplo com a rota /api/auth/verify-code
      const response = await fetch("/api/auth/verify-code", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ 
          email: data.email, 
          code: data.code 
        }),
      });

      if (!response.ok) {
        const error = await response.json();
        toast.error(error.message || "Código inválido ou expirado.");
        return; 
      }

      sessionStorage.setItem("reset_email", data.email);
      sessionStorage.setItem("reset_code", data.code);
      
      toast.success("Código verificado! Agora crie sua nova senha.");
      router.push(`/auth/new-password`);

    } catch (error) {
      toast.error("Erro ao verificar código. Tente novamente.");
    }
  };

  return (
      <Form {...form}>
        <form onSubmit={form.handleSubmit(onSubmit)} >
          <Card 
            className="min-w-[326px] w-[30vw] bg-white rounded-[20px] flex flex-col gap-y-1 pb-4" 
            style={{ boxShadow: "4px 4px 10px rgba(0, 0, 0, 0.25)" }}
          >
            <CardHeader className="pt-10 pb-4">
              <div className="w-full flex justify-center">
                <span className="font-baloo2 font-semibold text-[2.25rem] leading-normal mt-10 text-center text-blue-900">
                  Recuperar Senha
                </span>
              </div>
            </CardHeader>

            <CardContent className="space-y-4">
              <FormField
                control={form.control}
                name="email"
                render={({ field }) => (
                  <FormItem className="min-h-[100px]">
                    <FormLabel className="font-bold text-gray-700">Usuário</FormLabel>
                    <div className="relative flex items-center gap-2">
                      <FormControl>
                        <Input {...field} placeholder="Digite seu email" className="h-12 rounded-lg border-gray-300" />
                      </FormControl>
                      <button
                        type="button"
                        onClick={handleSendCode}
                        className="bg-[#1A589B] p-3 rounded-lg text-white hover:bg-[#14457A] transition-colors"
                      >
                        <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2.5" className="rotate-45">
                          <line x1="22" y1="2" x2="11" y2="13"></line>
                          <polygon points="22 2 15 22 11 13 2 9 22 2"></polygon>
                        </svg>
                      </button>
                    </div>
                    <p className="text-[11px] text-gray-500 mt-1">Um código de verificação será enviado para o seu email.</p>
                    <FormMessage />
                  </FormItem>
                )}
              />

              <FormField
                control={form.control}
                name="code"
                render={({ field }) => (
                  <FormItem className="min-h-[90px]">
                    <FormLabel className="font-bold text-gray-700">Código de verificação</FormLabel>
                    <FormControl>
                      <Input {...field} placeholder="Digite o código" className="h-12 rounded-lg border-gray-300" />
                    </FormControl>
                    <FormMessage />
                  </FormItem>
                )}
              />
            </CardContent>

            <CardFooter className="w-full flex flex-col items-center">
              <PrimaryButton type="submit" className="w-full py-6 text-lg">Enviar</PrimaryButton>
              <Link href="/auth/login" className="mt-4 text-[#F2994A] font-bold underline text-sm">
                Voltar para tela de login.
              </Link>
            </CardFooter>
          </Card>
        </form>
      </Form>
  );
}