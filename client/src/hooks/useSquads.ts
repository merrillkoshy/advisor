import { squadQuery, squadsQuery } from "@/api/squads";
import { useQuery } from "@tanstack/react-query";

export function useSquads({ playerId }: { playerId: number }) {
  return useQuery(squadsQuery(playerId));
}

export function useSquad({
  playerId,
  squadNumber,
}: {
  playerId: number;
  squadNumber: number;
}) {
  return useQuery(squadQuery(playerId, squadNumber));
}
