"use client";

import React, { useEffect, useState } from "react";
import CreatableSelect from "react-select/creatable";
import { MultiValue, StylesConfig } from "react-select";

interface DisorderOption {
  label: string;
  value: string;
  id?: string;
}

interface DisorderMultiSelectProps {
  value: any[]; 
  onChange: (value: any[]) => void;
}

const capitalize = (s: string) => s.charAt(0).toUpperCase() + s.slice(1).toLowerCase();

export const DisorderMultiSelect = ({ value, onChange }: DisorderMultiSelectProps) => {
  const [options, setOptions] = useState<DisorderOption[]>([]);
  const [isLoading, setIsLoading] = useState(false);

  useEffect(() => {
    const fetchDisorders = async () => {
      try {
        const res = await fetch("/api/transtornos");
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
    fetchDisorders();
  }, []);

  const getCurrentValue = (): DisorderOption[] => {
    if (!value || !Array.isArray(value)) return [];
    return value.map((v) => ({ 
        label: v.name || v.label || v.value || v, 
        value: v.name || v.label || v.value || v,
        id: v.id 
    }));
  };

  const handleChange = (newValue: MultiValue<DisorderOption>) => {
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
            handleChange([...getCurrentValue(), existingOption] as MultiValue<DisorderOption>);
        }
        return;
    }

    setIsLoading(true);
    try {
        const res = await fetch("/api/transtornos", {
            method: "POST",
            body: JSON.stringify({ name: normalizedName })
        });
        
        let newOption = { label: normalizedName, value: normalizedName, id: undefined };
        
        if (res.ok || res.status === 201) {
            const createdDisorder = await res.json();
            if (createdDisorder && createdDisorder.id) {
                newOption = { 
                    label: createdDisorder.name, 
                    value: createdDisorder.name, 
                    id: createdDisorder.id 
                };
            }
        }

        setOptions((prev) => [...prev, newOption]);
        
        const current = getCurrentValue();
        handleChange([...current, newOption] as MultiValue<DisorderOption>);

    } catch (error) { console.error(error); } 
    finally { setIsLoading(false); }
  };

  const customStyles: StylesConfig<DisorderOption, true> = {
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
      placeholder="Selecione ou crie transtornos..."
      formatCreateLabel={(inputValue) => `Criar "${capitalize(inputValue)}"`}
      noOptionsMessage={() => "Nenhum transtorno encontrado"}
      createOptionPosition="first"
      blurInputOnSelect={false}
    />
  );
};