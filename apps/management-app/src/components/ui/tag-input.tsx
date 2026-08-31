"use client";

import React, { useState, KeyboardEvent } from "react";
import { X } from "lucide-react";

interface TagInputProps {
  placeholder?: string;
  value: string | null | undefined;
  onChange: (value: string) => void;
}

export function TagInput({ placeholder, value, onChange }: TagInputProps) {
  const [inputValue, setInputValue] = useState("");
  const tags = value
    ? value
        .split(",")
        .map((t) => t.trim())
        .filter(Boolean)
    : [];

  const addTag = (tag: string) => {
    const trimmed = tag.trim();
    if (trimmed && !tags.includes(trimmed)) {
      const newTags = [...tags, trimmed];
      onChange(newTags.join(", "));
      setInputValue("");
    }
  };

  const removeTag = (tagToRemove: string) => {
    const newTags = tags.filter((tag) => tag !== tagToRemove);
    onChange(newTags.join(", "));
  };

  const handleKeyDown = (e: KeyboardEvent<HTMLInputElement>) => {
    if (e.key === "Enter" || e.key === ",") {
      e.preventDefault();
      addTag(inputValue);
    } else if (e.key === "Backspace" && !inputValue && tags.length > 0) {
      removeTag(tags[tags.length - 1]);
    }
  };

  return (
    <div className="flex flex-wrap gap-2 p-2 border rounded-md bg-white focus-within:ring-2 focus-within:ring-offset-2 focus-within:ring-slate-400 border-slate-200 min-h-[2.5rem]">
      {tags.map((tag, index) => (
        <span
          key={index}
          className="flex items-center gap-1 bg-blue-50 text-[#0D4F97] px-2 py-1 rounded-md text-sm font-semibold"
        >
          {tag}
          <button
            type="button"
            onClick={() => removeTag(tag)}
            className="rounded-full hover:bg-blue-200 p-0.5 transition-colors"
          >
            <X size={12} />
          </button>
        </span>
      ))}
      <input
        className="flex-1 outline-none bg-transparent text-sm min-w-[120px] h-6 mt-0.5"
        placeholder={tags.length === 0 ? placeholder : ""}
        value={inputValue}
        onChange={(e) => setInputValue(e.target.value)}
        onKeyDown={handleKeyDown}
        onBlur={() => inputValue && addTag(inputValue)}
      />
    </div>
  );
}
