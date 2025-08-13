"use client";

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
import React from "react";
import { zodResolver } from "@hookform/resolvers/zod";
import Link from "next/link";
import { PrimaryButton } from "@/components/ButtonPrimary";
import { PasswordInput } from "@/components/PasswordInputs";
import { FormSignUp, signUpSchema } from "@/schemas/authSchema";
import { toast } from "react-toastify";
import { useRouter } from "next/navigation";

function Page() {
  const router = useRouter();
  const form = useForm<FormSignUp>({
    resolver: zodResolver(signUpSchema),
    defaultValues: {
      nomeCompleto: "",
      email: "",
      cpf: "",
      senha: "",
      confirmarSenha: "",
    },
    mode: "all",
  });

  const formatCPF = (value: string) => {
    const numbers = value.replace(/\D/g, "");
    if (numbers.length <= 3) return numbers;
    if (numbers.length <= 6)
      return `${numbers.slice(0, 3)}.${numbers.slice(3)}`;
    if (numbers.length <= 9)
      return `${numbers.slice(0, 3)}.${numbers.slice(3, 6)}.${numbers.slice(
        6
      )}`;
    return `${numbers.slice(0, 3)}.${numbers.slice(3, 6)}.${numbers.slice(
      6,
      9
    )}-${numbers.slice(9, 11)}`;
  };

  const onSubmit = async (data: FormSignUp) => {
    try {
      const res = await fetch("/api/register", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(data),
      });
      const responseData = await res.json();

      if (res.ok) {
        toast.success(responseData.message);
        router.push("/auth/login");
      } else {
        console.error("Erro ao cadastrar usuário:", responseData.message);
        toast.error(responseData.message || "Erro ao cadastrar");
      }
    } catch (err) {
      console.error(err);
      toast.error("Erro inesperado");
    }
  };

  return (
    <Form {...form}>
      <form onSubmit={form.handleSubmit(onSubmit)} className="h-full">
        <Card
          className="min-w-[326px] w-[30vw] min-h-[326px] h-[90vh]
                  bg-white rounded-[20px] overflow-hidden flex flex-col"
          style={{ boxShadow: "4px 4px 10px rgba(0, 0, 0, 0.25)" }}
        >
          <CardHeader className="flex-shrink-0 pt-20">
            <div className="w-full flex justify-center">
              <span className="font-baloo2 font-semibold text-[2.25rem] leading-[58px] text-center text-blue-900">
                Cadastrar
              </span>
            </div>
          </CardHeader>

          <CardContent className="flex-grow overflow-y-auto px-6 py-2">
            <div className="flex flex-col space-y-1 max-w-sm mx-auto">
              <FormField
                control={form.control}
                name="nomeCompleto"
                render={({ field }) => (
                  <FormItem>
                    <FormLabel className="text-sm font-medium text-foreground">
                      Nome Completo
                    </FormLabel>
                    <FormControl>
                      <Input
                        {...field}
                        placeholder="Digite seu nome completo"
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
                name="email"
                render={({ field }) => (
                  <FormItem>
                    <FormLabel className="text-sm font-medium text-foreground">
                      Email
                    </FormLabel>
                    <FormControl>
                      <Input
                        {...field}
                        type="email"
                        placeholder="Digite seu email"
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
                name="cpf"
                render={({ field }) => (
                  <FormItem>
                    <FormLabel className="text-sm font-medium text-foreground">
                      CPF
                    </FormLabel>
                    <FormControl>
                      <Input
                        {...field}
                        placeholder="000.000.000-00"
                        maxLength={14}
                        className="w-full bg-white border border-border rounded-md h-12 px-3"
                        onChange={(e) => {
                          const formatted = formatCPF(e.target.value);
                          field.onChange(formatted);
                        }}
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
                name="senha"
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

              <FormField
                control={form.control}
                name="confirmarSenha"
                render={({ field }) => (
                  <FormItem>
                    <FormLabel className="text-sm font-medium text-foreground">
                      Confirmar senha
                    </FormLabel>
                    <FormControl>
                      <PasswordInput
                        {...field}
                        placeholder="Confirme sua senha"
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
          </CardContent>

          <CardFooter className="flex-shrink-0 w-full flex-col justify-center py-3 border-gray-100">
            <PrimaryButton
              type="submit"
              loading={form.formState.isSubmitting}
              disabled={form.formState.isSubmitting}
            >
              Cadastrar
            </PrimaryButton>

            <div
              className="
              mt-4 sm:mt-6 
              font-baloo2 font-medium 
              text-xs sm:text-sm 
              leading-[150%] 
              text-[#222222]
              text-center
              px-4
            "
            >
              Já possui uma conta ?{" "}
              <Link
                href={"/auth/login"}
                className="
                  !text-[#F28C38] hover:!text-[#F28C38]/80 
                  underline hover:no-underline
                  font-medium
                  transition-colors duration-200
                  focus:outline-none focus:ring-2 focus:!ring-[#F28C38]/50 focus:ring-offset-1
                  rounded-sm px-1
                  [&]:!text-[#F28C38] [&:hover]:!text-[#F28C38]/80
                "
                style={{
                  color: "#F28C38 !important",
                }}
              >
                Entrar
              </Link>
            </div>
          </CardFooter>
        </Card>
      </form>
    </Form>
  );
}

export default Page;
