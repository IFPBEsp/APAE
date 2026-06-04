"use client";

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
import { Button } from "@/components/ui/button";
import { PasswordInput } from "@/components/forms/PasswordInputs";
import { FormSignUp, signUpSchema } from "@/schemas/authSchema";
import { toast } from "react-toastify";
import { useRouter } from "next/navigation";

function Page() {
  const router = useRouter();
  const form = useForm<FormSignUp>({
    resolver: zodResolver(signUpSchema),
    defaultValues: {
      fullName: "",
      email: "",
      cpf: "",
      password: "",
      confirmPassword: "",
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
        6,
      )}`;
    return `${numbers.slice(0, 3)}.${numbers.slice(3, 6)}.${numbers.slice(
      6,
      9,
    )}-${numbers.slice(9, 11)}`;
  };

  const onSubmit = async (data: FormSignUp) => {
    try {
      const res = await fetch("/apae-geral/api/auth/register", {
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
    <div className="w-full p-4 md:p-6">
      <div className="mx-auto w-full max-w-[1200px]">
        <div className="mb-6">
          <h1 className="text-lg font-bold sm:text-2xl text-[#0D4F97]">
            Cadastrar Novo Usuário
          </h1>
        </div>

        <div className="bg-white rounded-[20px] border border-gray-100 p-8 shadow-sm">
          <Form {...form}>
            <form onSubmit={form.handleSubmit(onSubmit)} className="space-y-6">
              <div className="grid grid-cols-1 md:grid-cols-2 gap-x-8 gap-y-6">
                
                <FormField
                  control={form.control}
                  name="fullName"
                  render={({ field }) => (
                    <FormItem>
                      <FormLabel className="text-[#0D4F97] font-semibold">Nome Completo</FormLabel>
                      <FormControl>
                        <Input 
                          {...field} 
                          placeholder="Digite o nome completo" 
                          className="h-12 border-[#0D4F97]/20 focus:border-[#0D4F97] focus-visible:ring-0"
                        />
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
                      <FormLabel className="text-[#0D4F97] font-semibold">Email</FormLabel>
                      <FormControl>
                        <Input 
                          {...field} 
                          type="email" 
                          placeholder="exemplo@email.com" 
                          className="h-12 border-[#0D4F97]/20 focus:border-[#0D4F97] focus-visible:ring-0"
                        />
                      </FormControl>
                      <FormMessage />
                    </FormItem>
                  )}
                />

                <FormField
                  control={form.control}
                  name="cpf"
                  render={({ field }) => (
                    <FormItem>
                      <FormLabel className="text-[#0D4F97] font-semibold">CPF</FormLabel>
                      <FormControl>
                        <Input
                          {...field}
                          placeholder="000.000.000-00"
                          maxLength={14}
                          className="h-12 border-[#0D4F97]/20 focus:border-[#0D4F97] focus-visible:ring-0"
                          onChange={(e) => field.onChange(formatCPF(e.target.value))}
                        />
                      </FormControl>
                      <FormMessage />
                    </FormItem>
                  )}
                />

                <div className="md:col-span-2 grid grid-cols-1 md:grid-cols-2 gap-8">
                  <FormField
                    control={form.control}
                    name="password"
                    render={({ field }) => (
                      <FormItem>
                        <FormLabel className="text-[#0D4F97] font-semibold">Senha</FormLabel>
                        <FormControl>
                          <PasswordInput 
                            {...field} 
                            placeholder="Crie uma senha" 
                            className="h-12 border-[#0D4F97]/20 focus:border-[#0D4F97] focus-visible:ring-0"
                          />
                        </FormControl>
                        <FormMessage />
                      </FormItem>
                    )}
                  />

                  <FormField
                    control={form.control}
                    name="confirmPassword"
                    render={({ field }) => (
                      <FormItem>
                        <FormLabel className="text-[#0D4F97] font-semibold">Confirmar Senha</FormLabel>
                        <FormControl>
                          <PasswordInput 
                            {...field} 
                            placeholder="Repita a senha" 
                            className="h-12 border-[#0D4F97]/20 focus:border-[#0D4F97] focus-visible:ring-0"
                          />
                        </FormControl>
                        <FormMessage />
                      </FormItem>
                    )}
                  />
                </div>
              </div>

              <div className="flex justify-end gap-4 pt-8">
                <Button
                  type="button"
                  variant="ghost"
                  onClick={() => router.back()}
                  className="w-32 h-11 text-gray-500 hover:text-gray-700"
                >
                  Cancelar
                </Button>
                <Button
                  type="submit"
                  className="w-32 h-11 bg-[#0D4F97] hover:bg-[#0D4F97]/90 rounded-lg"
                  disabled={form.formState.isSubmitting}
                >
                  {form.formState.isSubmitting ? "Gravando..." : "Cadastrar"}
                </Button>
              </div>
            </form>
          </Form>
        </div>
      </div>
    </div>
  );
}

export default Page;
