import type { DroneComponentRequest } from "@/dto";
import type { Drone } from "@/types";
import { queryOptions } from "@tanstack/react-query";
import { apiGet, apiPut } from "./client";

export const droneQuery = (playerId: number) =>
  queryOptions({
    queryKey: ["drone", playerId],
    queryFn: () => apiGet<Drone>(`/players/${playerId}/drone`),
  });

export async function saveDrone(playerId: number, level: number) {
  return apiPut<Drone>(`/players/${playerId}/drone`, level);
}

export async function saveDroneComponents(
  playerId: number,
  components: DroneComponentRequest[],
) {
  return apiPut<Drone>(`/players/${playerId}/drone/components`, components);
}
