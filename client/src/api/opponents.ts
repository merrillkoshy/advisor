import type { DroneComponentRequest, SquadSlotRequest } from "@/dto";
import type { Drone, Squad } from "@/types";
import { queryOptions } from "@tanstack/react-query";
import { apiGet, apiPut } from "./client";

export const opponentSquadsQuery = (opponentId: number) =>
  queryOptions({
    queryKey: ["opponentSquads", opponentId],
    queryFn: () => apiGet<Squad[]>(`/opponents/${opponentId}/squads`),
  });

export const opponentSquadQuery = (opponentId: number, squadNumber: number) =>
  queryOptions({
    queryKey: ["opponentSquads", opponentId, squadNumber],
    queryFn: () =>
      apiGet<Squad>(`/opponents/${opponentId}/squads/${squadNumber}`),
    enabled: !!squadNumber,
  });

export async function saveOpponentSquad(
  opponentId: number,
  squadNumber: number,
  slots: SquadSlotRequest[],
) {
  return apiPut<Squad>(`/opponents/${opponentId}/squads/${squadNumber}`, slots);
}

export const opponentDroneQuery = (opponentId: number) =>
  queryOptions({
    queryKey: ["OpponentDrone", opponentId],
    queryFn: () => apiGet<Drone>(`/opponents/${opponentId}/drone`),
    enabled: !!opponentId,
  });

export async function saveOpponentDrone(opponentId: number, level: number) {
  return apiPut<Drone>(`/opponents/${opponentId}/drone`, level);
}

export async function saveOpponentDroneComponents(
  opponentId: number,
  components: DroneComponentRequest[],
) {
  return apiPut<Drone>(`/opponents/${opponentId}/drone/components`, components);
}
