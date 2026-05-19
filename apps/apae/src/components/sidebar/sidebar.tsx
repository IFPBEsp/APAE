"use client";

import {
  BriefcaseMedical,
  LogOut,
  ShieldUser,
  SquareActivity,
  Stethoscope,
  Syringe,
  UserRoundPlus,
} from "lucide-react";
import Image from "next/image";
import Link from "next/link";
import { usePathname, useRouter } from "next/navigation";

import {
  Sidebar,
  SidebarContent,
  SidebarFooter,
  SidebarGroup,
  SidebarGroupLabel,
  SidebarHeader,
  SidebarMenu,
  SidebarMenuButton,
  SidebarMenuItem,
  useSidebar,
} from "@/components/ui/sidebar";
import { removeSessionCookie } from "@/lib/cookies";
import { cn } from "@/lib/utils";
import {
  AlertIcon,
  ArrowLeftIcon,
  ChecklistIcon,
  ChevronDownIcon,
  ClockIcon,
  IdBadgeIcon,
  PeopleIcon,
  PersonIcon,
  TasklistIcon,
} from "@primer/octicons-react";
import logo from "../../assets/logo.png";
import {
  Collapsible,
  CollapsibleContent,
  CollapsibleTrigger,
} from "../ui/collapsible";
import styles from "./sidebar.module.css";

