"use client";

import React, { useEffect, useState } from "react";
import CreatableSelect from "react-select/creatable";
import { MultiValue, StylesConfig } from "react-select";

interface Option {
  label: string;
  value: string;
  id?: string;
}

interface GenericDatabaseSelectProps {
  value: any[]; 
  onChange: (value: any[]) => void;
  endpoint: string; // EX: "/api/vacinas" ou "/api/transtornos"
  placeholder?: string;
  labelSingular: string; // EX: "Vacina" ou "Transtorno"
}

const capitalize = (s: string) => s.charAt(0).toUpperCase() + s.slice(1).toLowerCase();

export const GenericDatabaseSelect = ({ value, onChange, endpoint, placeholder, labelSingular }: GenericDatabaseSelectProps) => {
  const [options, setOptions] = useState<Option[]>([]);
  const [isLoading, setIsLoading] = useState(false);

  useEffect(() => {
    const fetchData = async () => {
      try {
        const res = await fetch(endpoint);
        if (res.ok) {
            const data = await res.json();
            const safeData = Array.isArray(data) ? data : [];
            setOptions(safeData.map((d: any) => ({ label: d.name, value: d.name, id: d.id })));
        }
      } catch (error) { console.error(error); }
    };
    fetchData();
  }, [endpoint]);

  const getCurrentValue = (): Option[] => {
    if (!value || !Array.isArray(value)) return [];
    return value.map((v) => ({ 
        label: v.name || v.label || v.value, 
        value: v.name || v.label || v.value,
        id: v.id 
    }));
  };

  const handleChange = (newValue: MultiValue<Option>) => {
    onChange(newValue.map(v => ({ name: v.value, id: v.id })));
  };

  const handleCreate = async (inputValue: string) => {
    const normalizedName = capitalize(inputValue.trim());
    const exists = options.some(opt => opt.label.toLowerCase() === normalizedName.toLowerCase());
    
    if (exists) {
        const existing = options.find(opt => opt.label.toLowerCase() === normalizedName.toLowerCase());
        if (existing) handleChange([...getCurrentValue(), existing] as MultiValue<Option>);
        return;
    }

    setIsLoading(true);
    try {
        const res = await fetch(endpoint, {
            method: "POST",
            body: JSON.stringify({ name: normalizedName })
        });
        
        let newOption = { label: normalizedName, value: normalizedName, id: undefined };
        if (res.ok || res.status === 201 || res.status === 409) { // 409 aceita se ja existir
            const created = await res.json().catch(() => ({}));
            if (created && created.id) newOption.id = created.id;
        }

        setOptions((prev) => [...prev, newOption]);
        handleChange([...getCurrentValue(), newOption] as MultiValue<Option>);
    } catch (error) { console.error(error); } 
    finally { setIsLoading(false); }
  };

  const customStyles: StylesConfig<Option, true> = {
    control: (base, state) => ({
      ...base,
      borderColor: "#e2e8f0",
      borderRadius: "0.375rem",
      fontSize: "0.875rem",
      minHeight: "2.5rem",
      boxShadow: "none",
      backgroundColor: "white",
      "&:hover": { borderColor: "#cbd5e1" },
      ...(state.isFocused && { borderColor: "black", borderWidth: "1px", outline: "1px solid black" })
    }),
    multiValue: (base) => ({ ...base, backgroundColor: "#eff6ff", borderRadius: "0.25rem" }),
    multiValueLabel: (base) => ({ ...base, color: "#0D4F97", fontWeight: 600, fontSize: "0.75rem" }),
    multiValueRemove: (base) => ({ ...base, color: "#0D4F97", ":hover": { backgroundColor: "#dbeafe", color: "#1e3a8a" } })
  };

  return (
    <CreatableSelect
      isMulti
      isDisabled={isLoading}
      isLoading={isLoading}
      onChange={handleChange}
      onCreateOption={handleCreate}
      options={options}
      value={getCurrentValue()}
      styles={customStyles}
      placeholder={placeholder}
      formatCreateLabel={(inputValue) => `Criar ${labelSingular} "${capitalize(inputValue)}"`}
      noOptionsMessage={() => `Nenhum(a) ${labelSingular.toLowerCase()} encontrado(a)`}
      createOptionPosition="first"
      blurInputOnSelect={false}
    />
  );
};