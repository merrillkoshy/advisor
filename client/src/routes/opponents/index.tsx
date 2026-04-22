import { opponentSquadsQuery } from "@/api/opponents";
import { APP_PATHS, OPPONENT_ID } from "@/constants";
import { SkeletonLoader } from "@/elements/SkeletonLoader";
import { SquadCard } from "@/elements/SquadCard";
import { useOpponentSquads } from "@/hooks/useOpponentSquads";
import { createFileRoute, useNavigate } from "@tanstack/react-router";

export const Route = createFileRoute("/opponents/")({
  component: OpponentsPage,
  loader: ({ context }) =>
    context.queryClient.ensureQueryData(opponentSquadsQuery(OPPONENT_ID)),
  errorComponent: ({ error }) => <div>{error.message}</div>,
});

function OpponentsPage() {
  const navigate = useNavigate();

  const { data, isLoading } = useOpponentSquads({
    opponentId: OPPONENT_ID,
  });

  const displaySquads = [1].map(
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
        <h1 className="text-2xl font-bold text-white">Opponent Squads</h1>
        <p className="mt-1 text-sm text-white/40">
          Configure opponent formations and gear loadouts.
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
                  to: APP_PATHS.opponent,
                  params: { opponentSquadId: String(squad.squadNumber) },
                })
              }
            />
          ))}
      </div>
    </div>
  );
}