export function AppSidebar() {
  const { setOpen, isMobile, setOpenMobile } = useSidebar();

  const pathname = usePathname();
  const router = useRouter();

  const handleLogout = async () => {
    await removeSessionCookie();
    router.replace("/auth/login");
  };

  return (
    <Sidebar className={styles.sidebar}>
      <SidebarHeader className={styles.header}>
        <button
          className={styles.closeButton}
          onClick={() => {
            if (isMobile) setOpenMobile(false);
            else setOpen(false);
          }}
        >
          <ArrowLeftIcon size={20} />
        </button>
        <div className={styles.logoContainer}>
          <Image
            className={styles.logo}
            src={logo}
            width={60}
            height={60}
            alt="Logo"
          />
          <div className={styles.textContainer}>
            <div className={styles.title}></div>
            <div className={styles.subtitle}>Agendamento</div>
          </div>
        </div>
      </SidebarHeader>
      <SidebarContent>
        <Collapsible defaultOpen={false} className="group/collapsible">
          <SidebarGroup>
            <SidebarGroupLabel asChild>
              <CollapsibleTrigger
                className={`${styles.menuButton} font-base gap-2`}
              >
                <ClockIcon size={20} />
                <span className="text-base">Agendamentos</span>
                <ChevronDownIcon
                  size={16}
                  className="ml-auto transition-transform group-data-[state=open]/collapsible:rotate-180"
                />
              </CollapsibleTrigger>
            </SidebarGroupLabel>
            <CollapsibleContent>
              <SidebarMenu>
                <SidebarMenuItem>
                  <Link href="/" prefetch={false} passHref>
                    <SidebarMenuButton
                      className={`${styles.menuButton} font-base gap-2 ${cn(
                        "h-10 transition-colors",
                        pathname == "/"
                          ? "bg-[#FFFFFF] text-[#000000]!"
                          : "text-[#0D4F97] hover:bg-[#0D4F97] hover:text-white!",
                      )}`}
                    >
                      <ChecklistIcon size={16} />
                      <span className="text-base">Agendamentos do dia</span>
                    </SidebarMenuButton>
                  </Link>
                </SidebarMenuItem>
                <SidebarMenuItem>
                  <Link href="/appointments/list" prefetch={false} passHref>
                    <SidebarMenuButton
                      className={`${styles.menuButton} font-base gap-2 ${cn(
                        "h-10 transition-colors",
                        pathname == "/appointments/list"
                          ? "bg-[#FFFFFF] text-[#000000]!"
                          : "text-[#0D4F97] hover:bg-[#0D4F97] hover:text-white!",
                      )}`}
                    >
                      <TasklistIcon size={16} />
                      <span className="text-base">Todos os agendamentos</span>
                    </SidebarMenuButton>
                  </Link>
                </SidebarMenuItem>
                <SidebarMenuItem>
                  <Link href="/absence" passHref>
                    <SidebarMenuButton
                      className={`${styles.menuButton} font-base gap-2 ${cn(
                        "h-10 transition-colors",
                        pathname == "/absence"
                          ? "bg-[#FFFFFF] text-[#000000]!"
                          : "text-[#0D4F97] hover:bg-[#0D4F97] hover:text-white!",
                      )}`}
                    >
                      <AlertIcon size={16} />
                      <span className="text-base">Faltas</span>
                    </SidebarMenuButton>
                  </Link>
                </SidebarMenuItem>
              </SidebarMenu>
            </CollapsibleContent>
          </SidebarGroup>
        </Collapsible>

        <Collapsible defaultOpen={false} className="group/collapsible">
          <SidebarGroup>
            <SidebarGroupLabel asChild>
              <CollapsibleTrigger
                className={`${styles.menuButton} font-base gap-2`}
              >
                <IdBadgeIcon size={20} />
                <span className="text-base">Profissionais da Saúde</span>
                <ChevronDownIcon
                  size={16}
                  className="ml-auto transition-transform group-data-[state=open]/collapsible:rotate-180"
                />
              </CollapsibleTrigger>
            </SidebarGroupLabel>
            <CollapsibleContent>
              <SidebarMenu>
                <SidebarMenuItem>
                  <Link href="/professionals" passHref>
                    <SidebarMenuButton
                      className={`${styles.menuButton} font-base gap-2 ${cn(
                        "h-10 transition-colors",
                        pathname == "/professionals"
                          ? "bg-[#FFFFFF] text-[#000000]!"
                          : "text-[#0D4F97] hover:bg-[#0D4F97] hover:text-white!",
                      )}`}
                    >
                      <Stethoscope size={20} />
                      <span className="text-base">Profissionais</span>
                    </SidebarMenuButton>
                  </Link>
                </SidebarMenuItem>
                <SidebarMenuItem>
                  <Link href="/service-types" passHref>
                    <SidebarMenuButton
                      className={`${styles.menuButton} font-base gap-2 ${cn(
                        "h-10 transition-colors",
                        pathname == "/service-types"
                          ? "bg-[#FFFFFF] text-[#000000]!"
                          : "text-[#0D4F97] hover:bg-[#0D4F97] hover:text-white!",
                      )}`}
                    >
                      <BriefcaseMedical size={16} />
                      <span className="text-base">Tipos de Atendimento</span>
                    </SidebarMenuButton>
                  </Link>
                </SidebarMenuItem>
              </SidebarMenu>
            </CollapsibleContent>
          </SidebarGroup>
        </Collapsible>

        <Collapsible defaultOpen={false} className="group/collapsible">
          <SidebarGroup>
            <SidebarGroupLabel asChild>
              <CollapsibleTrigger
                className={`${styles.menuButton} font-base gap-2`}
              >
                <PersonIcon size={20} />
                <span className="text-base">Pacientes</span>
                <ChevronDownIcon
                  size={16}
                  className="ml-auto transition-transform group-data-[state=open]/collapsible:rotate-180"
                />
              </CollapsibleTrigger>
            </SidebarGroupLabel>
            <CollapsibleContent>
              <SidebarMenu>
                <SidebarMenuItem>
                  <Link href="/patients" passHref>
                    <SidebarMenuButton
                      className={`${styles.menuButton} font-base gap-2 ${cn(
                        "h-10 transition-colors",
                        pathname == "/patients"
                          ? "bg-[#FFFFFF] text-[#000000]!"
                          : "text-[#0D4F97] hover:bg-[#0D4F97] hover:text-white!",
                      )}`}
                    >
                      <PeopleIcon size={16} />
                      <span className="text-base">Pessoas</span>
                    </SidebarMenuButton>
                  </Link>
                </SidebarMenuItem>
                <SidebarMenuItem>
                  <Link href="/disorders" passHref>
                    <SidebarMenuButton
                      className={`${styles.menuButton} font-base gap-2 ${cn(
                        "h-10 transition-colors",
                        pathname == "/disorders"
                          ? "bg-[#FFFFFF] text-[#000000]!"
                          : "text-[#0D4F97] hover:bg-[#0D4F97] hover:text-white!",
                      )}`}
                    >
                      <SquareActivity size={16} />
                      <span className="text-base">Transtornos</span>
                    </SidebarMenuButton>
                  </Link>
                </SidebarMenuItem>
                <SidebarMenuItem>
                  <Link href="/vaccines" passHref>
                    <SidebarMenuButton
                      className={`${styles.menuButton} font-base gap-2 ${cn(
                        "h-10 transition-colors",
                        pathname == "/vaccines"
                          ? "bg-[#FFFFFF] text-[#000000]!"
                          : "text-[#0D4F97] hover:bg-[#0D4F97] hover:text-white!",
                      )}`}
                    >
                      <Syringe size={16} />
                      <span className="text-base">Vacinas</span>
                    </SidebarMenuButton>
                  </Link>
                </SidebarMenuItem>
              </SidebarMenu>
            </CollapsibleContent>
          </SidebarGroup>
        </Collapsible>

        <Collapsible defaultOpen={false} className="group/collapsible">
          <SidebarGroup>
            <SidebarGroupLabel asChild>
              <CollapsibleTrigger
                className={`${styles.menuButton} font-base gap-2`}
              >
                <ShieldUser size={20} />
                <span className="text-base">Área do Administrador</span>
                <ChevronDownIcon
                  size={16}
                  className="ml-auto transition-transform group-data-[state=open]/collapsible:rotate-180"
                />
              </CollapsibleTrigger>
            </SidebarGroupLabel>
            <CollapsibleContent>
              <SidebarMenu>
                <SidebarMenuItem>
                  <Link href="/register">
                    <SidebarMenuButton
                      className={`${styles.menuButton} font-base gap-2 ${cn(
                        "h-10 transition-colors",
                        pathname == ""
                          ? "bg-[#FFFFFF] text-[#000000]!"
                          : "text-[#0D4F97] hover:bg-[#0D4F97] hover:text-white!",
                      )}`}
                    >
                      <UserRoundPlus size={16} />
                      <span className="text-base">Cadastrar Usuário</span>
                    </SidebarMenuButton>
                  </Link>
                </SidebarMenuItem>
              </SidebarMenu>
            </CollapsibleContent>
          </SidebarGroup>
        </Collapsible>
      </SidebarContent>

      <SidebarFooter>
        <button
          onClick={handleLogout}
          className={`
          flex w-full items-center rounded-3xl bg-transparent text-[#0D4F97] transition-all hover:bg-white/40 justify-center gap-3 px-4 py-3 cursor-pointer`}
          title="Sair"
        >
          <LogOut className="h-5 w-5 shrink-0" strokeWidth={1.75} />
          {<span className="font-medium">Sair</span>}
        </button>
      </SidebarFooter>
    </Sidebar>
  );
}
