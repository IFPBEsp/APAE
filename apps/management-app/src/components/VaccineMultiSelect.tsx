"use client";

import React, { useEffect, useState } from "react";
import CreatableSelect from "react-select/creatable";
import { MultiValue, StylesConfig } from "react-select";

interface VaccineOption {
  label: string;
  value: string;
  id?: string;
}

interface VaccineMultiSelectProps {
  value: any[]; 
  onChange: (value: any[]) => void;
}

const capitalize = (s: string) => s.charAt(0).toUpperCase() + s.slice(1).toLowerCase();

export const VaccineMultiSelect = ({ value, onChange }: VaccineMultiSelectProps) => {
  const [options, setOptions] = useState<VaccineOption[]>([]);
  const [isLoading, setIsLoading] = useState(false);

  useEffect(() => {
    const fetchVaccines = async () => {
      try {
        const res = await fetch("/api/vacinas");
        if (res.ok) {
            const data = await res.json();
            const safeData = Array.isArray(data) ? data : [];
            const formatted = safeData.map((d: any) => ({ 
                label: d.name, 
                value: d.name, 
                id: d.id 
            }));
            setOptions(formatted);
        }
      } catch (error) { console.error(error); }
    };
    fetchVaccines();
  }, []);

  const getCurrentValue = (): VaccineOption[] => {
    if (!value || !Array.isArray(value)) return [];
    return value.map((v) => ({ 
        label: v.name || v, 
        value: v.name || v,
        id: v.id 
    }));
  };

  const handleChange = (newValue: MultiValue<VaccineOption>) => {
    const formattedForForm = newValue.map(v => ({
        name: v.value,
        id: v.id
    }));
    onChange(formattedForForm);
  };

  const handleCreate = async (inputValue: string) => {
    const normalizedName = capitalize(inputValue.trim());
    const exists = options.some(opt => opt.label.toLowerCase() === normalizedName.toLowerCase());
    if (exists) {
        const existingOption = options.find(opt => opt.label.toLowerCase() === normalizedName.toLowerCase());
        if (existingOption) {
            handleChange([...getCurrentValue(), existingOption] as MultiValue<VaccineOption>);
        }
        return;
    }

    setIsLoading(true);
    try {
        const res = await fetch("/api/vacinas", {
            method: "POST",
            body: JSON.stringify({ name: normalizedName })
        });
    
        let newOption = { label: normalizedName, value: normalizedName, id: undefined };
        
        if (res.ok || res.status === 201) {
            const newVaccine = await res.json();
            if (newVaccine && newVaccine.id) {
                newOption = { label: newVaccine.name, value: newVaccine.name, id: newVaccine.id };
            }
        }

        setOptions((prev) => [...prev, newOption]);
        
        const current = getCurrentValue();
        handleChange([...current, newOption] as MultiValue<VaccineOption>);

    } catch (error) { console.error(error); } 
    finally { setIsLoading(false); }
  };

  const customStyles: StylesConfig<VaccineOption, true> = {
    control: (base, state) => ({
      ...base,
      borderColor: "#e2e8f0",
      borderRadius: "0.375rem",
      fontSize: "0.875rem",
      minHeight: "2.5rem",
      boxShadow: "none",
      backgroundColor: "white",
      "&:hover": { borderColor: "#cbd5e1" },
      ...(state.isFocused && {
        borderColor: "black", 
        borderWidth: "1px",
        outline: "1px solid black"
      })
    }),
    placeholder: (base) => ({
        ...base,
        color: "#64748b",
        fontSize: "0.875rem",
    }),
    menu: (base) => ({
        ...base,
        borderRadius: "0.375rem",
        border: "1px solid #e2e8f0",
        boxShadow: "0 4px 6px -1px rgb(0 0 0 / 0.1)",
        zIndex: 9999
    }),
    multiValue: (base) => ({
      ...base,
      backgroundColor: "#eff6ff",
      borderRadius: "0.25rem",
    }),
    multiValueLabel: (base) => ({
      ...base,
      color: "#0D4F97",
      fontWeight: 600,
      fontSize: "0.75rem",
    }),
    multiValueRemove: (base) => ({
        ...base,
        color: "#0D4F97",
        ":hover": { backgroundColor: "#dbeafe", color: "#1e3a8a" },
    })
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
      placeholder="Selecione ou crie vacinas..."
      formatCreateLabel={(inputValue) => `Criar "${capitalize(inputValue)}"`}
      noOptionsMessage={() => "Nenhuma vacina encontrada"}
      createOptionPosition="first"
    />
  );
};