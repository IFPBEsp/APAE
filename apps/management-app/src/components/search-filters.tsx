"use client";

import { useState, useEffect } from "react";
import { Input } from "@/components/ui/input";
import { Button } from "@/components/ui/button";
import {
  DropdownMenu,
  DropdownMenuContent,
  DropdownMenuItem,
  DropdownMenuTrigger,
} from "@/components/ui/dropdown-menu";
import { ChevronDown, Search } from "lucide-react";

export function SearchFilters() {
  const [activeFilter, setActiveFilter] = useState('medico'); 

  return (
    <div className="flex items-center gap-[103px]">
      <div className="flex items-center flex-grow gap-2">
        <div className="relative flex-1">
          <Search className="absolute left-3 top-1/2 -translate-y-1/2 h-5 w-5 text-gray-500" />
          <Input
            placeholder="Busque aqui"
            className="pl-10 h-[36px] border-2 border-[#0D4F97] rounded-[5px] placeholder-[#0D4F97]"
          />
        </div>

        <DropdownMenu>
          <DropdownMenuTrigger asChild>
            <Button
              variant="outline"
              className="w-[98px] h-[36px] border-2 border-[#003B93] rounded-[5px] justify-between text-[#003B93] hover:text-[#003B93] hover:bg-slate-50"
            >
              Todos
              <ChevronDown className="h-4 w-4" />
            </Button>
          </DropdownMenuTrigger>
          <DropdownMenuContent align="end" className="w-[120px]">
            <DropdownMenuItem>Ativos</DropdownMenuItem>
            <DropdownMenuItem>Inativos</DropdownMenuItem>
            <DropdownMenuItem>Em Fila</DropdownMenuItem>
          </DropdownMenuContent>
        </DropdownMenu>
      </div>

      <div className="flex items-center gap-2">
        <Button
          className={`${activeFilter === 'medico' ? 'bg-[#0D4F97] text-white' : 'bg-white text-[#0D4F97] border border-[#0D4F97]' } h-[36px] px-4 rounded-[5px] hover:bg-[#0b427d] hover:text-white`}
          onClick={() => setActiveFilter('medico')}
        >
          Médico
        </Button>
        <Button
          className={`${activeFilter === 'escolar' ? 'bg-[#0D4F97] text-white' : 'bg-white text-[#0D4F97] border border-[#0D4F97]' } h-[36px] px-4 rounded-[5px] hover:bg-[#0b427d] hover:text-white`}
          onClick={() => setActiveFilter('escolar')}
        >
          Escolar
        </Button>
      </div>
    </div>
  );
}