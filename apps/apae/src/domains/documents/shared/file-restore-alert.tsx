import { AlertCircle } from "lucide-react";

export function FileRestoreAlert() {
  return (
    <div className="flex gap-3 p-4 mb-6 border-l-4 rounded-r-md bg-amber-50 border-amber-400 animate-in fade-in slide-in-from-top-2">
      <AlertCircle className="flex-shrink-0 w-5 h-5 text-amber-600 mt-0.5" />
      <div>
        <h4 className="text-sm font-bold text-amber-900">Rascunho recuperado</h4>
        <p className="mt-1 text-xs leading-relaxed text-amber-800">
          Seus dados de texto foram restaurados, mas por segurança,
          <strong> arquivos e laudos precisam ser selecionados novamente.</strong>
        </p>
      </div>
    </div>
  );
}
