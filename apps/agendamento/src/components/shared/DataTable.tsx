import {
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableHeader,
  TableRow,
} from "@/components/ui/table"

interface Column {
  header: string
  accessor: string
}

interface DataTableProps {
  columns: Column[]
  data: any[]
  actions?: (row: any) => React.ReactNode
}

export function DataTable({ columns, data, actions }: DataTableProps) {
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
              <TableCell key={j}>{row[col.accessor]}</TableCell>
            ))}
            {actions && <TableCell>{actions(row)}</TableCell>}
          </TableRow>
        ))}
      </TableBody>
    </Table>
  )
}
