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
  ClockIcon,
  TasklistIcon,
  CalendarIcon,
  ChevronDownIcon,
  IdBadgeIcon,
} from "@primer/octicons-react";
import styles from "./sidebar.module.css";
import Image from "next/image";
import {
  Collapsible,
  CollapsibleContent,
  CollapsibleTrigger,
} from "../ui/collapsible";
import Link from "next/link";

export function AppSidebar() {
  return (
    <Sidebar className={styles.sidebar}>
      <SidebarHeader className={styles.header}>
        <Image
          className={styles.logo}
          src={require("../../assets/APAE-logo.svg")}
          width={35}
          height={35}
          alt="Logo da APAE"
        />
        <div>
          <div className={styles.title}>APAE</div>
          <div className={styles.subtitle}>Agendamento</div>
        </div>
      </SidebarHeader>
      <SidebarContent className="m-0 p-0 gap-0">
        <SidebarGroup className="m-0 pt-0">
          <Collapsible defaultOpen={false} className="group/collapsible">
            <SidebarGroupLabel asChild>
              <SidebarMenuButton asChild>
                <CollapsibleTrigger className="font-base gap-2">
                  <ClockIcon size={20} />
                  <span className="text-base">Agendamentos</span>
                  <ChevronDownIcon
                    size={16}
                    className="ml-auto transition-transform group-data-[state=open]/collapsible:rotate-180"
                  />
                </CollapsibleTrigger>
              </SidebarMenuButton>
            </SidebarGroupLabel>
            <CollapsibleContent>
              <SidebarMenu>
                <SidebarMenuItem>
                  <SidebarMenuButton className="pl-8">
                    <TasklistIcon size={16} />
                    <span className="text-base">Todos os agendamentos</span>
                  </SidebarMenuButton>
                </SidebarMenuItem>
                <SidebarMenuItem>
                  <SidebarMenuButton className="pl-8">
                    <CalendarIcon size={16} />
                    <span className="text-base">Calendário</span>
                  </SidebarMenuButton>
                </SidebarMenuItem>
              </SidebarMenu>
            </CollapsibleContent>
          </Collapsible>
        </SidebarGroup>

        <SidebarGroup className="m-0 pt-0">
          <Link href="/visualization-professional" passHref>
            <SidebarMenuButton className="font-base gap-2">
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
