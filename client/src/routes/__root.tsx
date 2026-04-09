import { AppLayout } from "@/layout/AppLayout";
import type { RouterContext } from "@/router/context";
import { createRootRouteWithContext } from "@tanstack/react-router";

export const Route = createRootRouteWithContext<RouterContext>()({
  component: AppLayout,
  errorComponent: ({ error }) => {
    return (
      <div>
        <h1>Something went wrong!</h1>
        <p>{(error as Error).message}</p>
      </div>
    );
  },
});
