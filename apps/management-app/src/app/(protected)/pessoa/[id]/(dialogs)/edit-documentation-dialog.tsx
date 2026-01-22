import { Button } from "@/components/ui/button";
import {
  Dialog,
  DialogContent,
  DialogFooter,
  DialogHeader,
  DialogTitle,
} from "@/components/ui/dialog";
import {
  FormField,
  FormItem,
  FormLabel,
  FormControl,
  FormMessage,
  Form,
} from "@/components/ui/form";

import { useForm } from "react-hook-form";
import { DialogProps } from "./dialog-types";

import { zodResolver } from "@hookform/resolvers/zod";
import { EditDocumentation } from "@/schemas/member-schemas";
import z from "zod";
import { Input } from "@/components/ui/input";
import {
  formatBirthCertificate,
  formatCNS,
  formatCPF,
  formatIssuingBody,
  formatRG,
} from "@/lib/formats";

export function EditDocumentationDialog({
  open,
  member,
  onOpenChange,
}: DialogProps<z.infer<typeof EditDocumentation>>) {
  const form = useForm<z.input<typeof EditDocumentation>>({
    resolver: zodResolver(EditDocumentation),
    mode: "onBlur",
    defaultValues: {
      rg: {
        issuing: {
          body: member?.issuingAgency ?? "",
          date: new Date(member?.issueDate),
        },
        number: member?.rg ?? "",
      },
      cpf: member?.cpf ?? "",
      birthCertificate: member?.birthCertificateNumber ?? "",
      cns: member?.cns ?? "",
      nis: member?.nis ?? "",
    },
  });

  const onSubmit = async (values: z.input<typeof EditDocumentation>) => {
    const parseBirthCertificate = (certificate: string) => {
      const [
        cartorio,
        acervo,
        servicoRegistroCivil,
        ano,
        tipo,
        livro,
        folha,
        termo,
        digitoVerificador,
      ] = certificate.split(" ");

      return {
        cartorio,
        acervo,
        servicoRegistroCivil,
        ano,
        tipo,
        livro,
        folha,
        termo,
        digitoVerificador,
      };
    };

    const {
      cartorio: registryOffice,
      livro: book,
      folha: fls,
    } = parseBirthCertificate(values.birthCertificate);
    const response = await fetch(`/api/pessoas/${member.id}`, {
      method: "PUT",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({
        ...member,
        nationality: member.birthplace,

        issuingAgency: values.rg.issuing.body,
        issueDate: values.rg.issuing.date.toISOString().split("T")[0],
        rg: values.rg.number,
        cpf: values.cpf,
        birthCertificateNumber: values.birthCertificate,
        cns: values.cns,
        nis: values.nis,
        registryOffice,
        book,
        fls,
      }),
    });

    if (!response.ok) {
      throw new Error("Ocorreu um erro ao atualizar pessoa.");
    }

    onOpenChange(false);
  };

  return (
    <Dialog open={open} onOpenChange={onOpenChange}>
      <DialogContent className="sm:max-w-[600px]">
        <DialogHeader>
          <DialogTitle className="text-[#0D4F97]">
            Editar Documentação
          </DialogTitle>
        </DialogHeader>

        <Form {...form}>
          <form onSubmit={form.handleSubmit(onSubmit)} className="space-y-4">
            <FormField
              control={form.control}
              name="cpf"
              render={({ field }) => (
                <FormItem>
                  <FormLabel>CPF *</FormLabel>
                  <FormControl>
                    <Input
                      placeholder="000.000.000-00"
                      maxLength={14}
                      value={field.value}
                      onChange={(e) => {
                        const formatted = formatCPF(e.target.value);
                        field.onChange(formatted);
                      }}
                    />
                  </FormControl>
                  <FormMessage />
                </FormItem>
              )}
            />

            <FormField
              control={form.control}
              name="cns"
              render={({ field }) => (
                <FormItem>
                  <FormLabel>CNS *</FormLabel>
                  <FormControl>
                    <Input
                      placeholder="Apenas números"
                      maxLength={18}
                      value={field.value}
                      onChange={(e) => {
                        const formatted = formatCNS(e.target.value);
                        field.onChange(formatted);
                      }}
                    />
                  </FormControl>
                  <FormMessage />
                </FormItem>
              )}
            />

            <FormField
              control={form.control}
              name="nis"
              render={({ field }) => (
                <FormItem>
                  <FormLabel>NIS *</FormLabel>
                  <FormControl>
                    <Input placeholder="Digite o NIS" {...field} />
                  </FormControl>
                  <FormMessage />
                </FormItem>
              )}
            />

            <FormField
              control={form.control}
              name="rg.number"
              render={({ field }) => (
                <FormItem>
                  <FormLabel>RG *</FormLabel>
                  <FormControl>
                    <Input
                      placeholder="1.234.567"
                      maxLength={9}
                      value={field.value}
                      onChange={(e) => {
                        const formatted = formatRG(e.target.value);
                        field.onChange(formatted);
                      }}
                    />
                  </FormControl>
                  <FormMessage />
                </FormItem>
              )}
            />

            <FormField
              control={form.control}
              name="rg.issuing.body"
              render={({ field }) => (
                <FormItem>
                  <FormLabel>Órgão Emissor *</FormLabel>
                  <FormControl>
                    <Input
                      placeholder="SSP/SP"
                      maxLength={7}
                      value={field.value}
                      onChange={(e) => {
                        const formatted = formatIssuingBody(e.target.value);
                        field.onChange(formatted);
                      }}
                    />
                  </FormControl>
                  <FormMessage />
                </FormItem>
              )}
            />

            <FormField
              control={form.control}
              name="rg.issuing.date"
              render={({ field }) => (
                <FormItem>
                  <FormLabel>Data de Emissão *</FormLabel>
                  <FormControl>
                    <Input
                      type="date"
                      {...field}
                      value={
                        field.value instanceof Date &&
                        !Number.isNaN(field.value.getTime())
                          ? field.value.toISOString().split("T")[0]
                          : ""
                      }
                      onChange={(e) => field.onChange(new Date(e.target.value))}
                    />
                  </FormControl>
                  <FormMessage />
                </FormItem>
              )}
            />

            <FormField
              control={form.control}
              name="birthCertificate"
              render={({ field }) => (
                <FormItem>
                  <FormLabel>Certidão de Nascimento *</FormLabel>
                  <FormControl>
                    <Input
                      placeholder="Apenas números"
                      value={field.value}
                      onChange={(e) => {
                        const formatted = formatBirthCertificate(
                          e.target.value,
                        );
                        field.onChange(formatted);
                      }}
                    />
                  </FormControl>
                  <FormMessage />
                </FormItem>
              )}
            />

            <DialogFooter className="pt-6">
              <Button
                className="text-[#0D4F97]"
                variant="outline"
                onClick={() => onOpenChange(false)}
                type="button"
              >
                Cancelar
              </Button>
              <Button
                type="submit"
                className="text-white !bg-[#0D4F97] !hover:bg-[#0b427d]"
              >
                Salvar Alterações
              </Button>
            </DialogFooter>
          </form>
        </Form>
      </DialogContent>
    </Dialog>
  );
}
