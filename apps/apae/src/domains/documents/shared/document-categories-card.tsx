'use client';
import React from 'react';
import { User, ClipboardPlus, LibraryBigIcon } from 'lucide-react';

interface Category {
  label: string;
  icon: React.ReactNode;
  type: string;
}

interface DocumentCategoriesCardProps {
  onClickCategory: (type: string) => void;
  categories?: Category[];
}

const DocumentCategoriesCard: React.FC<DocumentCategoriesCardProps> = ({
  onClickCategory,
  categories = [
    { label: 'Pessoais', icon: <User />, type: 'personals' },
    { label: 'Médicos', icon: <ClipboardPlus />, type: 'medicals' },
    { label: 'Escolares', icon: <LibraryBigIcon />, type: 'schools' },
  ],
}) => {
  return (
    <div className="rounded-4xl bg-[#0D4F97] p-6 text-white w-full mx-auto">
      <h2 className="text-center text-2xl font-semibold mb-4">Documentos</h2>

      <div className="flex justify-around gap-2 flex-wrap">
        {categories.map((category) => (
          <div
            key={category.type}
            onClick={() => onClickCategory(category.type)}
            className="flex flex-col items-center justify-center cursor-pointer border-2 border-white px-4 py-3 rounded-lg hover:bg-white hover:text-[#003366] transition-colors duration-200"
          >
            {category.icon}
            <span className="mt-2 text-sm font-medium">{category.label}</span>
          </div>
        ))}
      </div>
    </div>
  );
};

export default DocumentCategoriesCard;