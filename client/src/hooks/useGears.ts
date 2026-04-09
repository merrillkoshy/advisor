import { gearsQuery } from "@/api/gears";
import { useQuery } from "@tanstack/react-query";

export function useGears() {
  return useQuery(gearsQuery);
}
