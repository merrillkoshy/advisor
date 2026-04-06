import { SidebarProvider, SidebarTrigger } from "@/components/ui/sidebar";
import { Outlet } from "@tanstack/react-router";
import { AppSidebar } from "./AppSidebar";

export function AppLayout() {
  return (
    <SidebarProvider>
      <AppSidebar />
      <main className="flex flex-col flex-1">
        <div className="flex items-center p-2 border-b">
          <SidebarTrigger />
        </div>
        <div className="flex-1 p-4">
          <Outlet />
        </div>
      </main>
    </SidebarProvider>
  );
}
