import { droneQuery } from "@/api/drone";
import { useQuery } from "@tanstack/react-query";

export function useDrone({ playerId }: { playerId: number }) {
  return useQuery(droneQuery(playerId));
}
