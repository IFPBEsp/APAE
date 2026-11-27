import { describe, it, expect } from "vitest";
import { MAX_FILE_SIZE_BYTES } from "@/lib/constants";
import {
  ALLOWED_DOCUMENT_TYPES,
  documentFileSchema,
  optionalDocumentFileSchema,
  validateDocumentFile,
} from "./document.schema";

const makeFile = (name: string, type: string, size = 1024) =>
  new File([new Uint8Array(size)], name, { type });

describe("documentFileSchema", () => {
  describe("formatos válidos", () => {
    it.each([
      ["PDF", "laudo.pdf", "application/pdf"],
      [
        "DOCX",
        "relatorio.docx",
        "application/vnd.openxmlformats-officedocument.wordprocessingml.document",
      ],
      ["PNG", "exame.png", "image/png"],
      ["JPEG", "exame.jpeg", "image/jpeg"],
      ["JPG", "exame.jpg", "image/jpg"],
      ["WEBP", "exame.webp", "image/webp"],
    ])("aceita arquivo %s válido (%s)", (_, name, type) => {
      const file = makeFile(name, type);
      const result = documentFileSchema.safeParse(file);

      expect(result.success).toBe(true);
      expect(validateDocumentFile(file)).toBeNull();
    });

    it("lista exatamente os mesmos 6 content-types aceitos pelo backend em ValidFileFormat.java", () => {
      expect(ALLOWED_DOCUMENT_TYPES).toEqual([
        "application/pdf",
        "application/vnd.openxmlformats-officedocument.wordprocessingml.document",
        "image/png",
        "image/jpeg",
        "image/jpg",
        "image/webp",
      ]);
    });
  });

  describe("arquivo ausente ou vazio", () => {
    it.each([undefined, null, "nao-e-arquivo"])(
      "rejeita valor que não é File (%s) com 'Selecione um arquivo'",
      (value) => {
        const result = documentFileSchema.safeParse(value);

        expect(result.success).toBe(false);
        if (!result.success) {
          expect(result.error.issues[0].message).toBe("Selecione um arquivo");
        }
      },
    );

    it("rejeita arquivo vazio (0 bytes) com 'O arquivo está vazio'", () => {
      const emptyFile = makeFile("vazio.pdf", "application/pdf", 0);
      const result = documentFileSchema.safeParse(emptyFile);

      expect(result.success).toBe(false);
      if (!result.success) {
        expect(result.error.issues[0].message).toBe("O arquivo está vazio");
      }
      expect(validateDocumentFile(emptyFile)).toBe("O arquivo está vazio");
    });
  });

  describe("formato inválido", () => {
    it.each([
      ["TXT", "notas.txt", "text/plain"],
      ["GIF", "animacao.gif", "image/gif"],
      [
        "XLSX",
        "planilha.xlsx",
        "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
      ],
      ["HTML", "pagina.html", "text/html"],
      ["EXE", "programa.exe", "application/x-msdownload"],
    ])("rejeita formato não aceito (%s)", (_, name, type) => {
      const invalidFile = makeFile(name, type);
      const result = documentFileSchema.safeParse(invalidFile);

      expect(result.success).toBe(false);
      if (!result.success) {
        expect(result.error.issues[0].message).toBe(
          "Formato inválido. Use PDF, DOCX, PNG, JPG ou WEBP",
        );
      }
      expect(validateDocumentFile(invalidFile)).toBe(
        "Formato inválido. Use PDF, DOCX, PNG, JPG ou WEBP",
      );
    });
  });

  describe("limite de tamanho (10MB)", () => {
    it("aceita arquivo exatamente no limite de 10MB (MAX_FILE_SIZE_BYTES)", () => {
      const exactLimitFile = makeFile(
        "limite.pdf",
        "application/pdf",
        MAX_FILE_SIZE_BYTES,
      );
      const result = documentFileSchema.safeParse(exactLimitFile);

      expect(result.success).toBe(true);
      expect(validateDocumentFile(exactLimitFile)).toBeNull();
    });

    it("rejeita arquivo acima do limite de 10MB (MAX_FILE_SIZE_BYTES + 1)", () => {
      const oversizedFile = makeFile(
        "grande.pdf",
        "application/pdf",
        MAX_FILE_SIZE_BYTES + 1,
      );
      const result = documentFileSchema.safeParse(oversizedFile);

      expect(result.success).toBe(false);
      if (!result.success) {
        expect(result.error.issues[0].message).toBe(
          "O arquivo excede o limite máximo permitido de 10MB",
        );
      }
      expect(validateDocumentFile(oversizedFile)).toBe(
        "O arquivo excede o limite máximo permitido de 10MB",
      );
    });
  });

  describe("optionalDocumentFileSchema", () => {
    it("aceita undefined, null ou um File válido", () => {
      expect(optionalDocumentFileSchema.safeParse(undefined).success).toBe(true);
      expect(optionalDocumentFileSchema.safeParse(null).success).toBe(true);
      expect(
        optionalDocumentFileSchema.safeParse(
          makeFile("laudo.pdf", "application/pdf"),
        ).success,
      ).toBe(true);
    });
  });
});
