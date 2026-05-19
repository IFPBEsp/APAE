import { Users, BarChart, SquareActivity, BriefcaseMedical, Calendar, UserCog, FileText, Stethoscope } from "lucide-react"

export const NAV = [
  { label: "Pacientes", href: "/patients", icon: Users },
  { label: "Dashboard", href: "/dashboard", icon: BarChart },
  { label: "Profissionais", href: "/professionals", icon: UserCog },
  { label: "Agendamentos", href: "/appointments/list", icon: Calendar },
  { label: "Transtornos", href: "/disorders", icon: SquareActivity },
  { label: "Vacinas", href: "/vaccines", icon: BriefcaseMedical },
  { label: "Tipos de atendimento", href: "/service-types", icon: Stethoscope },
  { label: "Documentos", href: "/documents", icon: FileText },
]