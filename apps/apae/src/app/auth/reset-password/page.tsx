"use client";

import React from "react";
import { useRouter } from "next/navigation";
import { useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import Link from "next/link";
import { Form, FormField, FormItem, FormLabel, FormControl, FormMessage } from "@/components/ui/form";
import { PasswordInput } from "@/components/forms/PasswordInputs";
import { PrimaryButton } from "@/components/buttons/ButtonPrimary";
import { Card, CardContent, CardFooter, CardHeader } from "@/components/ui/card";
import { toast } from "react-toastify";
import { newPasswordSchema, FormNewPasswordSchema } from "@/schemas/authSchema";

export default function NewPasswordPage() {
  const router = useRouter();
  const [sessionData, setSessionData] = React.useState({ email: "", code: "" });

  const form = useForm<FormNewPasswordSchema>({
    resolver: zodResolver(newPasswordSchema),
    defaultValues: { 
      senha: "", 
      confirmarSenha: ""
    },
    mode: "all",
  });

  React.useEffect(() => {
    // Conexão com a tela de validação vencida, atualize para o link enviado por email
    const email = sessionStorage.getItem("reset_email");
    const code = sessionStorage.getItem("reset_code");

    if (!email || !code) {
      toast.error("Sessão expirada. Identifique-se novamente.");
      router.push("/auth/reset-password");
      return;
    }

    setSessionData({ email, code });
  }, [router]);

  const onSubmit = async (data: FormNewPasswordSchema) => {
    try {
      const payload = {
        email: sessionData.email, 
        code: sessionData.code,   
        password: data.senha,     
      };

      // Chamada para o backend 
      // Exemplo de rota /api/auth/reset-password
      const response = await fetch("/api/auth/reset-password", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify(payload),
      });

      if (!response.ok) {
        const errorData = await response.json();
        throw new Error(errorData.message || "Erro ao redefinir a senha.");
      }

      toast.success("Senha alterada com sucesso.");
      sessionStorage.clear(); 
      router.push("/auth/login");

    } catch (err: any) {
      toast.error(err.message || "Ocorreu um erro inesperado. Tente novamente.");
      console.error("Erro no Reset Password:", err);
    }
  };

  return (
      <Form {...form}>
        <form onSubmit={form.handleSubmit(onSubmit)}>
          <Card
            className="min-w-[326px] w-[30vw] max-h-[90vh]
                    bg-white rounded-[20px] overflow-hidden flex flex-col gap-y-1"
            style={{ boxShadow: "4px 4px 10px rgba(0, 0, 0, 0.25)" }}
            >
            <CardHeader className="flex-shrink-0 pt-10 pb-4">
                <div className="w-full flex justify-center">
                    <span className="font-baloo2 font-semibold text-[2.25rem] leading-normal mt-10 text-center text-blue-900">
                        Recuperar Senha
                    </span>
                </div>
            </CardHeader>

            <CardContent className="w-full space-y-6">
              <FormField
                control={form.control}
                name="senha"
                render={({ field }) => (
                  <FormItem>
                    <FormLabel className="font-bold text-gray-700">Nova senha</FormLabel>
                    <FormControl>
                      <PasswordInput {...field} placeholder="Digite a senha" className="h-12 border-gray-300" />
                    </FormControl>
                    <FormMessage />
                  </FormItem>
                )}
              />

              <FormField
                control={form.control}
                name="confirmarSenha"
                render={({ field }) => (
                  <FormItem>
                    <FormLabel className="font-bold text-gray-700">Confirmar senha</FormLabel>
                    <FormControl>
                      <PasswordInput {...field} placeholder="Confirme sua senha" className="h-12 border-gray-300" />
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