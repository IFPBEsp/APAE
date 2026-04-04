"use client";

import React, { useRef, useState } from "react";
import { useForm } from "react-hook-form";

import { Button } from "@/components/ui/button";
import { Card, CardContent } from "@/components/ui/card";
import {
  Form,
  FormField,
  FormItem,
  FormLabel,
  FormControl,
  FormMessage,
} from "@/components/ui/form";
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from "@/components/ui/select";
import { Input } from "@/components/ui/input";
import { Textarea } from "@/components/ui/textarea";

import { toast } from "react-toastify";

import { DocumentWithOutUrl } from "@/types/document";

interface AbsenceFormProps {
  generatedAppointmentId: string;
  patientId: string;
  absenceDate: string;
  onSuccess?: () => void;
}

type FormDataType = {
  hasJustification: string;
  justificationText?: string;
};

export function AbsenceForm({
  generatedAppointmentId,
  patientId,
  absenceDate,
  onSuccess,
}: AbsenceFormProps) {

  const form = useForm<FormDataType>({
    defaultValues: {
      hasJustification: "",
      justificationText: "",
    },
  });

  const hasJustification = form.watch("hasJustification");

  const [file, setFile] = useState<File | null>(null);
  const fileInputRef = useRef<HTMLInputElement | null>(null);

  const onSubmit = async (data: FormDataType) => {
    try {
      let documentId: string | null = null;

      if (data.hasJustification === "yes" && file) {
        const docFormData = new FormData();

        docFormData.append("file", file);
        docFormData.append("category", "ABSENCE");
        docFormData.append("type", "ATTACHMENTANY");
        docFormData.append("year", String(new Date().getFullYear()));

        const docResponse = await fetch(`/api/pessoas/${patientId}/documentos`, {
          method: "POST",
          body: docFormData,
        });

        if (!docResponse.ok) {
          const errorData = await docResponse.json();
          throw new Error(errorData.message);
        }

        const document = (await docResponse.json()) as DocumentWithOutUrl;

        documentId = document.id;
        
      }

      const absencePayload = {
        generatedAppointmentId,
        absenceDate,
        isJustified: data.hasJustification === "yes",
        justification:
          data.hasJustification === "yes"
            ? data.justificationText
            : false,
        justificationDocumentId: documentId,
      };

      const response = await fetch("/api/absence", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify(absencePayload),
      });

      if (!response.ok) {
        const errorData = await response.json();
        throw new Error(errorData.message);
      }

      toast.success("Falta registrada com sucesso!");

      form.reset();
      setFile(null);

      if (fileInputRef.current) {
        fileInputRef.current.value = "";
      }

      window.location.reload();

    } catch (error: any) {
      toast.error(error.message || "Erro ao registrar falta");
    }
  };

  return (
    <Card className="w-full mx-auto border-none shadow-none">
      <Form {...form}>
        <form onSubmit={form.handleSubmit(onSubmit)}>
          <CardContent className="space-y-6">

            {/* Select justificativa */}
            <FormField
              control={form.control}
              name="hasJustification"
              rules={{ required: "Obrigatório" }}
              render={({ field }) => (
                <FormItem>
                  <FormLabel>
                    Possui justificativa?{" "}
                    <span className="text-red-500">*</span>
                  </FormLabel>

                  <FormControl>
                    <Select
                      onValueChange={field.onChange}
                      value={field.value}
                    >
                      <SelectTrigger>
                        <SelectValue placeholder="Selecione uma opção" />
                      </SelectTrigger>

                      <SelectContent>
                        <SelectItem value="yes">
                          Com justificativa
                        </SelectItem>
                        <SelectItem value="no">
                          Sem justificativa
                        </SelectItem>
                      </SelectContent>
                    </Select>
                  </FormControl>

                  <FormMessage />
                </FormItem>
              )}
            />

            {hasJustification === "yes" && (
              <div className="space-y-4">

                <FormField
                  control={form.control}
                  name="justificationText"
                  rules={{
                    validate: (value) => {
                      if (
                        form.getValues("hasJustification") === "yes" &&
                        !value?.trim()
                      ) {
                        return "Obrigatório";
                      }
                      return true;
                    },
                  }}
                  render={({ field }) => (
                    <FormItem>
                      <FormLabel>Descrição da justificativa</FormLabel>

                      <FormControl>
                        <Textarea
                          placeholder="Descreva a justificativa"
                          {...field}
                        />
                      </FormControl>

                      <FormMessage />
                    </FormItem>
                  )}
                />

                <div className="space-y-2">
                  <FormLabel>Anexar documento</FormLabel>

                  <Input
                    ref={fileInputRef}
                    type="file"
                    onChange={(e) => {
                      if (e.target.files?.[0]) {
                        setFile(e.target.files[0]);
                      }
                    }}
                  />

                  {file && (
                    <div className="flex items-center justify-between text-sm">
                      <span className="text-muted-foreground">
                        {file.name}
                      </span>

                      <Button
                        type="button"
                        variant="outline"
                        size="sm"
                        className="text-red-500 hover:text-red-600"
                        onClick={() => {
                          setFile(null);

                          if (fileInputRef.current) {
                            fileInputRef.current.value = "";
                          }
                        }}
                      >
                        Remover
                      </Button>
                    </div>
                  )}
                </div>

              </div>
            )}

            <div className="flex justify-end pt-4">
              <Button className="w-full bg-[#0D4F97] text-white hover:bg-blue-900 sm:w-auto">
                Registrar Falta
              </Button>
            </div>

          </CardContent>
        </form>
      </Form>
    </Card>
  );
}