import type { Gear } from "@/types";
import { queryOptions } from "@tanstack/react-query";
import { apiGet } from "./client";

export const gearsQuery = queryOptions({
  queryKey: ["gears"],
  queryFn: () => apiGet<Gear[]>("/gears"),
});
