import { squadsQuery } from "@/api/squads";
import { APP_PATHS, PLAYER_ID } from "@/constants";
import { SkeletonLoader } from "@/elements/SkeletonLoader";
import { SquadCard } from "@/elements/SquadCard";
import { useSquads } from "@/hooks/useSquads";
import { createFileRoute, useNavigate } from "@tanstack/react-router";

export const Route = createFileRoute("/squads/")({
  component: SquadsPage,
  loader: async ({ context }) => {
    await context.queryClient.ensureQueryData(squadsQuery(PLAYER_ID));
  },
});

function SquadsPage() {
  const navigate = useNavigate();

  const { data, isLoading } = useSquads({
    playerId: PLAYER_ID,
  });

  const displaySquads = [1, 2, 3].map(
    (n) =>
      data?.find((s) => s.squadNumber === n) ?? {
        id: n,
        squadNumber: n,
        slots: [],
      },
  );

  return (
    <div className="p-8">
      <div className="mb-8">
        <h1 className="text-2xl font-bold text-white">Squads</h1>
        <p className="mt-1 text-sm text-white/40">
          Configure your hero formations and gear loadouts.
        </p>
      </div>

      <div className="grid grid-cols-1 gap-4 sm:grid-cols-3">
        {isLoading && <SkeletonLoader items={3} />}
        {!isLoading &&
          displaySquads?.map((squad) => (
            <SquadCard
              key={squad.id}
              squad={squad}
              squadNumber={squad.squadNumber}
              onClick={() =>
                navigate({
                  to: APP_PATHS.squad,
                  params: { squadId: String(squad.squadNumber) },
                })
              }
            />
          ))}
      </div>
    </div>
  );
}
