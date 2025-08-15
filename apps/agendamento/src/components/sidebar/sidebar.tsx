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
    SidebarMenuItem
} from "@/components/ui/sidebar";
import { ClockIcon, TasklistIcon, CalendarIcon, ChevronDownIcon, ChecklistIcon } from '@primer/octicons-react';
import styles from "./sidebar.module.css";
import Image from "next/image";
import { Collapsible, CollapsibleContent, CollapsibleTrigger } from "../ui/collapsible";
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
            <SidebarContent>
                <Collapsible defaultOpen={false} className="group/collapsible">
                    <SidebarGroup>
                        <SidebarGroupLabel asChild>
                            <CollapsibleTrigger className={'font-base gap-2'}>
                                <ClockIcon size={20} />
                                <span className="text-base">Agendamentos</span>
                                <ChevronDownIcon size={16} className="ml-auto transition-transform group-data-[state=open]/collapsible:rotate-180" />
                            </CollapsibleTrigger>
                        </SidebarGroupLabel>
                        <CollapsibleContent>
                            <SidebarMenu>
                                <SidebarMenuItem>
                                    <Link href='/' passHref>
                                        <SidebarMenuButton className={'pl-8'} >
                                            <ChecklistIcon size={16} />
                                            <span className="text-base">Agendamentos do dia</span>
                                        </SidebarMenuButton>
                                    </Link>
                                </SidebarMenuItem>
                                <SidebarMenuItem>
                                    <Link href='/all-appointments' passHref>
                                        <SidebarMenuButton className={'pl-8'} >
                                            <TasklistIcon size={16} />
                                            <span className="text-base">Todos os agendamentos</span>
                                        </SidebarMenuButton>
                                    </Link>
                                </SidebarMenuItem>
                                <SidebarMenuItem>
                                    <SidebarMenuButton className={'pl-8'}>
                                        <CalendarIcon size={16} />
                                        <span className="text-base">Calendário</span>
                                    </SidebarMenuButton>
                                </SidebarMenuItem>
                            </SidebarMenu>
                        </CollapsibleContent>
                    </SidebarGroup>
                </Collapsible>
            </SidebarContent>
            <SidebarFooter />
        </Sidebar>
    );
}