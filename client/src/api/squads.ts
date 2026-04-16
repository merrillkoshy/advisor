import type { SquadSlotRequest } from "@/dto";
import type { Squad } from "@/types";
import { queryOptions } from "@tanstack/react-query";
import { apiGet, apiPut } from "./client";

export const squadsQuery = (playerId: number) =>
  queryOptions({
    queryKey: ["squads", playerId],
    queryFn: () => apiGet<Squad[]>(`/players/${playerId}/squads`),
  });

export const squadQuery = (playerId: number, squadNumber: number) =>
  queryOptions({
    queryKey: ["squads", playerId, squadNumber],
    queryFn: () => apiGet<Squad>(`/players/${playerId}/squads/${squadNumber}`),
  });

export async function saveSquad(
  playerId: number,
  squadNumber: number,
  slots: SquadSlotRequest[],
) {
  return apiPut<Squad>(`/players/${playerId}/squads/${squadNumber}`, slots);
}
