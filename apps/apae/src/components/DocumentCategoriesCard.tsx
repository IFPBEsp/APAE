'use client';
import React from 'react';
import { User, ClipboardPlus, LibraryBigIcon } from 'lucide-react';

interface Categoria {
  label: string;
  icon: React.ReactNode;
  tipo: string;
}

interface DocumentCategoriesCardProps {
  onClickCategoria: (tipo: string) => void;
  categorias?: Categoria[];
}

const DocumentCategoriesCard: React.FC<DocumentCategoriesCardProps> = ({
  onClickCategoria,
  categorias = [
    { label: 'Pessoais', icon: <User />, tipo: 'pessoais' },
    { label: 'Médicos', icon: <ClipboardPlus />, tipo: 'medicos' },
    { label: 'Escolares', icon: <LibraryBigIcon />, tipo: 'escolares' },
  ],
}) => {
  return (
    <div className="rounded-4xl bg-[#0D4F97] p-6 text-white w-full mx-auto">
      <h2 className="text-center text-2xl font-semibold mb-4">Documentos</h2>

      <div className="flex justify-around gap-2 flex-wrap">
        {categorias.map((categoria) => (
          <div
            key={categoria.tipo}
            onClick={() => onClickCategoria(categoria.tipo)}
            className="flex flex-col items-center justify-center cursor-pointer border-2 border-white px-4 py-3 rounded-lg hover:bg-white hover:text-[#003366] transition-colors duration-200"
          >
            {categoria.icon}
            <span className="mt-2 text-sm font-medium">{categoria.label}</span>
          </div>
        ))}
      </div>
    </div>
  );
};

export default DocumentCategoriesCard;