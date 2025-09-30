"use client";
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
} from "@/components/ui/sidebar";
import {
  ChecklistIcon,
  ChevronDownIcon,
  ClockIcon,
  IdBadgeIcon,
  TasklistIcon,
  ArrowLeftIcon,
} from "@primer/octicons-react";
import Image from "next/image";
import Link from "next/link";
import {
  Collapsible,
  CollapsibleContent,
  CollapsibleTrigger,
} from "../ui/collapsible";
import styles from "./sidebar.module.css";
import { useSidebar } from "@/components/ui/sidebar";
import { usePathname } from "next/navigation";
import { cn } from "@/lib/utils";

export function AppSidebar() {
  const { open, setOpen } = useSidebar();
  const pathname = usePathname();

  return (
    <Sidebar className={styles.sidebar}>
      <SidebarHeader className={styles.header}>
        <button className={styles.closeButton} onClick={() => setOpen(false)}>
          <ArrowLeftIcon size={20} />
        </button>
        <div className={styles.logoContainer}>
          <Image
            className={styles.logo}
            src={require("../../assets/APAE-logo.svg")}
            width={60}
            height={60}
            alt="Logo da APAE"
          />
          <div className={styles.textContainer}>
            <div className={styles.title}>APAE</div>
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
                  <Link href="/" passHref>
                    <SidebarMenuButton
                      className={`${styles.menuButton} font-base gap-2 ${cn(
                                "h-10 transition-colors",
                                pathname == "/"
                                  ? "bg-[#FFFFFF] !text-[#000000]"
                                  : "text-[#0D4F97] hover:bg-[#0D4F97] hover:!text-white"
                              )}`}
                    >
                      <ChecklistIcon size={16} />
                      <span className="text-base">Agendamentos do dia</span>
                    </SidebarMenuButton>
                  </Link>
                </SidebarMenuItem>
                <SidebarMenuItem>
                  <Link href="/all-appointments" passHref>
                    <SidebarMenuButton
                      className={`${styles.menuButton} font-base gap-2 ${cn(
                                "h-10 transition-colors",
                                pathname == "/all-appointments"
                                  ? "bg-[#FFFFFF] !text-[#000000]"
                                  : "text-[#0D4F97] hover:bg-[#0D4F97] hover:!text-white"
                              )}`}
                    >
                      <TasklistIcon size={16} />
                      <span className="text-base">Todos os agendamentos</span>
                    </SidebarMenuButton>
                  </Link>
                </SidebarMenuItem>
              </SidebarMenu>
            </CollapsibleContent>
          </SidebarGroup>
        </Collapsible>
        <SidebarGroup className="m-0 pt-0">
          <Link href="/visualization-professional" passHref>
            <SidebarMenuButton
              className={`${styles.menuButton} font-base gap-2 ${cn(
                        "h-10 transition-colors",
                        pathname == "/visualization-professional"
                          ? "bg-[#FFFFFF] !text-[#000000]"
                          : "text-[#0D4F97] hover:bg-[#0D4F97] hover:!text-white"
                      )}`}
            >
              <IdBadgeIcon size={20} />
              <span className="text-base">Profissionais da Saúde</span>
            </SidebarMenuButton>
          </Link>
        </SidebarGroup>
      </SidebarContent>
      <SidebarFooter />
    </Sidebar>
  );
}