import { opponentSquadQuery, opponentSquadsQuery } from "@/api/opponents";
import { useQuery } from "@tanstack/react-query";

export function useOpponentSquads({ opponentId }: { opponentId: number }) {
  return useQuery(opponentSquadsQuery(opponentId));
}

export function useOpponentSquad({
  opponentId,
  squadNumber,
}: {
  opponentId: number;
  squadNumber: number;
}) {
  return useQuery(opponentSquadQuery(opponentId, squadNumber));
}
