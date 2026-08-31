import { ReactNode } from "react";

import {
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableHeader,
  TableRow,
} from "@/components/ui/table";

interface Column<T> {
  header: string;
  accessor: keyof T;
}

interface DataTableProps<T> {
  columns: Column<T>[];
  data: T[];
  actions?: (row: T) => ReactNode;
}

export function DataTable<T>({ columns, data, actions }: DataTableProps<T>) {
  return (
    <Table>
      <TableHeader>
        <TableRow>
          {columns.map((col, index) => (
            <TableHead key={index}>{col.header}</TableHead>
          ))}
          {actions && <TableHead>Ações</TableHead>}
        </TableRow>
      </TableHeader>
      <TableBody>
        {data.map((row, i) => (
          <TableRow key={i}>
            {columns.map((col, j) => (
              <TableCell key={j}>{row[col.accessor] as React.ReactNode}</TableCell>
            ))}
            {actions && <TableCell>{actions(row)}</TableCell>}
          </TableRow>
        ))}
      </TableBody>
    </Table>
  );
}
