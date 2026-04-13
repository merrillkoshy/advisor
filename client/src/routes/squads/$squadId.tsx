// routes/squads/$squadId.tsx
import { saveSquad, squadQuery } from "@/api/squads";
import { APP_PATHS, PLAYER_ID } from "@/constants";
import { SquadEditor } from "@/elements/SquadEditor";
import { useSquad } from "@/hooks/useSquads";
import type { Slot, SquadSlotRequest } from "@/types";
import { useMutation, useQueryClient } from "@tanstack/react-query";
import { createFileRoute, useNavigate } from "@tanstack/react-router";
import { ChevronLeft } from "lucide-react";

export const Route = createFileRoute("/squads/$squadId")({
  component: SquadEditorPage,
  loader: async ({ context, params }) => {
    await context.queryClient.ensureQueryData(
      squadQuery(PLAYER_ID, Number(params.squadId)),
    );
  },
});

function SquadEditorPage() {
  const { squadId } = Route.useParams();
  const navigate = useNavigate();
  const queryClient = useQueryClient();

  const { data } = useSquad({
    playerId: PLAYER_ID,
    squadNumber: Number(squadId),
  });

  const { mutate } = useMutation({
    mutationFn: (slots: SquadSlotRequest[]) =>
      saveSquad(PLAYER_ID, Number(squadId), slots),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ["squads", PLAYER_ID] });
      navigate({ to: APP_PATHS.squads });
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
        onClick={() => navigate({ to: APP_PATHS.squads })}
        className="flex items-center gap-1 text-sm text-white/40 hover:text-white/80 transition-colors mb-6"
      >
        <ChevronLeft className="w-4 h-4" />
        Squads
      </button>
      <div className="mb-8">
        <h1 className="text-2xl font-bold text-white">Squad {squadId}</h1>
        <p className="mt-1 text-sm text-white/40">
          Configure your formation and gear loadout.
        </p>
      </div>
      <SquadEditor initialSlots={initialSlots} onSave={onSave} />
    </div>
  );
}
