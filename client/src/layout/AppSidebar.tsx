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
import { APP_NAME, APP_PATHS } from "@/constants";
import { Link } from "@tanstack/react-router";
import {
  Gift,
  Medal,
  PawPrint,
  Plane,
  Shield,
  Sword,
  Swords,
  TestTube,
  Turntable,
  Users,
  UserStar,
  WalletCards,
  WandSparkles,
  type LucideProps,
} from "lucide-react";
import * as appPackage from "../../package.json";

interface SideBarItem {
  title: string;
  url: string;
  icon: React.ForwardRefExoticComponent<
    Omit<LucideProps, "ref"> & React.RefAttributes<SVGSVGElement>
  >;
}

const mainNav = [{ title: "Advisor", url: APP_PATHS.advisor, icon: Turntable }];

const heroNav = [
  { title: "Heroes", url: APP_PATHS.heroes, icon: Sword },
  { title: "Gear", url: APP_PATHS.gears, icon: Shield },
];

const squadNav = [{ title: "Squads", url: APP_PATHS.squads, icon: Users }];

const adminNav = [
  { title: "Opponent Configuration", url: APP_PATHS.opponents, icon: Swords },
];

const armyNav = [
  { title: "Drone", url: APP_PATHS.drone, icon: Plane },
  { title: "Overlord", url: APP_PATHS.overlord, icon: PawPrint },
  { title: "Tech", url: APP_PATHS.tech, icon: TestTube },
  { title: "Unit", url: APP_PATHS.unit, icon: UserStar },
  { title: "Wall of Honor", url: APP_PATHS.wall_of_honor, icon: Medal },
  { title: "Cosmetics", url: APP_PATHS.cosmetics, icon: WandSparkles },
  { title: "Tactics Cards", url: APP_PATHS.tactics_cards, icon: WalletCards },
  { title: "Decorations", url: APP_PATHS.decorations, icon: Gift },
];

const SideBarItem = ({ title, url, icon: Icon }: SideBarItem) => (
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
          <SidebarGroupLabel>Squads</SidebarGroupLabel>
          <SidebarGroupContent>
            <SidebarMenu>
              {squadNav.map((item) => (
                <SideBarItem key={item.title} {...item} />
              ))}
            </SidebarMenu>
          </SidebarGroupContent>
        </SidebarGroup>
        {/* Authorized route */}
        <SidebarGroup>
          <SidebarGroupLabel>Admin</SidebarGroupLabel>
          <SidebarGroupContent>
            <SidebarMenu>
              {adminNav.map((item) => (
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
