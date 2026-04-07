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
import {
  Gift,
  Medal,
  PawPrint,
  Plane,
  Shield,
  Sword,
  TestTube,
  Turntable,
  UserStar,
  WalletCards,
  WandSparkles,
} from "lucide-react";
import * as appPackage from "../../package.json";

const mainNav = [{ title: "Advisor", url: "/", icon: Turntable }];

const heroNav = [
  { title: "Heroes", url: "/heroes", icon: Sword },
  { title: "Gear", url: "/gears", icon: Shield },
];

const armyNav = [
  { title: "Drone", url: "/army/drone", icon: Plane },
  { title: "Overlord", url: "/army/overlord", icon: PawPrint },
  { title: "Tech", url: "/army/tech", icon: TestTube },
  { title: "Unit", url: "/army/unit", icon: UserStar },
  { title: "Wall of Honor", url: "/army/wall-of-honor", icon: Medal },
  { title: "Cosmetics", url: "/army/cosmetics", icon: WandSparkles },
  { title: "Tactics Cards", url: "/army/tactics-cards", icon: WalletCards },
  { title: "Decorations", url: "/army/decorations", icon: Gift },
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
