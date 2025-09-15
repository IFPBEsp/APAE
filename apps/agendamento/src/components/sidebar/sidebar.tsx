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
  TasklistIcon
} from "@primer/octicons-react";
import Image from "next/image";
import Link from "next/link";
import {
  Collapsible,
  CollapsibleContent,
  CollapsibleTrigger,
} from "../ui/collapsible";
import styles from "./sidebar.module.css";

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
      <SidebarContent>
        <Collapsible defaultOpen={false} className="group/collapsible">
          <SidebarGroup>
            <SidebarGroupLabel asChild>
              <CollapsibleTrigger className={"font-base gap-2"}>
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
                    <SidebarMenuButton className={"pl-8"}>
                      <ChecklistIcon size={16} />
                      <span className="text-base">Agendamentos do dia</span>
                    </SidebarMenuButton>
                  </Link>
                </SidebarMenuItem>
                <SidebarMenuItem>
                  <Link href="/all-appointments" passHref>
                    <SidebarMenuButton className={"pl-8"}>
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
