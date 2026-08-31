"use client";

import React from "react";
import Link from "next/link";
import { useSearchParams } from "next/navigation";
import { useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";

import {
  Form,
  FormField,
  FormItem,
  FormLabel,
  FormControl,
  FormMessage,
} from "@/components/ui/form";

import { PasswordInput } from "@/components/forms/PasswordInputs";
import { PrimaryButton } from "@/components/buttons/ButtonPrimary";

import { Card, CardContent, CardFooter, CardHeader } from "@/components/ui/card";

import { newPasswordSchema, FormNewPasswordSchema } from "../authSchema";

import { useResetPassword } from "./use-reset-password";

export function ResetPasswordForm() {
  const token = useSearchParams().get("token") ?? "";

  const { submit } = useResetPassword(token);

  const form = useForm<FormNewPasswordSchema>({
    resolver: zodResolver(newPasswordSchema),
    defaultValues: {
      password: "",
      confirmPassword: "",
    },
    mode: "all",
  });

  return (
    <Form {...form}>
      <form onSubmit={form.handleSubmit(submit)}>
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
              name="password"
              render={({ field }) => (
                <FormItem>
                  <FormLabel className="font-bold text-gray-700">Nova senha</FormLabel>

                  <FormControl>
                    <PasswordInput
                      {...field}
                      placeholder="Digite a senha"
                      className="h-12 border-gray-300"
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
                  <FormLabel className="font-bold text-gray-700">Confirmar senha</FormLabel>

                  <FormControl>
                    <PasswordInput
                      {...field}
                      placeholder="Confirme sua senha"
                      className="h-12 border-gray-300"
                    />
                  </FormControl>

                  <FormMessage />
                </FormItem>
              )}
            />
          </CardContent>

          <CardFooter className="w-full flex flex-col items-center">
            <PrimaryButton type="submit" className="w-full py-6 text-lg">
              Enviar
            </PrimaryButton>

            <Link href="/auth/login" className="mt-4 text-[#F2994A] font-bold underline text-sm">
              Voltar para tela de login.
            </Link>
          </CardFooter>
        </Card>
      </form>
    </Form>
  );
}
