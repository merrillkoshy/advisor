import {
  Sidebar,
  SidebarContent,
  SidebarFooter,
  SidebarGroup,
  SidebarGroupContent,
  SidebarGroupLabel,
  SidebarHeader,
  SidebarMenu,
  SidebarMenuButton,
  SidebarMenuItem,
} from "@/components/ui/sidebar";
import { APP_NAME } from "@/constants";
import { Link } from "@tanstack/react-router";
import { Cpu, Home, Settings, Shield, Sword } from "lucide-react";
import * as appPackage from "../../package.json";

const mainNav = [{ title: "Advisor", url: "/", icon: Home }];

const heroNav = [
  { title: "Heroes", url: "/heroes", icon: Sword },
  { title: "Gear", url: "/gears", icon: Shield },
];

const armyNav = [
  { title: "Drone", url: "/army/drone", icon: Cpu },
  { title: "Overlord", url: "/army/overlord", icon: Settings },
];
const SideBarItem = ({ title, url, icon: Icon }: any) => (
  <SidebarMenuItem>
    <SidebarMenuButton tooltip={title} size={"lg"}>
      <Link to={url} className="flex items-center gap-4">
        <Icon />
        <span>{title}</span>
      </Link>
    </SidebarMenuButton>
  </SidebarMenuItem>
);
export function AppSidebar() {
  return (
    <Sidebar collapsible="icon">
      <SidebarHeader className="flex flex-row items-center justify-between px-5">
        <span className="font-bold text-lg group-data-[collapsible=icon]:hidden">
          {APP_NAME}
        </span>
      </SidebarHeader>
      <SidebarContent className="px-2">
        <SidebarGroup>
          <SidebarGroupContent>
            <SidebarMenu>
              {mainNav.map((item) => (
                <SideBarItem key={item.title} {...item} />
              ))}
            </SidebarMenu>
          </SidebarGroupContent>
        </SidebarGroup>
        <SidebarGroup>
          <SidebarGroupLabel>Heroes</SidebarGroupLabel>
          <SidebarGroupContent>
            <SidebarMenu>
              {heroNav.map((item) => (
                <SideBarItem key={item.title} {...item} />
              ))}
            </SidebarMenu>
          </SidebarGroupContent>
        </SidebarGroup>
        <SidebarGroup>
          <SidebarGroupLabel>Army</SidebarGroupLabel>
          <SidebarGroupContent>
            <SidebarMenu>
              {armyNav.map((item) => (
                <SideBarItem key={item.title} {...item} />
              ))}
            </SidebarMenu>
          </SidebarGroupContent>
        </SidebarGroup>
      </SidebarContent>
      <SidebarFooter>
        <span className="text-xs text-muted-foreground px-2">
          v {appPackage.version}
        </span>
      </SidebarFooter>
    </Sidebar>
  );
}
