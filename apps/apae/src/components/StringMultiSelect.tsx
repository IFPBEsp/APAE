"use client";

import React, { useState, useEffect } from "react";
import CreatableSelect from "react-select/creatable";
import { MultiValue, StylesConfig } from "react-select";

interface Option {
  label: string;
  value: string;
}

interface StringMultiSelectProps {
  value: string | null | undefined;
  onChange: (value: string) => void;
  placeholder?: string;
}

const capitalize = (s: string) => s.charAt(0).toUpperCase() + s.slice(1).toLowerCase();

export const StringMultiSelect = ({ value, onChange, placeholder }: StringMultiSelectProps) => {
  const [inputValue, setInputValue] = useState("");
  const getSelectValue = (): Option[] => {
    if (!value) return [];
    return value.split(",").map((item) => {
      const trimmed = item.trim();
      if (!trimmed) return null;
      return { label: trimmed, value: trimmed };
    }).filter((item): item is Option => item !== null);
  };

  const handleChange = (newValue: MultiValue<Option>) => {
    const uniqueValues = Array.from(new Set(newValue.map(opt => opt.value)));
    onChange(uniqueValues.join(", "));
  };

  const handleCreate = (inputValue: string) => {
    const normalized = capitalize(inputValue.trim());
    if (!normalized) return;

    const currentOptions = getSelectValue();
    const exists = currentOptions.some(opt => opt.value === normalized);
    
    if (!exists) {
      const newValues = [...currentOptions.map(o => o.value), normalized];
      onChange(newValues.join(", "));
    }
    setInputValue("");
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
      inputValue={inputValue}
      onInputChange={(val) => setInputValue(val)}
      placeholder={placeholder || "Digite e aperte Enter..."}
      value={getSelectValue()}
      onChange={handleChange}
      onCreateOption={handleCreate}
      styles={customStyles}
      formatCreateLabel={(inputValue) => `Adicionar "${capitalize(inputValue)}"`}
      noOptionsMessage={() => "Digite para adicionar..."}
      createOptionPosition="first"
      blurInputOnSelect={false}
    />
  );
};