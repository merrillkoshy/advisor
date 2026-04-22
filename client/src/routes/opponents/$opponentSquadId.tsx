import { saveOpponentSquad } from "@/api/opponents";
import { APP_PATHS, OPPONENT_ID } from "@/constants";
import type { SquadSlotRequest } from "@/dto";
import { SquadEditor } from "@/elements/SquadEditor";
import { useOpponentSquad } from "@/hooks/useOpponentSquads";
import type { Slot } from "@/types";
import { useMutation, useQueryClient } from "@tanstack/react-query";
import { createFileRoute, useNavigate } from "@tanstack/react-router";
import { ChevronLeft } from "lucide-react";

export const Route = createFileRoute("/opponents/$opponentSquadId")({
  component: OpponentEditorPage,
});

function OpponentEditorPage() {
  const { opponentSquadId } = Route.useParams();
  const navigate = useNavigate();
  const queryClient = useQueryClient();

  const { data } = useOpponentSquad({
    opponentId: OPPONENT_ID,
    squadNumber: Number(opponentSquadId),
  });

  const { mutate } = useMutation({
    mutationFn: (slots: SquadSlotRequest[]) =>
      saveOpponentSquad(OPPONENT_ID, Number(opponentSquadId), slots),
    onSuccess: () => {
      queryClient.invalidateQueries({
        queryKey: ["opponentSquads", OPPONENT_ID],
      });
      navigate({ to: APP_PATHS.opponents });
    },
    onError: (error) => {
      console.error(error);
    },
  });

  const initialSlots: Slot[] = (data?.slots ?? []).map((s) => ({
    position: s.position,
    index: s.slotIndex,
    hero: s.hero,
    gear: {
      gun: s.gunStars,
      armor: s.armorStars,
      chip: s.chipStars,
      radar: s.radarStars,
    },
  }));

  const onSave = (slots: Slot[]) => {
    const mappedSlots: SquadSlotRequest[] = slots
      .filter((s) => s.hero !== null)
      .map((s) => ({
        position: s.position,
        slotIndex: s.index,
        heroId: s.hero!.id,
        gunStars: s.gear.gun,
        armorStars: s.gear.armor,
        chipStars: s.gear.chip,
        radarStars: s.gear.radar,
      }));

    mutate(mappedSlots);
  };

  return (
    <div className="p-8">
      <button
        onClick={() => navigate({ to: APP_PATHS.opponents })}
        className="flex items-center gap-1 text-sm text-white/40 hover:text-white/80 transition-colors mb-6"
      >
        <ChevronLeft className="w-4 h-4" />
        Squads
      </button>
      <div className="mb-8">
        <h1 className="text-2xl font-bold text-white">
          {opponentSquadId !== "undefined"
            ? `Squad ${opponentSquadId}`
            : `Create a squad`}
        </h1>
        <p className="mt-1 text-sm text-white/40">
          Configure your formation and gear loadout.
        </p>
      </div>
      <SquadEditor initialSlots={initialSlots} onSave={onSave} />
    </div>
  );
}
