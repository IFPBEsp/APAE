"use client";

import React, { useEffect, useState } from "react";
import CreatableSelect from "react-select/creatable";
import { MultiValue, StylesConfig } from "react-select";

interface Option {
  label: string;
  value: string;
  id?: string | number;
  [key: string]: any;
}

interface GenericDatabaseSelectProps {
  value: any[];
  onChange: (value: any[]) => void;
  endpoint: string;
  placeholder?: string;
  labelSingular: string;
  labelKey?: string;
  menuPlacement?: "auto" | "bottom" | "top";
}

const capitalize = (s: string) => s.charAt(0).toUpperCase() + s.slice(1).toLowerCase();

export const GenericDatabaseSelect = ({
  value,
  onChange,
  endpoint,
  placeholder,
  labelSingular,
  labelKey = "name",
  menuPlacement = "auto",
}: GenericDatabaseSelectProps) => {
  const [options, setOptions] = useState<Option[]>([]);
  const [isLoading, setIsLoading] = useState(false);

  useEffect(() => {
    const fetchData = async () => {
      try {
        const res = await fetch(endpoint);
        if (res.ok) {
          const data = await res.json();
          const safeData = Array.isArray(data) ? data : [];
          setOptions(
            safeData.map((d: any) => ({
              label: d[labelKey] || d.name || "Sem nome",
              value: d[labelKey] || d.name || "Sem nome",
              id: d.id,
            })),
          );
        }
      } catch (error) {
        console.error(error);
      }
    };
    fetchData();
  }, [endpoint, labelKey]);

  const getCurrentValue = (): Option[] => {
    if (!value || !Array.isArray(value)) return [];
    return value
      .map((v) => {
        const text = v[labelKey] || v.name || v.label || v.value;
        const existingOption = options.find((opt) => opt.id === v.id);
        if (existingOption) return existingOption;
        return { label: text, value: text, id: v.id };
      })
      .filter((v) => v.label);
  };

  const handleChange = (newValue: MultiValue<Option>) => {
    const formatted = newValue.map((v) => ({
      [labelKey]: v.value,
      name: v.value,
      id: v.id,
    }));
    onChange(formatted);
  };

  const handleCreate = async (inputValue: string) => {
    const normalizedName = capitalize(inputValue.trim());
    const existingOption = options.find(
      (opt) => opt.label.toLowerCase() === normalizedName.toLowerCase(),
    );

    if (existingOption) {
      const currentSelected = getCurrentValue();
      if (!currentSelected.some((s) => s.value === existingOption.value)) {
        handleChange([...currentSelected, existingOption] as MultiValue<Option>);
      }
      return;
    }

    setIsLoading(true);
    try {
      const payload = { [labelKey]: normalizedName };
      const res = await fetch(endpoint, { method: "POST", body: JSON.stringify(payload) });

      let newOption: Option = { label: normalizedName, value: normalizedName, id: undefined };

      if (res.ok || res.status === 201 || res.status === 409) {
        const created = await res.json().catch(() => ({}));
        if (created && created.id) newOption.id = created.id;
      }

      setOptions((prev) => [...prev, newOption]);
      const currentSelected = getCurrentValue();
      const newSelection = [...currentSelected, newOption] as MultiValue<Option>;

      const formatted = newSelection.map((v) => ({
        [labelKey]: v.value,
        name: v.value,
        id: v.id,
      }));

      onChange(formatted);
    } catch (error) {
      console.error(error);
    } finally {
      setIsLoading(false);
    }
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
        outline: "1px solid black",
      }),
    }),
    multiValue: (base) => ({ ...base, backgroundColor: "#eff6ff", borderRadius: "0.25rem" }),
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
    }),
    menu: (base) => ({ ...base, zIndex: 50 }),
    menuList: (base) => ({
      ...base,
      paddingRight: "4px",
      "::-webkit-scrollbar": { width: "6px", height: "6px" },
      "::-webkit-scrollbar-thumb": { background: "#cbd5e1", borderRadius: "3px" },
    }),
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
      maxMenuHeight={250}
      menuPlacement={menuPlacement}
    />
  );
};
