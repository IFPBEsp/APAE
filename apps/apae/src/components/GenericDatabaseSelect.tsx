"use client";

import React, { useEffect, useState } from "react";
import CreatableSelect from "react-select/creatable";
import { MultiValue, StylesConfig } from "react-select";

interface Option {
  label: string;
  value: string;
  id?: string | number;
}

interface GenericDatabaseSelectProps<T> {
  value: T[];
  onChange: (value: T[]) => void;
  endpoint: string;
  placeholder?: string;
  labelSingular: string;
  labelKey: keyof T;
  menuPlacement?: "auto" | "bottom" | "top";
}

const capitalize = (s: string) =>
  s.charAt(0).toUpperCase() + s.slice(1).toLowerCase();

export const GenericDatabaseSelect = <T extends { id?: string | number }>({
  value,
  onChange,
  endpoint,
  placeholder,
  labelSingular,
  labelKey,
  menuPlacement = "auto",
}: GenericDatabaseSelectProps<T>) => {
  const [options, setOptions] = useState<Option[]>([]);
  const [isLoading, setIsLoading] = useState(false);

  useEffect(() => {
    const fetchData = async () => {
      try {
        const res = await fetch(endpoint);

        if (res.ok) {
          const data = (await res.json()) as T[];
          const safeData = Array.isArray(data) ? data : [];

          setOptions(
            safeData.map((d) => ({
              label: String(d[labelKey] ?? "Sem nome"),
              value: String(d[labelKey] ?? "Sem nome"),
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
        const text = String(v[labelKey] ?? "");

        return { label: text, value: text, id: v.id };
      })
      .filter((v) => v.label);
  };

  const handleChange = (newValue: MultiValue<Option>) => {
    const formatted = newValue.map(
      (v) =>
        ({
          [labelKey]: v.value,
          id: v.id,
        }) as unknown as T,
    );
    onChange(formatted);
  };

  const handleCreate = async (inputValue: string) => {
    const normalizedName = capitalize(inputValue.trim());
    const existingOption = options.find(
      (opt) => opt.label.toLowerCase() === normalizedName.toLowerCase(),
    );

    const currentSelected = Array.isArray(value) ? value : [];

    if (existingOption) {
      const isAlreadySelected = currentSelected.some(
        (s) =>
          s.id === existingOption.id ||
          String(s[labelKey] ?? "") === existingOption.value,
      );

      if (!isAlreadySelected) {
        onChange([
          ...currentSelected,
          {
            [labelKey]: existingOption.value,
            id: existingOption.id,
          } as unknown as T,
        ]);
      }

      return;
    }

    setIsLoading(true);
    try {
      const res = await fetch(endpoint, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ [labelKey]: normalizedName }),
      });

      const newOption: Option = {
        label: normalizedName,
        value: normalizedName,
      };

      if (res.ok || res.status === 201 || res.status === 409) {
        const created = (await res.json().catch(() => ({}))) as T;
        if (created && created.id) newOption.id = created.id;
      }

      if (!newOption.id) newOption.id = `temp-${Date.now()}`;

      setOptions((prev) => [...prev, newOption]);

      onChange([
        ...currentSelected,
        { [labelKey]: newOption.value, id: newOption.id } as unknown as T,
      ]);
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
      backgroundColor: "white",
      boxShadow: "none",
      "&:hover": { borderColor: "#cbd5e1" },
      ...(state.isFocused && {
        borderColor: "black",
        borderWidth: "1px",
        outline: "1px solid black",
      }),
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
    }),
    menu: (base) => ({ ...base, zIndex: 9999 }),
    menuPortal: (base) => ({ ...base, zIndex: 9999 }),
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
      formatCreateLabel={(inputValue) =>
        `Criar ${labelSingular} "${capitalize(inputValue)}"`
      }
      noOptionsMessage={() =>
        `Nenhum(a) ${labelSingular.toLowerCase()} encontrado(a)`
      }
      createOptionPosition="first"
      menuPlacement={menuPlacement}
      captureMenuScroll={false} // Evita bugs de scroll no modal
      closeMenuOnSelect={false} // Bom para MultiSelect
      tabSelectsValue={false}   // Melhora a acessibilidade no teclado
    />
  );
};
