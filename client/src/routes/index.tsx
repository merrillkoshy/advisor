import { analyze } from "@/api/advisor";
import { opponentSquadsQuery } from "@/api/opponents";
import { squadsQuery } from "@/api/squads";
import { Button } from "@/components/ui/button";
import {
  Select,
  SelectContent,
  SelectGroup,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from "@/components/ui/select";
import { OPPONENT_ID, PLAYER_ID } from "@/constants";
import type { AdvisorRequest } from "@/dto";
import { AnalysisResult } from "@/elements/Advisor/AnalysisResult";
import { BattlefieldLayout } from "@/elements/Advisor/BattlefieldLayout";
import { SkeletonLoader } from "@/elements/SkeletonLoader";
import { useOpponentSquads } from "@/hooks/useOpponentSquads";
import { useSquads } from "@/hooks/useSquads";
import stompClient from "@/lib/stompClient";
import type {
  AdvisorResult,
  DamageEvent,
  Squad,
  SquadSlot,
  TickSnapshot,
} from "@/types";
import { useMutation } from "@tanstack/react-query";

import { createFileRoute } from "@tanstack/react-router";
import { useState } from "react";

export const Route = createFileRoute("/")({
  component: AdvisorPage,
  loader: async ({ context }) => {
    await context.queryClient.ensureQueryData(squadsQuery(PLAYER_ID));
    await context.queryClient.ensureQueryData(opponentSquadsQuery(OPPONENT_ID));
  },
});

function AdvisorPage() {
  const [selectedSquad, setSelectedSquad] = useState<number>(1);
  const [selectedOpponentSquad, setSelectedOpponentSquad] = useState<number>(1);

  const [result, setResult] = useState<AdvisorResult | null>(null);
  const [currentSnapshot, setCurrentSnapshot] = useState<TickSnapshot | null>(
    null,
  );
  const [allDamageEvents, setAllDamageEvents] = useState<DamageEvent[]>([]);

  const { data: playerData, isLoading: isLoadingPlayer } = useSquads({
    playerId: PLAYER_ID,
  });
  const { data: opponentData, isLoading: isLoadingOpponent } =
    useOpponentSquads({
      opponentId: OPPONENT_ID,
    });

  const handleChange = (
    fn: (value: React.SetStateAction<number>) => void,
    val: string | null,
  ) => {
    fn(Number(val));
  };

  const getSlot = (
    data: Squad[] | undefined,
    squadNumber: number,
  ): SquadSlot[] | null => {
    return (
      data?.find((squad) => squad.squadNumber === squadNumber)?.slots ?? null
    );
  };

  const { mutate } = useMutation({
    mutationFn: (request: AdvisorRequest) => analyze(request),
    onSuccess: (data) => {
      setResult(data);
      stompClient.deactivate();
    },
    onError: (error) => {
      console.error(error);
    },
  });

  const handleAnalyze = () => {
    setAllDamageEvents([]);
    setCurrentSnapshot(null);
    if (!playerData || !opponentData) return;
    const battleId = crypto.randomUUID();

    stompClient.activate();

    stompClient.onConnect = () => {
      stompClient.subscribe(`/topic/battle/${battleId}`, (message) => {
        const snapshot: TickSnapshot = JSON.parse(message.body);
        setCurrentSnapshot(snapshot);
        setAllDamageEvents((prev) => [...prev, ...snapshot.damageEvents]);
      });

      mutate({
        playerSquadId: selectedSquad,
        enemySquadId: selectedOpponentSquad,
        battleId: battleId,
      });
    };
  };
  return (
    <div className="flex flex-col gap-6 p-6 max-w-2xl mx-auto">
      <h1 className="text-sm font-medium text-white/70 tracking-widest uppercase">
        Advisor
      </h1>

      {/* Selectors */}
      <div className="flex items-center gap-4 ">
        <div className="flex flex-col gap-1 flex-1">
          <label className="text-[10px] text-white/35 tracking-widest uppercase">
            Your Squad
          </label>
          <Select
            value={String(selectedSquad)}
            onValueChange={(val) => handleChange(setSelectedSquad, val)}
          >
            <SelectTrigger className="w-45">
              <SelectValue placeholder="Pick squad">
                {selectedSquad ? `Squad ${selectedSquad}` : "Pick squad"}
              </SelectValue>
            </SelectTrigger>
            <SelectContent>
              <SelectGroup>
                {isLoadingPlayer && <SkeletonLoader items={5} />}
                {!isLoadingPlayer &&
                  playerData?.map((item) => (
                    <SelectItem
                      key={`squad-${item.id}`}
                      value={item.squadNumber}
                    >
                      Squad {item.squadNumber}
                    </SelectItem>
                  ))}
              </SelectGroup>
            </SelectContent>
          </Select>
        </div>

        <div className="flex flex-col gap-1 flex-1">
          <label className="text-[10px] text-white/35 tracking-widest uppercase">
            Opponent
          </label>
          <Select
            value={String(selectedOpponentSquad)}
            onValueChange={(val) => handleChange(setSelectedOpponentSquad, val)}
          >
            <SelectTrigger className="w-45">
              <SelectValue placeholder="Pick squad">
                {selectedOpponentSquad
                  ? `Opponent Squad ${selectedOpponentSquad}`
                  : "Pick opponent"}
              </SelectValue>
            </SelectTrigger>
            <SelectContent>
              <SelectGroup>
                {isLoadingOpponent && <SkeletonLoader items={5} />}
                {!isLoadingOpponent &&
                  opponentData?.map((item) => (
                    <SelectItem
                      key={`opponent-squad-${item.id}`}
                      value={item.squadNumber}
                    >
                      Squad {item.squadNumber}
                    </SelectItem>
                  ))}
              </SelectGroup>
            </SelectContent>
          </Select>
        </div>
        <Button onClick={() => setSelectedOpponentSquad(0)}>Reset</Button>
      </div>

      {/* Battlefield */}
      <BattlefieldLayout
        player={getSlot(playerData, selectedSquad)}
        opponent={getSlot(opponentData, selectedOpponentSquad)}
        snapshot={currentSnapshot}
        allDamageEvents={allDamageEvents}
      />

      {/* Analyze button */}
      <button
        className="w-full py-2.5 rounded-xl border border-white/20 bg-white/5 text-sm text-white/70 hover:bg-white/10 hover:text-white/90 transition-all disabled:opacity-30 disabled:cursor-not-allowed"
        onClick={handleAnalyze}
        disabled={!selectedOpponentSquad}
      >
        Analyze
      </button>

      {/* Result */}
      {result && (
        <AnalysisResult tier={result.tier} explanation={result.explanation} />
      )}
    </div>
  );
}
