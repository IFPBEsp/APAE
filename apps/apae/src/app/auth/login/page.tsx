"use client";

import React from "react";
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
import { useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import Link from "next/link";
import { PrimaryButton } from "@/components/buttons/ButtonPrimary";
import { PasswordInput } from "@/components/forms/PasswordInputs";
import { loginSchema, FormLogin } from "@/schemas/authSchema";
import { toast } from "react-toastify";
import { useRouter } from "next/navigation";
import { formatCPF } from "@/lib/formats";

function LoginPage() {
  const router = useRouter();
  const form = useForm<FormLogin>({
    resolver: zodResolver(loginSchema),
    defaultValues: {
      username: "",
      password: "",
    },
    mode: "all",
  });

  const onSubmit = async (data: FormLogin) => {
    try {
      const res = await fetch("/api/auth/login", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(data),
      });

      const responseData = await res.json();

      if (res.ok) {
        toast.success(responseData.message || "Login bem-sucedido!");
        router.push("/");
      } else {
        toast.error(responseData.message || "Credenciais inválidas");
      }
    } catch (err) {
      console.error(err);
      toast.error("Erro inesperado. Tente novamente mais tarde.");
    }
  };

  return (
    <Form {...form}>
      <form onSubmit={form.handleSubmit(onSubmit)} className="w-full h-full">
        <Card
          className="min-w-[326px] w-[30vw] max-h-[90vh]
                  bg-white rounded-[20px] overflow-hidden flex flex-col gap-y-1"
          style={{ boxShadow: "4px 4px 10px rgba(0, 0, 0, 0.25)" }}
        >
          <CardHeader className="flex-shrink-0 pt-10 pb-4">
            <div className="w-full flex justify-center">
              <span className="font-baloo2 font-semibold text-[2.25rem] leading-normal mt-10 text-center text-blue-900">
                Entrar
              </span>
            </div>
          </CardHeader>
          <CardContent className="flex-grow overflow-y-auto px-6">
            <div className="flex flex-col space-y-1 max-w-sm mx-auto">
              <FormField
                control={form.control}
                name="username"
                render={({ field }) => (
                  <FormItem>
                    <FormLabel className="text-sm font-medium text-foreground">
                      Usuário
                    </FormLabel>
                    <FormControl>
                      <Input
                        {...field}
                        onChange={(e) => {
                          const regexForPossibleCPF = new RegExp(
                            /(^\d{4,}$)|(^\d{3}\.\d{4,}$)|(^\d{3}\.\d{3}\.\d{4,}$)|^\d{3}\.\d{3}\.\d{3}\-\d{2,}$/,
                          );
                          const formatted = regexForPossibleCPF.test(
                            e.target.value,
                          )
                            ? formatCPF(e.target.value)
                            : e.target.value;

                          field.onChange(formatted);
                        }}
                        placeholder="Digite seu email ou CPF"
                        className="w-full bg-white border border-border rounded-md h-12 px-3"
                      />
                    </FormControl>
                    <div className="min-h-[18px]">
                      <FormMessage />
                    </div>
                  </FormItem>
                )}
              />
              <FormField
                control={form.control}
                name="password"
                render={({ field }) => (
                  <FormItem>
                    <FormLabel className="text-sm font-medium text-foreground">
                      Senha
                    </FormLabel>
                    <FormControl>
                      <PasswordInput
                        {...field}
                        placeholder="Digite sua senha"
                        className="w-full bg-white border border-border rounded-md h-12 px-3"
                      />
                    </FormControl>
                    <div className="min-h-[18px]">
                      <FormMessage />
                    </div>
                  </FormItem>
                )}
              />
            </div>
            <div className="text-right mt-0">
              <Link
                href="/auth/reset-password"
                className="text-sm text-gray-600 hover:text-blue-600 transition-colors duration-200"
              >
                Esqueceu a senha?
              </Link>
            </div>
          </CardContent>
          <CardFooter className="flex-shrink-0 w-full flex-col justify-center mt-1 py-0 border-gray-100">
            <PrimaryButton
              type="submit"
              loading={form.formState.isSubmitting}
              disabled={form.formState.isSubmitting}
            >
              Entrar
            </PrimaryButton>
          </CardFooter>
        </Card>
      </form>
    </Form>
  );
}

export default LoginPage;
